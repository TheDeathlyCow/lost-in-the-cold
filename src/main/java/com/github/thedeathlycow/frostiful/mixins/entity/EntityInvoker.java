package com.github.thedeathlycow.frostiful.mixins.entity;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityInvoker {

    @Invoker("isBeingRainedOn")
    boolean frostiful$invokeIsBeingRainedOn();

    @Invoker("isInsideBubbleColumn")
    boolean frostiful$invokeIsInsideBubbleColumn();

    @Invoker("playExtinguishSound")
    void frostiful$invokePlayExtinguishSound();

}
