package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.registry.tag.FItemTags;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import java.util.EnumMap;

public final class FArmorMaterials {

    public static final ArmorMaterial FUR = new ArmorMaterial(
            5,
            Util.make(new EnumMap<>(EquipmentType.class), map -> {
                map.put(EquipmentType.BOOTS, 1);
                map.put(EquipmentType.LEGGINGS, 2);
                map.put(EquipmentType.CHESTPLATE, 3);
                map.put(EquipmentType.HELMET, 1);
            }),
            15,
            FSoundEvents.ITEM_ARMOR_EQUIP_FUR,
            0f, 0f,
            FItemTags.FUR,
            Frostiful.id("fur")
    );

    public static final ArmorMaterial FUR_LINED_CHAINMAIL = new ArmorMaterial(
            15,
            Util.make(new EnumMap<>(EquipmentType.class), map -> {
                map.put(EquipmentType.BOOTS, 2);
                map.put(EquipmentType.LEGGINGS, 5);
                map.put(EquipmentType.CHESTPLATE, 6);
                map.put(EquipmentType.HELMET, 2);
            }),
            12,
            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
            0,
            0,
            ItemTags.REPAIRS_CHAIN_ARMOR,
            Frostiful.id("fur_lined_chainmail")
    );


    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful armor materials");
    }

    private FArmorMaterials() {

    }
}
