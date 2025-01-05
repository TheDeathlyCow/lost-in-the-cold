package com.github.thedeathlycow.frostiful.mixins.entity.brushing;

import com.github.thedeathlycow.frostiful.entity.FBrushable;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public abstract class MobBrushingMixin extends LivingEntity {

    protected MobBrushingMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Inject into MobEntity as some mobs (such as Polar Bears) lack a definition of {@link MobEntity#interactMob(PlayerEntity, Hand)}
     */
    @ModifyReturnValue(
            method = "interactMob",
            at = @At("TAIL")
    )
    private ActionResult brushPolarBear(ActionResult original, PlayerEntity player, Hand hand) {
        MobEntity animal = (MobEntity) (Object) this;
        return FBrushable.interact(animal, player, hand, original);
    }
}
