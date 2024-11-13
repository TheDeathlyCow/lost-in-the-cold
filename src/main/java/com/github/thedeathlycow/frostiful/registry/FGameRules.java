package com.github.thedeathlycow.frostiful.registry;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.util.TextStyles;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;

/**
 * Custom game rules for Lost in the Cold
 */
public class FGameRules {

    /**
     * Mod's dedicated category for game rules
     */
    public static final CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(
            Frostiful.id("gamerule_category"),
            Text.translatable("gamerule.category." + Frostiful.MODID)
                    .setStyle(TextStyles.GAME_RULE_TITLE)
    );

    /**
     * Whether to apply passive freezing. When false, player freezing will revert
     * to vanilla, except for the increased frost resistance provided by their
     * frost resistance attribute.
     */
    public static final GameRules.Key<GameRules.BooleanRule> DO_PASSIVE_FREEZING =
            GameRuleRegistry.register(
                    Frostiful.MODID + ".doPassiveFreezing",
                    CATEGORY,
                    GameRuleFactory.createBooleanRule(true)
            );

    public static void initialize() {
        Frostiful.LOGGER.debug("Initialized Frostiful game rules");
    }

    private FGameRules() {

    }
}
