package com.github.thedeathlycow.frostiful.item;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.FrostifulConfig;
import com.github.thedeathlycow.frostiful.entity.FrostSpellEntity;
import com.github.thedeathlycow.frostiful.entity.RootedEntity;
import com.github.thedeathlycow.frostiful.registry.FItems;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class FrostWandItem extends Item {

    public FrostWandItem(Settings settings) {
        super(settings);
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(
                                BASE_ATTACK_DAMAGE_MODIFIER_ID,
                                5.0,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(
                                BASE_ATTACK_SPEED_MODIFIER_ID,
                                -2.9f,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }

    public static ToolComponent createToolComponent() {
        return new ToolComponent(List.of(), 1.0f, 2);
    }


    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(FItems.FROZEN_ROD);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        Entity attacker = damageSource.getAttacker();
        boolean resetCooldown = target instanceof RootedEntity rooted && rooted.frostiful$isRooted();
        if (attacker instanceof PlayerEntity player && resetCooldown) {
            player.getItemCooldownManager().set(this, 0);
        }

        return super.getBonusAttackDamage(target, baseAttackDamage, damageSource);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int useTime = this.getMaxUseTime(stack, user) - remainingUseTicks;
        if (useTime > 10 && !world.isClient) {
            fireFrostSpell(stack, world, user);
        }
    }

    public static void fireFrostSpell(ItemStack frostWandStack, World world, LivingEntity user) {
        FrostifulConfig config = Frostiful.getConfig();

        FrostSpellEntity spell = new FrostSpellEntity(
                world,
                user,
                Vec3d.ZERO,
                config.combatConfig.getMaxFrostSpellDistance()
        );

        spell.setVelocity(user, user.getPitch(), user.getHeadYaw(), 0.0f, 2.5f, 1.0f);

        world.spawnEntity(spell);

        spell.playSound(FSoundEvents.ITEM_FROST_WAND_CAST_SPELL, 1f, 1f);

        if (user instanceof PlayerEntity player) {
            frostWandStack.damage(1, player, LivingEntity.getSlotForHand(player.getActiveHand()));
            player.incrementStat(Stats.USED.getOrCreateStat(frostWandStack.getItem()));
            player.getItemCooldownManager().set(frostWandStack.getItem(), config.combatConfig.getFrostWandCooldown());
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        } else {
            if (!world.isClient) {
                world.playSound(
                        null,
                        user.getX(), user.getY(), user.getZ(),
                        FSoundEvents.ITEM_FROST_WAND_PREPARE_CAST,
                        SoundCategory.PLAYERS,
                        1.0f, 1.0f
                );
            }
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getHardness(world, pos) != 0.0f) {
            stack.damage(2, miner, LivingEntity.getSlotForHand(miner.getActiveHand()));
        }

        return true;
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

//    @Override
//    public boolean canBeEnchantedWith(ItemStack stack, Enchantment enchantment, EnchantingContext context) {
//
//        if (Registries.ENCHANTMENT.getEntry(enchantment).isIn(FEnchantmentTags.FROST_WAND_ENCHANTING_TABLE)) {
//            return true;
//        }
//
//        return super.canBeEnchantedWith(stack, enchantment, context);
//    }
}
