package com.github.thedeathlycow.frostiful.item;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.entity.ThrownIcicleEntity;
import com.github.thedeathlycow.frostiful.registry.FSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class IcicleItem extends BlockItem implements ProjectileItem {


    public IcicleItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        world.playSound(
                null,
                user.getX(), user.getY(), user.getZ(),
                FSoundEvents.ENTITY_THROWN_ICICLE_THROW, SoundCategory.NEUTRAL,
                0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
        );

        if (!world.isClient) {
            ThrownIcicleEntity icicleEntity = new ThrownIcicleEntity(world, user, itemStack.copyWithCount(1));

            icicleEntity.setVelocity(
                    user,
                    user.getPitch(), user.getYaw(),
                    0.0f, 1.0f, 1.0f
            );
            if (user.getAbilities().creativeMode) {
                icicleEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }

            world.spawnEntity(icicleEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrementUnlessCreative(1, user);
        user.getItemCooldownManager().set(this, Frostiful.getConfig().icicleConfig.getThrownIcicleCooldown());

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        ThrownIcicleEntity icicleEntity = new ThrownIcicleEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
        icicleEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return icicleEntity;
    }
}
