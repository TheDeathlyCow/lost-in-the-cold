package com.github.thedeathlycow.frostiful.entity.component;

import com.github.thedeathlycow.frostiful.registry.FComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class LivingEntityComponents implements Component, AutoSyncedComponent {
    private byte skateFlags;

    private final LivingEntity provider;

    public LivingEntityComponents(LivingEntity provider) {
        this.provider = provider;
    }

    public byte getSkateFlags() {
        return skateFlags;
    }

    public void setSkateFlags(byte skateFlags) {
        if (this.skateFlags != skateFlags) {
            this.skateFlags = skateFlags;
            FComponents.ENTITY_COMPONENTS.sync(this.provider);
        }
    }

    @Override
    public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeByte(this.skateFlags);
    }

    @Override
    public void applySyncPacket(RegistryByteBuf buf) {
        this.skateFlags = buf.readByte();
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        // nothing to save
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        // nothing to save
    }
}
