package com.github.thedeathlycow.frostiful.entity;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.block.FrozenTorchBlock;
import com.github.thedeathlycow.frostiful.config.FrostifulConfig;
import com.github.thedeathlycow.frostiful.config.group.CombatConfigGroup;
import com.github.thedeathlycow.frostiful.item.FrostWandItem;
import com.github.thedeathlycow.frostiful.item.enchantment.HeatDrainEnchantmentEffect;
import com.github.thedeathlycow.frostiful.registry.FEntityTypes;
import com.github.thedeathlycow.frostiful.registry.FItems;
import com.github.thedeathlycow.frostiful.registry.tag.FBlockTags;
import com.github.thedeathlycow.frostiful.registry.tag.FDamageTypeTags;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractTorchBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

/**
 * By remapping {@link SpellcastingIllagerEntity.Spell}s, the Frostologer has the following spells:
 * <p>
 * SUMMON_VEX = SUMMON_MINIONS
 * DISAPPEAR = DESTROY_HEAT_SOURCES
 */
public class FrostologerEntity extends SpellcastingIllagerEntity implements RangedAttackMob {

    private static final TrackedData<Boolean> IS_USING_FROST_WAND = DataTracker.registerData(
            FrostologerEntity.class, TrackedDataHandlerRegistry.BOOLEAN
    );

    public static final float MAX_POWER_SCALE_START = -0.95f;
    private static final int NUM_POWER_PARTICLES = 2;
    private static final float START_PLACING_SNOW_TEMP = -0.8f;


    public float prevStrideDistance;
    public float strideDistance;
    public double prevCapeX;
    public double prevCapeY;
    public double prevCapeZ;
    public double capeX;
    public double capeY;
    public double capeZ;
    private boolean isChanneling = false;

    private final BlockPos[] stepPositionsPool = new BlockPos[2];

    public FrostologerEntity(EntityType<? extends FrostologerEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 20;
    }

    public static DefaultAttributeContainer.Builder createFrostologerAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 150.0)
                .add(ThermooAttributes.MIN_TEMPERATURE, 5.0);
    }

    public boolean isInHeatedArea() {
        World world = this.getWorld();
        EnvironmentController controller = EnvironmentManager.INSTANCE.getController();

        CombatConfigGroup config = Frostiful.getConfig().combatConfig;
        int intolerableHeat = config.getFrostologerIntolerableHeat();

        return world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)
                && controller.getHeatAtLocation(world, this.getBlockPos()) > intolerableHeat;
    }

    public boolean isAtMaxPower() {
        return this.thermoo$getTemperatureScale() <= MAX_POWER_SCALE_START;
    }

    /**
     * 'Destroys' the heat source at the given `blockPos` by transforming `state` into another block according to the following rules:
     * <ul>
     * <li> protected block -> do nothing </li>
     * <li> full cubes -> ice </li>
     * <li> lava (level 8) -> obsidian </li>
     * <li> torches -> frozen torch </li>
     * <li> waterlogged blocks -> ice </li>
     * <li> everything else -> air </li>
     * </ul>
     *
     * @param world    The server world
     * @param state    The state to transform
     * @param blockPos The position of `state` in `world`.
     */
    public void destroyHeatSource(ServerWorld world, BlockState state, BlockPos blockPos) {

        BlockState frozenState;
        Block heatedBlock = state.getBlock();
        FluidState fluidState = state.getFluidState();

        if (state.isIn(FBlockTags.FROSTOLOGER_CANNOT_FREEZE)) {
            frozenState = state;
        } else if (blockPos.equals(this.getBlockPos())) {
            if (fluidState.isIn(FluidTags.WATER)) {
                frozenState = Blocks.WATER.getDefaultState();
            } else {
                frozenState = Blocks.AIR.getDefaultState();
            }
        } else if (state.isIn(FBlockTags.HOT_FLOOR)) {
            frozenState = Blocks.COBBLESTONE.getDefaultState();
        } else if (state.isFullCube(world, blockPos)) {
            frozenState = Blocks.ICE.getDefaultState();
        } else if (fluidState.isOf(Fluids.LAVA) && fluidState.getLevel() == 8) {
            frozenState = Blocks.OBSIDIAN.getDefaultState();
        } else if (heatedBlock instanceof AbstractTorchBlock) {
            BlockState torch = FrozenTorchBlock.freezeTorch(state);
            frozenState = torch != null ? torch : Blocks.AIR.getDefaultState();
        } else if (state.contains(Properties.WATERLOGGED) && Boolean.TRUE.equals(state.get(Properties.WATERLOGGED))) {
            frozenState = Blocks.ICE.getDefaultState();
        } else {
            frozenState = Blocks.AIR.getDefaultState();
        }

        if (!frozenState.isAir()) {
            world.setBlockState(blockPos, frozenState);
        } else {
            world.breakBlock(blockPos, true);
        }

        world.playSound(
                null,
                blockPos,
                SoundEvents.BLOCK_FIRE_EXTINGUISH,
                SoundCategory.HOSTILE,
                0.5f, 1.0f + ((this.random.nextFloat() % 0.2f) - 0.1f)
        );

        Vec3d centeredPos = Vec3d.ofCenter(blockPos);

        world.spawnParticles(
                ParticleTypes.SMOKE,
                centeredPos.x, centeredPos.y, centeredPos.z,
                12,
                0.1, 1, 0.1,
                0.1
        );
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (damageSource.isIn(DamageTypeTags.IS_PROJECTILE) && this.isChanneling()) {
            return true;
        }

        return damageSource.isIn(DamageTypeTags.IS_FREEZING)
                || damageSource.isIn(FDamageTypeTags.IS_ICICLE)
                || super.isInvulnerableTo(damageSource);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new SpellcastingIllagerEntity.LookAtTargetGoal());

        this.goalSelector.add(2, new FrostWandCastGoal(this, 1.0, 40, 10f));

        this.goalSelector.add(2, new FleeEntityGoal<>(this, IronGolemEntity.class, 8.0F, 1.2, 1.5));

        this.goalSelector.add(4, new FrostWandAttackGoal());
        this.goalSelector.add(4, new SummonMinionsGoal());
        this.goalSelector.add(4, new IcicleAttackGoal(UniformIntProvider.create(20, 30)));

        this.goalSelector.add(6, new DestroyHeatSourcesGoal(15));

        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));

        this.targetSelector.add(
                1,
                new RevengeGoal(this, RaiderEntity.class)
                        .setGroupRevenge()
        );
        this.targetSelector.add(
                2,
                new ActiveTargetGoal<>(this, PlayerEntity.class, true)
                        .setMaxTimeWithoutVisibility(300)
        );
        this.targetSelector.add(
                3,
                new ActiveTargetGoal<>(this, MerchantEntity.class, false)
                        .setMaxTimeWithoutVisibility(300)
        );
        this.targetSelector.add(
                3,
                new ActiveTargetGoal<>(this, IronGolemEntity.class, false)
        );
    }

    @Nullable
    @Override
    public EntityData initialize(
            ServerWorldAccess world,
            LocalDifficulty difficulty,
            SpawnReason spawnReason,
            @Nullable EntityData entityData
    ) {
        this.initEquipment(world.getRandom(), difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty difficulty) {
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(FItems.FROST_WAND));
        this.equipStack(EquipmentSlot.CHEST, new ItemStack(FItems.FROSTOLOGY_CLOAK));
        // TODO: figure out enchanting equipment
        // this.enchantMainHandItem(this.getWorld(), random, difficulty.getClampedLocalDifficulty());

        // equipment drops handled with loot table
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0f);
        this.setEquipmentDropChance(EquipmentSlot.CHEST, 0.0f);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_USING_FROST_WAND, false);
    }

    @Override
    public void tick() {
        super.tick();

        this.updateCapeAngles();

        if (this.getWorld().isClient && this.isAtMaxPower()) {
            this.spawnPowerParticles();
        }

        FrostifulConfig config = Frostiful.getConfig();
        if (this.isTargetPlayer() && this.thermoo$getTemperatureScale() > -config.combatConfig.getFrostologerMaxPassiveFreezing()) {
            this.thermoo$addTemperature(
                    -config.combatConfig.getFrostologerPassiveFreezingPerTick(),
                    HeatingModes.PASSIVE
            );
        }
    }

    @Override
    public void tickRiding() {
        super.tickRiding();
        this.prevStrideDistance = this.strideDistance;
        this.strideDistance = 0.0F;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        this.prevStrideDistance = this.strideDistance;

        float walkSpeed;
        if (this.isOnGround() && !this.isDead() && !this.isSwimming()) {
            walkSpeed = Math.min(0.1F, (float) this.getVelocity().horizontalLength());
        } else {
            walkSpeed = 0.0F;
        }
        this.strideDistance += (walkSpeed - this.strideDistance) * 0.4f;

        World world = this.getWorld();
        if (world.isClient) {
            // dont place snow if client
            return;
        }

        // do not place snow/destroy heat sources unless mobGriefing is on
        if (!world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            return;
        }


        ServerWorld serverWorld = (ServerWorld) world; // covered by isClient check above

        BlockPos frostologerPos = this.getBlockPos();
        BlockState snow = Blocks.SNOW.getDefaultState();

        boolean canPlaceSnow;
        stepPositionsPool[0] = frostologerPos;
        stepPositionsPool[1] = frostologerPos.down();
        for (BlockPos blockPos : stepPositionsPool) {

            BlockState blockState = world.getBlockState(blockPos);
            if (EnvironmentManager.INSTANCE.getController().isHeatSource(blockState)) {
                this.destroyHeatSource(serverWorld, blockState, blockPos);
            }

            canPlaceSnow = blockState.isAir()
                    && this.thermoo$getTemperatureScale() <= START_PLACING_SNOW_TEMP
                    && snow.canPlaceAt(world, blockPos);
            if (canPlaceSnow) {
                world.setBlockState(blockPos, snow);
                world.emitGameEvent(
                        GameEvent.BLOCK_PLACE,
                        blockPos,
                        GameEvent.Emitter.of(this, blockState)
                );
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private void spawnPowerParticles() {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        Box box = this.getBoundingBox();

        for (int i = 0; i < NUM_POWER_PARTICLES; i++) {
            // pick random pos in bounding box
            double x = box.getMin(Direction.Axis.X) + random.nextDouble(box.getLengthX());
            double y = box.getMin(Direction.Axis.Y) + random.nextDouble(box.getLengthY());
            double z = box.getMin(Direction.Axis.Z) + random.nextDouble(box.getLengthZ());

            getWorld().addParticle(
                    ParticleTypes.SNOWFLAKE,
                    x, y, z,
                    0, 0, 0
            );
        }
    }

    @Override
    public IllagerEntity.State getState() {
        if (this.isSpellcasting()) {
            return State.SPELLCASTING;
        } else {
            return this.isCelebrating() ? State.CELEBRATING : State.NEUTRAL;
        }
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        if (this.activeItemStack.isOf(FItems.FROST_WAND)) {
            this.getLookControl().lookAt(target);
            FrostWandItem.fireFrostSpell(this.activeItemStack.copy(), this.getWorld(), this);
        }
    }

    public boolean hasTarget() {
        LivingEntity target = this.getTarget();
        return target != null && target.isAlive();
    }

    public boolean isTargetPlayer() {
        LivingEntity target = this.getTarget();
        return target != null && target.isAlive() && target.isPlayer();
    }

    public boolean isTargetRooted() {
        LivingEntity target = this.getTarget();
        return target != null
                && ((RootedEntity) target).frostiful$isRooted();
    }

    public boolean isUsingFrostWand() {
        return this.dataTracker.get(IS_USING_FROST_WAND);
    }

    @Override
    public void addBonusForWave(ServerWorld world, int wave, boolean unused) {

    }

    private void updateCapeAngles() {
        this.prevCapeX = this.capeX;
        this.prevCapeY = this.capeY;
        this.prevCapeZ = this.capeZ;
        double dx = this.getX() - this.capeX;
        double dy = this.getY() - this.capeY;
        double dz = this.getZ() - this.capeZ;
        double threshold = 10.0;
        if (dx > threshold) {
            this.capeX = this.getX();
            this.prevCapeX = this.capeX;
        }

        if (dz > threshold) {
            this.capeZ = this.getZ();
            this.prevCapeZ = this.capeZ;
        }

        if (dy > threshold) {
            this.capeY = this.getY();
            this.prevCapeY = this.capeY;
        }

        if (dx < -threshold) {
            this.capeX = this.getX();
            this.prevCapeX = this.capeX;
        }

        if (dz < -threshold) {
            this.capeZ = this.getZ();
            this.prevCapeZ = this.capeZ;
        }

        if (dy < -threshold) {
            this.capeY = this.getY();
            this.prevCapeY = this.capeY;
        }

        this.capeX += dx / 4;
        this.capeZ += dz / 4;
        this.capeY += dy / 4;
    }

    @Override
    public boolean isTeammate(@Nullable Entity other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else if (super.isTeammate(other)) {
            return true;
        } else if (other.getType() == FEntityTypes.BITER) {
            return this.isTeammate(((BiterEntity) other).getOwner());
        } else if (other instanceof LivingEntity otherEntity && otherEntity.getType().isIn(EntityTypeTags.ILLAGER_FRIENDS)) {
            return this.getScoreboardTeam() == null && other.getScoreboardTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return FSoundEvents.ENTITY_FROSTOLOGER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return FSoundEvents.ENTITY_FROSTOLOGER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return FSoundEvents.ENTITY_FROSTOLOGER_HURT;
    }

    @Override
    public SoundEvent getCelebratingSound() {
        return FSoundEvents.ENTITY_FROSTOLOGER_CELEBRATE;
    }

    @Override
    protected SoundEvent getCastSpellSound() {
        return FSoundEvents.ENTITY_FROSTOLOGER_CAST_SPELL;
    }

    public boolean isChanneling() {
        return isChanneling;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(IS_USING_FROST_WAND, nbt.getBoolean("IsUsingFrostWand"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsUsingFrostWand", this.dataTracker.get(IS_USING_FROST_WAND));
    }

    protected class FrostWandAttackGoal extends AttackGoal {
        public FrostWandAttackGoal() {
            super(FrostologerEntity.this);
        }

        @Override
        public boolean canStart() {
            return FrostologerEntity.this.isTargetRooted()
                    && super.canStart();
        }

    }

    protected class FrostWandCastGoal extends ProjectileAttackGoal {

        public FrostWandCastGoal(RangedAttackMob mob, double mobSpeed, int intervalTicks, float maxShootRange) {
            super(mob, mobSpeed, intervalTicks, maxShootRange);
        }

        @Override
        public boolean canStart() {
            return super.canStart()
                    && FrostologerEntity.this.hasTarget()
                    && !FrostologerEntity.this.isTargetRooted()
                    && FrostologerEntity.this.getMainHandStack().isOf(FItems.FROST_WAND);
        }

        @Override
        public void start() {
            super.start();
            FrostologerEntity.this.setAttacking(true);
            FrostologerEntity.this.setCurrentHand(Hand.MAIN_HAND);
            this.startUsingFrostWand();
        }

        @Override
        public void stop() {
            super.stop();
            FrostologerEntity.this.setAttacking(false);
            FrostologerEntity.this.clearActiveItem();
            this.stopUsingFrostWand();
        }

        private void startUsingFrostWand() {
            FrostologerEntity.this.playSound(
                    FSoundEvents.ITEM_FROST_WAND_PREPARE_CAST,
                    1.0f, 1.0f
            );
            FrostologerEntity.this.dataTracker.set(IS_USING_FROST_WAND, true);
        }

        private void stopUsingFrostWand() {
            FrostologerEntity.this.dataTracker.set(IS_USING_FROST_WAND, false);
        }
    }

    protected class DestroyHeatSourcesGoal extends SpellcastingIllagerEntity.CastSpellGoal {

        private final int range;

        protected DestroyHeatSourcesGoal(int range) {
            super();
            this.range = range;
        }

        @Override
        public void start() {
            super.start();
            if (FrostologerEntity.this.isOnFire()) {
                FrostologerEntity.this.extinguish();
                FrostologerEntity.this.playExtinguishSound();
            }
            FrostologerEntity.this.isChanneling = true;
        }

        @Override
        public boolean canStart() {
            // no super call as that requires a target to be selected
            if (FrostologerEntity.this.isSpellcasting()) {
                return false;
            } else if (FrostologerEntity.this.age < this.startTime) {
                return false;
            } else if (!FrostologerEntity.this.hasTarget()) {
                return false;
            } else {
                return FrostologerEntity.this.isOnFire()
                        || FrostologerEntity.this.isInHeatedArea();
            }
        }

        @Override
        public boolean shouldContinue() {
            return this.spellCooldown > 0;
        }

        @Override
        public void tick() {

            Box box = FrostologerEntity.this.getBoundingBox().expand(this.range);

            @Nullable
            ServerWorld serverWorld = null;
            World world = FrostologerEntity.this.getWorld();
            if (!world.isClient) {
                serverWorld = (ServerWorld) world;
            }

            if (FrostologerEntity.this.isOnFire()) {
                FrostologerEntity.this.extinguish();
                FrostologerEntity.this.playExtinguishSound();
            }

            int heatDrain = Frostiful.getConfig().combatConfig.getFrostologerHeatDrainPerTick();
            for (LivingEntity victim : world.getEntitiesByClass(LivingEntity.class, box, entity -> true)) {
                victim.thermoo$addTemperature(-heatDrain, HeatingModes.ACTIVE);

                if (serverWorld != null) {
                    HeatDrainEnchantmentEffect.addHeatDrainParticles(
                            serverWorld, victim, FrostologerEntity.this, 5, 0.08
                    );
                }
            }

            super.tick();
        }

        @Override
        protected void castSpell() {

            World world = getWorld();
            if (!world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                return;
            }

            BlockPos origin = FrostologerEntity.this.getBlockPos();
            Vec3i distance = new Vec3i(this.range, this.range, this.range);
            for (BlockPos pos : BlockPos.iterate(origin.subtract(distance), origin.add(distance))) {
                BlockState state = world.getBlockState(pos);
                if (EnvironmentManager.INSTANCE.getController().isHeatSource(state) && world instanceof ServerWorld serverWorld) {
                    FrostologerEntity.this.destroyHeatSource(serverWorld, state, pos);
                }
            }

            FrostologerEntity.this.isChanneling = false;
        }

        @Override
        protected int getInitialCooldown() {
            return 60;
        }

        @Override
        protected int getSpellTicks() {
            return 60;
        }

        @Override
        protected int startTimeDelay() {
            return 140;
        }

        @Nullable
        @Override
        protected SoundEvent getSoundPrepare() {
            return FSoundEvents.ENTITY_FROSTOLOGER_PREPARE_CAST_BLIZZARD;
        }

        @Override
        protected Spell getSpell() {
            return Spell.DISAPPEAR;
        }

    }

    protected class SummonMinionsGoal extends SpellcastingIllagerEntity.CastSpellGoal {
        private static final TargetPredicate IS_MINION = TargetPredicate.createNonAttackable()
                .setBaseMaxDistance(16.0)
                .ignoreVisibility()
                .ignoreDistanceScalingFactor();

        // I could make this configurable, but it's for the sake of stopping biter spam and not really a significant mechanic
        private static final int SUMMON_BITER_COOL_DOWN = 5 * 20;

        private int nextStartTime = -1;

        @Override
        public void start() {
            super.start();
            if (FrostologerEntity.this.isOnFire()) {
                FrostologerEntity.this.extinguish();
                FrostologerEntity.this.playExtinguishSound();
            }
        }

        @Override
        public boolean canStart() {
            if (FrostologerEntity.this.age <= nextStartTime) {
                return false;
            } else if (FrostologerEntity.this.random.nextInt(2) == 0) {
                return false;
            } else if (!super.canStart()) {
                return false;
            } else if (!FrostologerEntity.this.isTargetRooted()) {
                return false;
            } else {
                int numNearbyMinions = getWorld().getTargets(
                        BiterEntity.class,
                        IS_MINION,
                        FrostologerEntity.this,
                        FrostologerEntity.this.getBoundingBox().expand(16.0)
                ).size();

                return FrostologerEntity.this.random.nextInt(8) + 1 > numNearbyMinions;
            }
        }

        @Override
        protected void castSpell() {
            ServerWorld serverWorld = (ServerWorld) getWorld();
            nextStartTime = FrostologerEntity.this.age + SUMMON_BITER_COOL_DOWN;

            for (int i = 0; i < 3; ++i) {
                BlockPos blockPos = FrostologerEntity.this.getBlockPos();

                // use vex entity as placeholder for custom minions
                BiterEntity minionEntity = FEntityTypes.BITER.create(serverWorld);

                if (minionEntity == null) {
                    return;
                }

                minionEntity.refreshPositionAndAngles(blockPos, 0.0F, 0.0F);

                minionEntity.initialize(
                        serverWorld,
                        serverWorld.getLocalDifficulty(blockPos),
                        SpawnReason.MOB_SUMMONED,
                        null
                );
                minionEntity.setOwner(FrostologerEntity.this);

                serverWorld.spawnEntityAndPassengers(minionEntity);
            }
        }

        @Override
        protected int getSpellTicks() {
            return 20;
        }

        @Override
        protected int startTimeDelay() {
            return 20;
        }

        @Nullable
        @Override
        protected SoundEvent getSoundPrepare() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected Spell getSpell() {
            return Spell.SUMMON_VEX;
        }
    }

    protected class IcicleAttackGoal extends SpellcastingIllagerEntity.CastSpellGoal {

        private final IntProvider numIciclesProvider;

        private static final int ICICLE_ATTACK_COOL_DOWN = 20;

        private int nextStartTime = -1;

        public IcicleAttackGoal(IntProvider numIciclesProvider) {
            this.numIciclesProvider = numIciclesProvider;
        }

        @Override
        public void start() {
            super.start();
            if (FrostologerEntity.this.isOnFire()) {
                FrostologerEntity.this.extinguish();
                FrostologerEntity.this.playExtinguishSound();
            }
        }

        @Override
        public boolean canStart() {
            if (FrostologerEntity.this.age <= nextStartTime) {
                return false;
            } else if (FrostologerEntity.this.random.nextInt(2) == 0) {
                return false;
            } else if (!super.canStart()) {
                return false;
            } else {
                return FrostologerEntity.this.isTargetRooted();
            }
        }

        @Override
        protected void castSpell() {
            ServerWorld serverWorld = (ServerWorld) getWorld();

            int numIcicles = this.numIciclesProvider.get(random);
            nextStartTime = FrostologerEntity.this.age + ICICLE_ATTACK_COOL_DOWN;
            for (int i = 0; i < numIcicles; ++i) {
                BlockPos blockPos = FrostologerEntity.this.getBlockPos()
                        .add(
                                -2 + FrostologerEntity.this.random.nextInt(5),
                                2,
                                -2 + FrostologerEntity.this.random.nextInt(5)
                        );

                ThrownIcicleEntity icicle = FEntityTypes.THROWN_ICICLE.create(serverWorld);

                if (icicle == null) {
                    return;
                }

                icicle.refreshPositionAndAngles(blockPos, 0.0F, 0.0F);
                icicle.setOwner(FrostologerEntity.this);

                icicle.setVelocity(
                        FrostologerEntity.this,
                        FrostologerEntity.this.getPitch() + FrostologerEntity.this.random.nextFloat(),
                        FrostologerEntity.this.getHeadYaw() + FrostologerEntity.this.random.nextFloat(),
                        0.0f, 3.0f, 1.0f
                );

                serverWorld.spawnEntityAndPassengers(icicle);
            }
        }

        @Override
        protected int getSpellTicks() {
            return 20;
        }

        @Override
        protected int startTimeDelay() {
            return 20;
        }

        @Nullable
        @Override
        protected SoundEvent getSoundPrepare() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected Spell getSpell() {
            return Spell.SUMMON_VEX;
        }
    }
}
