package com.github.thedeathlycow.frostiful.entity.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class SunLichenDischargeCriterion extends AbstractCriterion<SunLichenDischargeCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, int temperatureImparted) {
        this.trigger(player, conditions -> conditions.temperatureImparted.test(temperatureImparted));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(
            Optional<LootContextPredicate> player,
            NumberRange.IntRange temperatureImparted
    ) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC
                                .optionalFieldOf("player")
                                .forGetter(Conditions::player),
                        NumberRange.IntRange.CODEC
                                .fieldOf("temperature_imparted")
                                .orElse(NumberRange.IntRange.ANY)
                                .forGetter(Conditions::temperatureImparted)
                ).apply(instance, Conditions::new)
        );
    }
}