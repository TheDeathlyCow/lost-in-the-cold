package com.github.thedeathlycow.frostiful.item;

import com.github.thedeathlycow.frostiful.Frostiful;
import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

public class IceSkateUpgradeTemplate {

    /// Text ///
    private static final Text ICE_SKATE_UPGRADE_APPLIES_TO_TEXT = Text.translatable(
            Util.createTranslationKey(
                    "item",
                    Frostiful.id("smithing_template.ice_skate_upgrade.applies_to")
            )).formatted(FSmithingTemplateItem.DESCRIPTION_FORMATTING);
    private static final Text ICE_SKATE_UPGRADE_INGREDIENTS_TEXT = Text.translatable(
            Util.createTranslationKey(
                    "item",
                    Frostiful.id("smithing_template.ice_skate_upgrade.ingredients")
            )
    ).formatted(FSmithingTemplateItem.DESCRIPTION_FORMATTING);
    private static final Text ICE_SKATE_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey(
                    "item", Frostiful.id("smithing_template.ice_skate_upgrade.base_slot_description")
            )
    );
    private static final Text ICE_SKATE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey(
                    "item",
                    Frostiful.id("smithing_template.ice_skate_upgrade.additions_slot_description")
            )
    );

    private static final Identifier EMPTY_SLOT_SWORD_TEXTURE = Identifier.ofVanilla("item/empty_slot_sword");

    public static SmithingTemplateItem createItem(Item.Settings settings) {
        return new SmithingTemplateItem(
                ICE_SKATE_UPGRADE_APPLIES_TO_TEXT,
                ICE_SKATE_UPGRADE_INGREDIENTS_TEXT,
                ICE_SKATE_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT,
                ICE_SKATE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT,
                getIceSkateUpgradeEmptyBaseSlotTextures(),
                getIceSkateUpgradeEmptyAdditionsSlotTextures(),
                settings
        );
    }

    private static List<Identifier> getIceSkateUpgradeEmptyBaseSlotTextures() {
        return List.of(FSmithingTemplateItem.EMPTY_ARMOR_SLOT_BOOTS_TEXTURE);
    }

    private static List<Identifier> getIceSkateUpgradeEmptyAdditionsSlotTextures() {
        return List.of(EMPTY_SLOT_SWORD_TEXTURE);
    }

    private IceSkateUpgradeTemplate() {

    }

}
