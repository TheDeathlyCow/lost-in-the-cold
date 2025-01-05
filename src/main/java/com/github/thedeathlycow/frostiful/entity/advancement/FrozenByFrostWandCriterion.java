package com.github.thedeathlycow.frostiful.entity.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public class FrozenByFrostWandCriterion extends AbstractCriterion<FrozenByFrostWandCriterion.Conditions> {

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, Collection<LivingEntity> frozenEntities) {
        List<LootContext> victimContexts = new ArrayList<>(frozenEntities.size());

        for (LivingEntity frozenEntity : frozenEntities) {
            victimContexts.add(EntityPredicate.createAdvancementEntityLootContext(player, frozenEntity));
        }

        this.trigger(player, conditions -> conditions.matches(victimContexts));
    }

    public record Conditions(
            Optional<LootContextPredicate> player,
            List<LootContextPredicate> victims,
            NumberRange.IntRange entitiesFrozen
    ) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC
                                        .optionalFieldOf("player")
                                        .forGetter(Conditions::player),
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC
                                        .listOf()
                                        .optionalFieldOf("victims", List.of())
                                        .forGetter(Conditions::victims),
                                NumberRange.IntRange.CODEC
                                        .optionalFieldOf("entities_frozen", NumberRange.IntRange.ANY)
                                        .forGetter(Conditions::entitiesFrozen)
                        )
                        .apply(instance, Conditions::new)
        );

        /**
         *
         * Implementation: Finds the first victim that matches each predicate. If the predicate matches no victims,
         * returns false. Otherwise, checks the entities frozen count
         *
         * @param victims victims frozen
         * @return returns true
         */
        public boolean matches(Collection<LootContext> victims) {
            if (!this.victims.isEmpty()) {
                List<LootContext> unmatchedVictims = new ArrayList<>(victims);

                for (LootContextPredicate predicate : this.victims) {
                    boolean matched = false;

                    Iterator<LootContext> iterator = unmatchedVictims.iterator();
                    while (iterator.hasNext()) {
                        LootContext lootContext = iterator.next();
                        if (predicate.test(lootContext)) {
                            iterator.remove();
                            matched = true;
                            break;
                        }
                    }

                    if (!matched) {
                        return false;
                    }
                }
            }
            return this.entitiesFrozen.test(victims.size());
        }

        @Override
        public void validate(LootContextPredicateValidator validator) {
            AbstractCriterion.Conditions.super.validate(validator);
            validator.validateEntityPredicates(this.victims, ".victims");
        }
    }
}