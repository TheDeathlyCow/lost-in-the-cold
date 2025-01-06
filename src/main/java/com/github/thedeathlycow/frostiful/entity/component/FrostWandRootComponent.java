package com.github.thedeathlycow.frostiful.entity.component;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.entity.damage.FDamageSources;
import com.github.thedeathlycow.frostiful.mixins.entity.EntityInvoker;
import com.github.thedeathlycow.frostiful.registry.FComponents;
import com.github.thedeathlycow.frostiful.registry.FEntityAttributes;
import com.github.thedeathlycow.frostiful.registry.tag.FDamageTypeTags;
import com.github.thedeathlycow.frostiful.registry.tag.FEntityTypeTags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class FrostWandRootComponent implements Component, AutoSyncedComponent, ServerTickingComponent {

    private static final String ROOTED_TICKS_KEY = "rooted_ticks";

    private final LivingEntity provider;

    private int rootedTicks;

    public FrostWandRootComponent(LivingEntity provider) {
        this.provider = provider;
    }

    public static void afterDamage(
            LivingEntity provider,
            DamageSource source,
            float baseDamageTaken, float damageTaken,
            boolean blocked
    ) {
        FrostWandRootComponent component = FComponents.FROST_WAND_ROOT_COMPONENT.get(provider);
        boolean breakRoot = !blocked
                && damageTaken > 0f
                && !source.isIn(FDamageTypeTags.DOES_NOT_BREAK_ROOT)
                && component.isRooted();

        if (breakRoot) {
            component.breakRoot(source.getAttacker());
        }
    }

    @Nullable
    public static Vec3d adjustMovementForRoot(MovementType type, Vec3d movement, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            FrostWandRootComponent component = FComponents.FROST_WAND_ROOT_COMPONENT.get(livingEntity);
            return component.adjustMovementForRoot(type, movement);
        }

        return null;
    }

    @Override
    public void serverTick() {
        if (provider.isSpectator()) {
            this.setRootedTicks(0);
        } else if (this.isRooted()) {
            this.setRootedTicks(this.getRootedTicks() - 1);

            if (provider.isOnFire()) {
                this.breakRoot(null);
                provider.extinguish();
                ((EntityInvoker) provider).frostiful$invokePlayExtinguishSound();
            }
        }
    }

    public float getRootProgress() {
        return (float) this.rootedTicks / Frostiful.getConfig().combatConfig.getFrostWandRootTime();
    }

    public void breakRoot(@Nullable Entity attacker) {
        if (this.isRooted() && provider.getWorld() instanceof ServerWorld serverWorld) {
            this.setRootedTicks(1); // set to 1 so the icebreaker enchantment can detect it
            spawnShatterParticlesAndSound(provider, serverWorld);
        }

        double damage = attacker instanceof LivingEntity livingAttacker
                ? livingAttacker.getAttributeValue(FEntityAttributes.ICE_BREAK_DAMAGE)
                : Frostiful.getConfig().combatConfig.getIceBreakFallbackDamage();

        DamageSource source = FDamageSources.getDamageSources(provider.getWorld())
                .frostiful$brokenIce(attacker);
        provider.damage(source, (float) damage);
    }

    public boolean tryRootFromFrostWand(@Nullable Entity originalCaster) {
        if (this.canBeRootedBy(originalCaster)) {
            this.setRootedTicks(Frostiful.getConfig().combatConfig.getFrostWandRootTime());
            return true;
        }
        return false;
    }

    @Override
    public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(this.rootedTicks);
    }

    @Override
    public void applySyncPacket(RegistryByteBuf buf) {
        this.rootedTicks = buf.readVarInt();
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.rootedTicks = tag.contains(ROOTED_TICKS_KEY, NbtElement.INT_TYPE)
                ? tag.getInt(ROOTED_TICKS_KEY)
                : 0;
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.rootedTicks != 0) {
            tag.putInt(ROOTED_TICKS_KEY, this.rootedTicks);
        }
    }

    public boolean isRooted() {
        return this.getRootedTicks() > 0;
    }

    public int getRootedTicks() {
        return rootedTicks;
    }

    public void setRootedTicks(int rootedTicks) {
        if (this.rootedTicks != rootedTicks) {
            this.rootedTicks = rootedTicks;
            FComponents.FROST_WAND_ROOT_COMPONENT.sync(this.provider);
        }
    }

    private boolean canBeRootedBy(@Nullable Entity originalCaster) {
        if (this.isRooted()) {
            return false;
        }

        if (provider.getType().isIn(FEntityTypeTags.ROOT_IMMUNE)) {
            return false;
        }

        if (originalCaster != null && provider.isTeammate(originalCaster)) {
            return false;
        }

        return provider.thermoo$canFreeze();
    }

    @Nullable
    private Vec3d adjustMovementForRoot(MovementType type, Vec3d movement) {
        if (!this.isRooted()) {
            return null;
        }

        return switch (type) {
            case SELF, PLAYER -> Vec3d.ZERO.add(0, movement.y < 0 && !provider.hasNoGravity() ? movement.y : 0, 0);
            default -> null;
        };
    }

    private static void spawnShatterParticlesAndSound(LivingEntity victim, ServerWorld serverWorld) {
        ParticleEffect shatteredIce = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.BLUE_ICE.getDefaultState());

        serverWorld.spawnParticles(
                shatteredIce,
                victim.getX(), victim.getY(), victim.getZ(),
                500,
                0.5, 1.0, 0.5,
                1.0
        );

        victim.getWorld().playSound(
                null,
                victim.getBlockPos(),
                SoundEvents.BLOCK_GLASS_BREAK,
                SoundCategory.AMBIENT,
                1.0f, 0.75f
        );
    }
}