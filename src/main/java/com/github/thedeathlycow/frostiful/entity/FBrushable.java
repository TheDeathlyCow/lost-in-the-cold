package com.github.thedeathlycow.frostiful.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;

public interface FBrushable {
    void frostiful$brush(PlayerEntity source, SoundCategory shearedSoundCategory);

    boolean frostiful$isBrushable();

    boolean frostiful$wasBrushed();
}
