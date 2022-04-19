package com.github.thedeathlycow.lostinthecold.util.survival;

import com.github.thedeathlycow.datapack.config.config.Config;
import com.github.thedeathlycow.lostinthecold.attributes.LostInTheColdEntityAttributes;
import com.github.thedeathlycow.lostinthecold.config.ConfigKeys;
import com.github.thedeathlycow.lostinthecold.init.LostInTheCold;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class FrostHelper {

    public static void addFrozenTicks(LivingEntity entity, int amount) {

        final Config config = LostInTheCold.getConfig();
        double frostResistance = entity.getAttributeValue(LostInTheColdEntityAttributes.FROST_RESISTANCE);
        double frostModifier = frostResistance * config.get(ConfigKeys.PERCENT_FROST_REDUCTION_PER_FROST_RESISTANCE);
        frostModifier /= 100.0D;

        int toAdd = (int) ((1 - frostModifier) * amount);

        int current = entity.getFrozenTicks();
        setFrozenTicks(entity, current + toAdd);
    }

    public static void removeFrozenTicks(LivingEntity entity, int amount) {
        int current = entity.getFrozenTicks();
        setFrozenTicks(entity, current - amount);
    }

    public static void setFrozenTicks(Entity entity, int amount) {
        amount = MathHelper.clamp(amount, 0, entity.getMinFreezeDamageTicks());
        entity.setFrozenTicks(amount);
    }

}
