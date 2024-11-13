package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class FSoundEvents {

    public static final SoundEvent FIRE_LICHEN_DISCHARGE = register("block.frostiful.sun_lichen.discharge");
    public static final SoundEvent CAMPFIRE_HISS = register("block.frostiful.campfire.hiss");
    public static final SoundEvent ITEM_FROST_WAND_CAST_SPELL = register("item.frostiful.frost_wand.cast_spell");
    public static final SoundEvent ITEM_FROST_WAND_PREPARE_CAST = register("item.frostiful.frost_wand.prepare_cast");
    public static final SoundEvent ENTITY_FROST_SPELL_FREEZE = register("entity.frostiful.frost_spell.freeze");
    public static final SoundEvent ENTITY_THROWN_ICICLE_HIT = register("entity.frostiful.thrown_icicle.hit");
    public static final SoundEvent ENTITY_THROWN_ICICLE_THROW = register("entity.frostiful.thrown_icicle.throw");

    public static final SoundEvent BLOCK_PACKED_SNOW_BREAK = register("block.frostiful.packed_snow.break");
    public static final SoundEvent BLOCK_PACKED_SNOW_FALL = register("block.frostiful.packed_snow.fall");
    public static final SoundEvent BLOCK_PACKED_SNOW_HIT = register("block.frostiful.packed_snow.hit");
    public static final SoundEvent BLOCK_PACKED_SNOW_PLACE = register("block.frostiful.packed_snow.place");
    public static final SoundEvent BLOCK_PACKED_SNOW_STEP = register("block.frostiful.packed_snow.step");

    public static final SoundEvent ENTITY_FROSTOLOGER_CAST_SPELL = register("entity.frostiful.frostologer.cast_spell");
    public static final SoundEvent ENTITY_FROSTOLOGER_PREPARE_CAST_BLIZZARD = register("entity.frostiful.frostologer.prepare_cast.blizzard");
    public static final SoundEvent ENTITY_FROSTOLOGER_AMBIENT = register("entity.frostiful.frostologer.ambient");
    public static final SoundEvent ENTITY_FROSTOLOGER_CELEBRATE = register("entity.frostiful.frostologer.celebrate");
    public static final SoundEvent ENTITY_FROSTOLOGER_DEATH = register("entity.frostiful.frostologer.death");
    public static final SoundEvent ENTITY_FROSTOLOGER_HURT = register("entity.frostiful.frostologer.hurt");

    public static final SoundEvent ENTITY_CHILLAGER_AMBIENT = register("entity.frostiful.chillager.ambient");
    public static final SoundEvent ENTITY_CHILLAGER_CELEBRATE = register("entity.frostiful.chillager.celebrate");
    public static final SoundEvent ENTITY_CHILLAGER_DEATH = register("entity.frostiful.chillager.death");
    public static final SoundEvent ENTITY_CHILLAGER_HURT = register("entity.frostiful.chillager.hurt");

    public static final SoundEvent ENTITY_BITER_AMBIENT = register("entity.frostiful.biter.ambient");
    public static final SoundEvent ENTITY_BITER_DEATH = register("entity.frostiful.biter.death");
    public static final SoundEvent ENTITY_BITER_HURT = register("entity.frostiful.biter.hurt");
    public static final SoundEvent ENTITY_BITER_BITE = register("entity.frostiful.biter.bite");
    public static final SoundEvent ENTITY_BITER_BURP = register("entity.frostiful.biter.burp");

    public static final SoundEvent ENTITY_WIND_BLOW = register("entity.frostiful.wind.generic.blow");
    public static final SoundEvent ENTITY_WIND_HOWL = register("entity.frostiful.wind.generic.howl");
    public static final SoundEvent ENTITY_WIND_WOOSH = register("entity.frostiful.wind.generic.woosh");
    public static final SoundEvent ENTITY_FREEZING_WIND_BLOWOUT = register("entity.frostiful.freezing_wind.blowout");

    public static final SoundEvent ENTITY_GENERIC_ICE_SKATE_SKATE = register("entity.generic.ice_skate.skate");
    public static final SoundEvent ENTITY_GENERIC_ICE_SKATE_GLIDE = register("entity.generic.ice_skate.glide");
    public static final SoundEvent ENTITY_GENERIC_ICE_SKATE_STOP = register("entity.generic.ice_skate.stop");

    public static final RegistryEntry<SoundEvent> ITEM_ARMOR_EQUIP_FUR = registerReference("item.armor.equip_fur");

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful sound events");
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(String name) {
        Identifier id = Frostiful.id(name);
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    private static SoundEvent register(String name) {
        SoundEvent event = SoundEvent.of(Frostiful.id(name));
        Registry.register(Registries.SOUND_EVENT, event.getId(), event);
        return event;
    }

    private FSoundEvents() {

    }
}
