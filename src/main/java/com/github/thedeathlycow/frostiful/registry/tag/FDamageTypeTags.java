package com.github.thedeathlycow.frostiful.registry.tag;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class FDamageTypeTags {

    public static final TagKey<DamageType> IS_ICICLE = register("is_icicle");
    public static final TagKey<DamageType> DOES_NOT_BREAK_ROOT = register("does_not_break_root");

    private static TagKey<DamageType> register(String id) {
        return TagKey.of(RegistryKeys.DAMAGE_TYPE, Frostiful.id(id));
    }

    private FDamageTypeTags() {

    }
}
