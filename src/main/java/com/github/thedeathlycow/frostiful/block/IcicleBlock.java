package com.github.thedeathlycow.frostiful.block;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.FrostifulConfig;
import com.github.thedeathlycow.frostiful.entity.damage.FDamageSources;
import com.github.thedeathlycow.frostiful.mixins.entity.FallingBlockEntityAccessor;
import com.github.thedeathlycow.frostiful.registry.FBlocks;
import com.github.thedeathlycow.frostiful.registry.tag.FBlockTags;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.minecraft.block.*;
import net.minecraft.block.enums.Thickness;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Icicles are growths of ice that sometimes will fall and
 * damage players.
 * Largely based on code from {@link PointedDripstoneBlock}.
 */
@SuppressWarnings("deprecation")
public class IcicleBlock extends Block implements LandingBlock, Waterloggable {

    /**
     * Icicles can point up and down
     */
    public static final DirectionProperty VERTICAL_DIRECTION = Properties.VERTICAL_DIRECTION;
    /**
     * Icicles have varying levels of thickness
     */
    public static final EnumProperty<Thickness> THICKNESS = Properties.THICKNESS;
    /**
     * Icicles can be waterlogged
     */
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    /**
     * An unstable icicle is about to fall
     */
    public static final BooleanProperty UNSTABLE = Properties.UNSTABLE;

    private static final VoxelShape TIP_MERGE_SHAPE = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape UP_TIP_SHAPE = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final VoxelShape DOWN_TIP_SHAPE = Block.createCuboidShape(5.0D, 5.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape BASE_SHAPE = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    private static final VoxelShape FRUSTUM_SHAPE = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    private static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    private static final IntProvider UNSTABLE_TICKS_BEFORE_FALL = UniformIntProvider.create(40, 80);

    public IcicleBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(VERTICAL_DIRECTION, Direction.UP)
                .with(THICKNESS, Thickness.TIP)
                .with(WATERLOGGED, false)
                .with(UNSTABLE, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VERTICAL_DIRECTION, THICKNESS, WATERLOGGED, UNSTABLE);
    }

    /**
     * Determine if the state can legally be placed as an icicle
     *
     * @param state State of the icicle to test
     * @param world World the icicle is in
     * @param pos   The position of the icicle
     * @return Returns true if the icicle can be placed, false otherwise
     */
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return canPlaceAtWithDirection(world, pos, state.get(VERTICAL_DIRECTION));
    }

    /**
     * Icicles collapse when hit by any projectile
     *
     * @param world      The world the icicle is in
     * @param state      The state of the icicle
     * @param hit        How the projectile hit the icicle
     * @param projectile The projectile that hit the icicle
     */
    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        BlockPos blockPos = hit.getBlockPos();
        if (!world.isClient && projectile.canModifyAt(world, blockPos) && projectile.getVelocity().length() > 0.6D) {
            world.breakBlock(blockPos, true);
        }
    }

    /**
     * Hurt entities when they fall on icicles
     *
     * @param world        The world the icicle is in
     * @param state        The state of the icicle
     * @param pos          The position of the icicle
     * @param entity       The entity that fell
     * @param fallDistance How far the entity fell
     */
    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (state.get(VERTICAL_DIRECTION) == Direction.UP) {
            DamageSource damageSource = FDamageSources.getDamageSources(world).frostiful$icicle();
            boolean tookDamage = entity.handleFallDamage(fallDistance + 2.0F, 2.0F, damageSource);
            if (tookDamage && entity instanceof LivingEntity livingEntity) {
                FrostifulConfig config = Frostiful.getConfig();
                livingEntity.thermoo$addTemperature(
                        -config.icicleConfig.getIcicleCollisionFreezeAmount(),
                        HeatingModes.ACTIVE
                );
            }
        } else {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
        }
    }

    /**
     * Get the state of an icicle to be placed
     *
     * @param ctx Context for placement
     * @return Returns the {@link BlockState} to be placed
     */
    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        WorldAccess worldAccess = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction lookingDirection = ctx.getVerticalPlayerLookDirection().getOpposite();
        Direction direction = getDirectionToPlaceAt(worldAccess, blockPos, lookingDirection);
        if (direction == null) {
            return null;
        }

        Thickness thickness = getThickness(worldAccess, blockPos, direction, !ctx.shouldCancelInteraction());

        boolean unstable = false;
        if (direction == Direction.DOWN) {
            BlockState state = worldAccess.getBlockState(blockPos.up());
            unstable = isUnstable(state) && isHeldByIcicle(state, worldAccess, blockPos);
        }
        return thickness == null ? null : this.getDefaultState()
                .with(VERTICAL_DIRECTION, direction)
                .with(THICKNESS, thickness)
                .with(WATERLOGGED, worldAccess.getFluidState(blockPos).getFluid() == Fluids.WATER)
                .with(UNSTABLE, unstable);
    }

    /**
     * Gets an updated block state to change the icicle to when a neighbour is updated.
     * Handles fluid ticking when water logged.
     * Schedules the icicle to be broken if its support has been broken or if its support
     * is unstable.
     *
     * @param state         the state of this block
     * @param direction     the direction from this block to the neighbor
     * @param neighborState the state of the updated neighbor block
     * @param world         the world
     * @param pos           the position of this block
     * @param neighborPos   the position of the neighbor block
     * @return Returns the updated {@link BlockState} of the icicle
     */
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (direction != Direction.UP && direction != Direction.DOWN) {
            return state;
        } else {
            Direction pointingIn = state.get(VERTICAL_DIRECTION);
            if (pointingIn == Direction.DOWN && world.getBlockTickScheduler().isQueued(pos, this)) {
                return state;
            } else if (direction == pointingIn.getOpposite() && !this.canPlaceAt(state, world, pos)) {
                if (pointingIn == Direction.DOWN) {
                    world.scheduleBlockTick(pos, this, 2);
                } else {
                    world.scheduleBlockTick(pos, this, 1);
                }

                return state;
            } else {
                boolean tryMerge = state.get(THICKNESS) == Thickness.TIP_MERGE;
                Thickness thickness = getThickness(world, pos, pointingIn, tryMerge);

                boolean makeUnstable = isUnstable(state)
                        || (isHeldByIcicle(state, world, pos)
                        && isUnstable(neighborState)
                        && direction == Direction.UP);

                return state.with(THICKNESS, thickness).with(UNSTABLE, makeUnstable);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (isPointingUp(state) && !this.canPlaceAt(state, world, pos)) {
            world.breakBlock(pos, true);
        } else {
            spawnFallingBlock(state, world, pos);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (isPointingDown(state)) {
            FrostifulConfig config = Frostiful.getConfig();
            if (random.nextFloat() < config.icicleConfig.getBecomeUnstableChance() && isHeldByIcicleFallable(state, world, pos)) { // fall
                this.tryFall(state, world, pos, random);
            }
            final double growChance = this.getGrowChance(world);
            if (random.nextFloat() < growChance) { // grow
                this.tryGrowIcicle(state, world, pos, random);
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (isUnstable(state)) {
            createUnstableParticle(world, pos, state);
        }
    }

    @Override
    public DamageSource getDamageSource(Entity attacker) {
        return FDamageSources.getDamageSources(attacker.getWorld()).frostiful$fallingIcicle(attacker);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Boolean.TRUE.equals(state.get(WATERLOGGED))
                ? Fluids.WATER.getStill(false)
                : super.getFluidState(state);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Thickness thickness = state.get(THICKNESS);
        VoxelShape voxelShape;
        if (thickness == Thickness.TIP_MERGE) {
            voxelShape = TIP_MERGE_SHAPE;
        } else if (thickness == Thickness.TIP) {
            if (state.get(VERTICAL_DIRECTION) == Direction.DOWN) {
                voxelShape = DOWN_TIP_SHAPE;
            } else {
                voxelShape = UP_TIP_SHAPE;
            }
        } else if (thickness == Thickness.FRUSTUM) {
            voxelShape = BASE_SHAPE;
        } else if (thickness == Thickness.MIDDLE) {
            voxelShape = FRUSTUM_SHAPE;
        } else {
            voxelShape = MIDDLE_SHAPE;
        }

        Vec3d vec3d = state.getModelOffset(world, pos);
        return voxelShape.offset(vec3d.x, 0.0D, vec3d.z);
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public float getMaxHorizontalModelOffset() {
        return 0.125F;
    }

    private Double getGrowChance(ServerWorld world) {
        FrostifulConfig config = Frostiful.getConfig();
        if (world.isThundering()) {
            return config.icicleConfig.getGrowChanceDuringThunder();
        } else if (world.isRaining()) {
            return config.icicleConfig.getGrowChanceDuringRain();
        } else {
            return config.icicleConfig.getGrowChance();
        }
    }

    private void tryFall(BlockState state, ServerWorld world, BlockPos pos, Random random) {

        if (isUnstable(state) || isPointingUp(state)) {
            return;
        }

        BlockPos tipPos = getTipPos(state, world, pos, 25, false);
        if (tipPos != null) {
            world.setBlockState(pos, state.with(UNSTABLE, true));
            world.scheduleBlockTick(pos, this, UNSTABLE_TICKS_BEFORE_FALL.get(random));
        }
    }

    private void tryGrowIcicle(BlockState state, ServerWorld world, BlockPos pos, Random random) {

        if (isUnstable(state) || isPointingUp(state)) {
            return;
        }

        BlockPos anchorPos = pos.up();
        BlockState anchor = world.getBlockState(anchorPos);
        if (canGrowIcicleOnAnchor(world, anchorPos, anchor)) {
            BlockPos tipPos = getTipPos(state, world, pos, 7, false);
            if (tipPos != null) {
                BlockState tipState = world.getBlockState(tipPos);
                if (!isUnstable(tipState) && isTipDown(tipState) && canGrow(tipState, world, tipPos)) {
                    tryGrow(world, tipPos, Direction.DOWN);
                }
            }
        }
    }

    private static void tryGrowGroundIcicle(ServerWorld world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.mutableCopy();

        for (int i = 0; i < 10; ++i) {
            mutable.move(Direction.DOWN);
            BlockState blockState = world.getBlockState(mutable);
            if (!blockState.getFluidState().isEmpty()) {
                return;
            }

            if (isTip(blockState, Direction.UP) && canGrow(blockState, world, mutable)) {
                tryGrow(world, mutable, Direction.UP);
                return;
            }

            if (canPlaceAtWithDirection(world, mutable, Direction.UP)) {
                tryGrow(world, mutable.down(), Direction.UP);
                return;
            }
        }
    }

    private static void tryGrow(ServerWorld world, BlockPos pos, Direction direction) {
        BlockPos posGrowingInto = pos.offset(direction);
        BlockState stateGrowingInto = world.getBlockState(posGrowingInto);
        if (isTip(stateGrowingInto, direction.getOpposite())) {
            growMerged(stateGrowingInto, world, posGrowingInto);
        } else if (stateGrowingInto.isAir() || stateGrowingInto.isOf(Blocks.WATER)) {
            place(world, posGrowingInto, direction, Thickness.TIP);
        }
    }

    private static void place(WorldAccess world, BlockPos pos, Direction direction, Thickness thickness) {
        BlockState blockState = FBlocks.ICICLE.getDefaultState().with(VERTICAL_DIRECTION, direction).with(THICKNESS, thickness).with(WATERLOGGED, world.getFluidState(pos).getFluid() == Fluids.WATER);
        world.setBlockState(pos, blockState, 3);
    }

    private static void growMerged(BlockState state, WorldAccess world, BlockPos pos) {
        BlockPos upperPos;
        BlockPos lowerPos;
        if (state.get(VERTICAL_DIRECTION) == Direction.UP) {
            lowerPos = pos;
            upperPos = pos.up();
        } else {
            upperPos = pos;
            lowerPos = pos.down();
        }

        place(world, upperPos, Direction.DOWN, Thickness.TIP_MERGE);
        place(world, lowerPos, Direction.UP, Thickness.TIP_MERGE);
    }

    @Nullable
    private static BlockPos getTipPos(BlockState state, WorldAccess world, BlockPos pos, int range, boolean allowMerged) {
        if (isTip(state, allowMerged)) {
            return pos;
        } else {
            Direction direction = state.get(VERTICAL_DIRECTION);
            BiPredicate<BlockPos, BlockState> continuePredicate = (posx, statex) -> {
                return statex.isOf(FBlocks.ICICLE) && statex.get(VERTICAL_DIRECTION) == direction;
            };
            Predicate<BlockState> stopPredicate = (statex) -> {
                return isTip(statex, allowMerged);
            };
            return searchInDirection(world, pos, direction.getDirection(), continuePredicate, stopPredicate, range).orElse(null);
        }
    }

    public static boolean isTipDown(BlockState state) {
        return isPointingDown(state) && state.get(THICKNESS) == Thickness.TIP;
    }

    private static boolean canGrow(BlockState state, ServerWorld world, BlockPos pos) {
        Direction direction = state.get(VERTICAL_DIRECTION);
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        if (!blockState.getFluidState().isEmpty()) {
            return false;
        } else {
            return blockState.isAir() || isTip(blockState, direction.getOpposite());
        }
    }

    private static boolean canGrowIcicleOnAnchor(ServerWorld world, BlockPos anchorPos, BlockState anchorState) {

        if (world.isRaining()) {
            Biome biome = world.getBiome(anchorPos).value();
            return biome.isCold(anchorPos);
        }

        return anchorState.isIn(FBlockTags.ICICLE_GROWABLE);
    }

    private static Optional<BlockPos> searchInDirection(WorldAccess world, BlockPos pos, Direction.AxisDirection direction, BiPredicate<BlockPos, BlockState> continuePredicate, Predicate<BlockState> stopPredicate, int range) {
        Direction toMove = Direction.get(direction, Direction.Axis.Y);
        BlockPos.Mutable current = pos.mutableCopy();

        for (int i = 1; i < range; i++) {
            current.move(toMove);
            BlockState blockState = world.getBlockState(current);
            if (stopPredicate.test(blockState)) {
                return Optional.of(current.toImmutable());
            }

            if (world.isOutOfHeightLimit(current.getY()) || !continuePredicate.test(current, blockState)) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    private static boolean canPlaceAtWithDirection(WorldView world, BlockPos pos, Direction direction) {
        BlockPos candidateAnchorPos = pos.offset(direction.getOpposite());
        BlockState candidateAnchorState = world.getBlockState(candidateAnchorPos);
        return candidateAnchorState.isSideSolidFullSquare(world, candidateAnchorPos, direction)
                || isIcicleFacingDirection(candidateAnchorState, direction);
    }

    @Nullable
    private static Direction getDirectionToPlaceAt(WorldView world, BlockPos pos, Direction direction) {
        Direction toPlace;
        if (canPlaceAtWithDirection(world, pos, direction)) {
            toPlace = direction;
        } else {
            if (!canPlaceAtWithDirection(world, pos, direction.getOpposite())) {
                return null;
            }
            toPlace = direction.getOpposite();
        }

        return toPlace;
    }

    private static Thickness getThickness(WorldView world, BlockPos pos, Direction direction, boolean tryMerge) {
        Direction direction2 = direction.getOpposite();
        BlockState blockState = world.getBlockState(pos.offset(direction));
        if (isIcicleFacingDirection(blockState, direction2)) {
            return !tryMerge && blockState.get(THICKNESS) != Thickness.TIP_MERGE ? Thickness.TIP : Thickness.TIP_MERGE;
        } else if (!isIcicleFacingDirection(blockState, direction)) {
            return Thickness.TIP;
        } else {
            Thickness thickness = blockState.get(THICKNESS);
            if (thickness != Thickness.TIP && thickness != Thickness.TIP_MERGE) {
                BlockState blockState2 = world.getBlockState(pos.offset(direction2));
                return !isIcicleFacingDirection(blockState2, direction) ? Thickness.BASE : Thickness.MIDDLE;
            } else {
                return Thickness.FRUSTUM;
            }
        }
    }

    private static boolean isUnstable(BlockState state) {
        return state.isOf(FBlocks.ICICLE) && state.get(UNSTABLE);
    }

    private static boolean isPointingUp(BlockState state) {
        return isIcicleFacingDirection(state, Direction.UP);
    }

    private static boolean isPointingDown(BlockState state) {
        return isIcicleFacingDirection(state, Direction.DOWN);
    }

    private static boolean isIcicleFacingDirection(BlockState state, Direction direction) {
        return state.isOf(FBlocks.ICICLE) && state.get(VERTICAL_DIRECTION) == direction;
    }

    private static boolean isTip(BlockState state, Direction direction) {
        return isTip(state, false) && state.get(VERTICAL_DIRECTION) == direction;
    }

    private static boolean isTip(BlockState state, boolean allowMerged) {
        if (!state.isOf(FBlocks.ICICLE)) {
            return false;
        } else {
            Thickness thickness = state.get(THICKNESS);
            return thickness == Thickness.TIP || (allowMerged && thickness == Thickness.TIP_MERGE);
        }
    }

    private static void spawnFallingBlock(BlockState state, ServerWorld world, BlockPos pos) {
        BlockPos.Mutable current = pos.mutableCopy();

        for (BlockState blockState = state; isPointingDown(blockState); blockState = world.getBlockState(current)) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(world, current, blockState);
            fallingBlockEntity.dropItem = false;
            ((FallingBlockEntityAccessor) fallingBlockEntity).frostiful$setDestroyOnLanding(true);
            if (isTip(blockState, true)) {
                float fallHurtAmount = Math.max(1 + pos.getY() - current.getY(), 6);
                fallingBlockEntity.setHurtEntities(fallHurtAmount, 40);
            }

            current.move(Direction.DOWN);
        }
    }

    private static boolean isHeldByIcicleFallable(BlockState state, WorldView world, BlockPos pos) {
        return !(world.getBlockState(pos.up()).isIn(FBlockTags.ICICLE_GROWABLE)) || isHeldByIcicle(state, world, pos);
    }

    private static boolean isHeldByIcicle(BlockState state, WorldView world, BlockPos pos) {
        return isPointingDown(state) && world.getBlockState(pos.up()).isOf(FBlocks.ICICLE);
    }

    private static void createUnstableParticle(World world, BlockPos pos, BlockState state) {
        Vec3d vec3d = state.getModelOffset(world, pos);
        double xOffset = (double) pos.getX() + 0.5D + vec3d.x;
        double yOffset = (double) ((float) (pos.getY() + 1) - 0.6875F) - 0.0625D;
        double zOffset = (double) pos.getZ() + 0.5D + vec3d.z;
        world.addParticle(ParticleTypes.SNOWFLAKE, xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D);
    }
}
