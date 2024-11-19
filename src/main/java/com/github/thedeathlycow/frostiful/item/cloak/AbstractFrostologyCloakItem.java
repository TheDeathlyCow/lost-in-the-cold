package com.github.thedeathlycow.frostiful.item.cloak;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.compat.FrostifulIntegrations;
import com.github.thedeathlycow.frostiful.registry.tag.FItemTags;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.function.Predicate;

public abstract class AbstractFrostologyCloakItem extends Item implements Equipment {

    public static final Identifier MODEL_TEXTURE_ID = Frostiful.id("textures/entity/frostology_cloak.png");

    public AbstractFrostologyCloakItem(Settings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    public static boolean isWearing(PlayerEntity player, Predicate<ItemStack> isCloak) {
        if (FrostifulIntegrations.isModLoaded(FrostifulIntegrations.TRINKETS_ID)) {
            boolean trinket = TrinketsApi.getTrinketComponent(player)
                    .map(trinketComponent -> trinketComponent.isEquipped(isCloak))
                    .orElse(false);

            if (trinket) {
                return true;
            }
        }

        ItemStack chestStack = player.getInventory()
                .getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
        return isCloak.test(chestStack);
    }

    public static EquipmentSlot getPreferredEquipmentSlot(LivingEntity entity, ItemStack stack) {
        return EquipmentSlot.CHEST;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isIn(FItemTags.FUR);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return this.equipAndSwap(this, world, user, hand);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.CHEST;
    }
}
