package com.github.thedeathlycow.frostiful.block;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public enum BrittleIceState implements StringIdentifiable {

    UNBROKEN("unbroken", 0),
    SMALL_CRACKS("small_cracks", 5),
    MEDIUM_CRACKS("medium_cracks", 10),
    LARGE_CRACKS("large_cracks", 15);

    private final String name;

    private final int redstoneOutput;

    public static final EnumProperty<BrittleIceState> CRACKING = EnumProperty.of("cracking", BrittleIceState.class);

    private static final Map<BrittleIceState, BrittleIceState> NEXT_STAGE = Util.make(
            new EnumMap<>(BrittleIceState.class),
            map -> {
                map.put(UNBROKEN, SMALL_CRACKS);
                map.put(SMALL_CRACKS, MEDIUM_CRACKS);
                map.put(MEDIUM_CRACKS, LARGE_CRACKS);
            }
    );

    BrittleIceState(String name, int redstoneOutput) {
        this.name = name;
        this.redstoneOutput = redstoneOutput;
    }

    @Nullable
    public static BrittleIceState getNextState(BlockState blockState) {
        BrittleIceState currentState = blockState.get(CRACKING);
        return NEXT_STAGE.getOrDefault(currentState, null);
    }

    @Override
    public String asString() {
        return this.name;
    }

    public int getRedstoneOutput() {
        return redstoneOutput;
    }
}
