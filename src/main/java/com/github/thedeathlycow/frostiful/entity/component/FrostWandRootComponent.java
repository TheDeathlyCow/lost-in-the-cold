package com.github.thedeathlycow.frostiful.entity.component;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.entity.RootedEntity;
import com.github.thedeathlycow.frostiful.mixins.entity.EntityInvoker;
import com.github.thedeathlycow.frostiful.registry.FComponents;
import com.github.thedeathlycow.frostiful.registry.tag.FDamageTypeTags;
import com.github.thedeathlycow.frostiful.registry.tag.FEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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
            LivingEntity entity,
            DamageSource source,
            float baseDamageTaken, float damageTaken,
            boolean blocked
    ) {
        boolean breakRoot = !blocked
                && damageTaken > 0f
                && !source.isIn(FDamageTypeTags.DOES_NOT_BREAK_ROOT)
                && entity instanceof RootedEntity rooted
                && rooted.frostiful$isRooted();

        if (breakRoot) {
            FComponents.FROST_WAND_ROOT_COMPONENT.get(entity).breakRoot();
        }
    }

    @Nullable
    public Vec3d adjustMovementForRoot(MovementType type, Vec3d movement) {
        if (!this.isRooted()) {
            return null;
        }

        return switch (type) {
            case SELF, PLAYER -> Vec3d.ZERO.add(0, movement.y < 0 && !provider.hasNoGravity() ? movement.y : 0, 0);
            default -> null;
        };
    }

    @Override
    public void serverTick() {
        if (provider.isSpectator()) {
            this.setRootedTicks(0);
        } else if (this.isRooted()) {
            this.rootedTicks--;

            if (provider.isOnFire() && provider.getWorld() instanceof ServerWorld serverWorld) {
                this.breakRoot();
                provider.extinguish();
                serverWorld.spawnParticles(
                        ParticleTypes.POOF,
                        provider.getX(), provider.getY(), provider.getZ(),
                        100,
                        0.5, 0.5, 0.5,
                        0.1f
                );
                ((EntityInvoker) provider).frostiful$invokePlayExtinguishSound();
            }
        }
    }

    public float getRootProgress() {
        return (float) this.rootedTicks / Frostiful.getConfig().combatConfig.getFrostWandRootTime();
    }

    public void breakRoot() {
        this.setRootedTicks(1); // set to 1 so the icebreaker enchantment can detect it
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

    private void setRootedTicks(int rootedTicks) {
        this.rootedTicks = rootedTicks;
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
}