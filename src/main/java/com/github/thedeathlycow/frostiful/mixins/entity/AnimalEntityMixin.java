package com.github.thedeathlycow.frostiful.mixins.entity;

import com.github.thedeathlycow.frostiful.entity.component.BrushableComponent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends MobEntity {
    protected AnimalEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Inject into MobEntity as some mobs (such as Polar Bears) lack a definition of {@link MobEntity#interactMob(PlayerEntity, Hand)}
     */
    @ModifyReturnValue(
            method = "interactMob",
            at = @At("TAIL")
    )
    private ActionResult postInteract(ActionResult original, PlayerEntity player, Hand hand) {
        AnimalEntity animal = (AnimalEntity) (Object) this;
        return BrushableComponent.interactWithMob(animal, player, hand, original);
    }
}