package com.github.thedeathlycow.frostiful.item;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BannerPatterns;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Unit;

import static net.minecraft.server.command.CommandManager.literal;

public final class FrostedBanner {

    private static final Text FROSTED_BANNER_NAME = Text.translatable("block.frostiful.frosted_banner")
            .formatted(Formatting.DARK_PURPLE);


    @SuppressWarnings("deprecation")
    public static ItemStack createItem(RegistryEntryLookup<BannerPattern> lookup) {
        ItemStack stack = new ItemStack(Items.WHITE_BANNER);

        BannerPatternsComponent bannerPatterns = new BannerPatternsComponent.Builder()
                .add(lookup, BannerPatterns.RHOMBUS, DyeColor.PURPLE)
                .add(lookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.LIGHT_GRAY)
                .add(lookup, BannerPatterns.STRIPE_CENTER, DyeColor.GRAY)
                .add(lookup, BannerPatterns.BORDER, DyeColor.LIGHT_GRAY)
                .add(lookup, BannerPatterns.STRIPE_MIDDLE, DyeColor.BLACK)
                .add(lookup, BannerPatterns.CIRCLE, DyeColor.LIGHT_GRAY)
                .add(lookup, BannerPatterns.GUSTER, DyeColor.CYAN)
                .add(lookup, BannerPatterns.HALF_HORIZONTAL, DyeColor.LIGHT_GRAY)
                .add(lookup, BannerPatterns.BORDER, DyeColor.BLACK)
                .build();
        stack.set(DataComponentTypes.BANNER_PATTERNS, bannerPatterns);
        stack.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        stack.set(DataComponentTypes.ITEM_NAME, FROSTED_BANNER_NAME);

        return stack;
    }

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("givefrostedbanner")
                        .executes(
                                context -> {
                                    Entity as = context.getSource().getEntity();

                                    if (as instanceof ServerPlayerEntity player) {
                                        RegistryEntryLookup<BannerPattern> lookup = context.getSource()
                                                .getServer()
                                                .getRegistryManager()
                                                .getWrapperOrThrow(RegistryKeys.BANNER_PATTERN);
                                        ItemStack stack = FrostedBanner.createItem(lookup);
                                        player.getInventory().insertStack(stack);
                                    }

                                    return 0;
                                }
                        )
        );
    }


    private FrostedBanner() {

    }
}