package com.github.thedeathlycow.frostiful.config.group;

import com.github.thedeathlycow.frostiful.Frostiful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.math.MathHelper;

@Config(name = Frostiful.MODID + ".combat_config")
public class CombatConfigGroup implements ConfigData {


    boolean doChillagerPatrols = true;

    boolean straysCarryFrostArrows = true;

    double maxFrostSpellDistance = 25;

    int frostWandCooldown = 120;

    int frostWandRootTime = 100;

    int frostologerHeatDrainPerTick = 30;

    int frostologerCoolingFromFrostWandHit = 6300 / 6;

    int packedSnowballFreezeAmount = 500;

    float packedSnowballDamage = 2.0f;

    float packedSnowballVulnerableTypesDamage = 5.0f;

    int biterFrostBiteMaxAmplifier = 2;

    float chillagerFireDamageMultiplier = 1.5f;

    @ConfigEntry.Gui.RequiresRestart
    float furUpgradeTemplateGenerateChance = 0.5f;

    @ConfigEntry.Gui.RequiresRestart
    float skateUpgradeTemplateGenerateChance = 0.33f;

    @ConfigEntry.Gui.RequiresRestart
    double veryProtectiveFrostResistanceMultiplier = 1.0;

    @ConfigEntry.Gui.RequiresRestart
    double protectiveFrostResistanceMultiplier = 0.5;


    public boolean doChillagerPatrols() {
        return doChillagerPatrols;
    }

    public boolean straysCarryFrostArrows() {
        return straysCarryFrostArrows;
    }

    public double getMaxFrostSpellDistance() {
        return maxFrostSpellDistance;
    }

    public int getFrostWandCooldown() {
        return frostWandCooldown;
    }

    public int getFrostWandRootTime() {
        return frostWandRootTime;
    }

    public int getFrostologerHeatDrainPerTick() {
        // multiply by 2 as goals only twice at half the rate of normal
        return 2 * frostologerHeatDrainPerTick;
    }

    public int getFrostologerCoolingFromFrostWandHit() {
        return frostologerCoolingFromFrostWandHit;
    }

    public int getPackedSnowballFreezeAmount() {
        return packedSnowballFreezeAmount;
    }

    public float getPackedSnowballDamage() {
        return packedSnowballDamage;
    }

    public float getPackedSnowballVulnerableTypesDamage() {
        return packedSnowballVulnerableTypesDamage;
    }

    public int getBiterFrostBiteMaxAmplifier() {
        return Math.max(0, this.biterFrostBiteMaxAmplifier);
    }

    public float getChillagerFireDamageMultiplier() {
        return chillagerFireDamageMultiplier;
    }

    public float getFurUpgradeTemplateGenerateChance() {
        return MathHelper.clamp(furUpgradeTemplateGenerateChance, 0f, 1f);
    }

    public float getSkateUpgradeTemplateGenerateChance() {
        return MathHelper.clamp(skateUpgradeTemplateGenerateChance, 0f, 1f);
    }

    public double getVeryProtectiveFrostResistanceMultiplier() {
        return veryProtectiveFrostResistanceMultiplier;
    }

    public double getProtectiveFrostResistanceMultiplier() {
        return protectiveFrostResistanceMultiplier;
    }

    public double getVeryHarmfulFrostResistanceMultiplier() {
        return -veryProtectiveFrostResistanceMultiplier;
    }

    public double getHarmfulFrostResistanceMultiplier() {
        return -protectiveFrostResistanceMultiplier;
    }
}
