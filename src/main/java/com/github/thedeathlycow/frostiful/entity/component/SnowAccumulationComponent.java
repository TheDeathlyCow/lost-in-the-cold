package com.github.thedeathlycow.frostiful.entity.component;

import com.github.thedeathlycow.frostiful.mixins.entity.EntityInvoker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class SnowAccumulationComponent implements Component, ServerTickingComponent {

    private static final String KEY = "snow_accumulation";

    private final LivingEntity provider;

    private int snowAccumulation = 0;

    public SnowAccumulationComponent(LivingEntity provider) {
        this.provider = provider;
    }

    @Override
    public void serverTick() {
        if (((EntityInvoker)this.provider).frostiful$invokeIsBeingRainedOn()) {

        }
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (snowAccumulation > 0) {
            tag.putInt(KEY, snowAccumulation);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.snowAccumulation = tag.contains(KEY, NbtElement.INT_TYPE) ? tag.getInt(KEY) : 0;
    }
}