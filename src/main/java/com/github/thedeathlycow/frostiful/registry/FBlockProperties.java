package com.github.thedeathlycow.frostiful.registry;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;

public final class FBlockProperties {
    public static final int MAX_CRACKING = 3;
    public static final IntProperty CRACKING = IntProperty.of("cracking", 0, MAX_CRACKING);
    public static final BooleanProperty FROZEN = BooleanProperty.of("frozen");

    private FBlockProperties() {

    }
}