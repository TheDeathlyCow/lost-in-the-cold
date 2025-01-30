package com.github.thedeathlycow.frostiful.config.group;

import com.github.thedeathlycow.frostiful.Frostiful;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Frostiful.MODID + ".environment_config")
public class EnvironmentConfigGroup implements ConfigData {

    boolean doDryBiomeNightFreezing = true;

    int nightTemperatureShift = -1;
    int coldBiomeTemperatureChange = -1;
    int freezingBiomeTemperatureChange = -3;

    int rainWetnessIncrease = 1;
    int touchingWaterWetnessIncrease = 5;
    int dryRate = 1;
    int onFireDryDate = 50;

    int onFireWarmRate = 50;

    int powderSnowFreezeRate = 30;

    int warmthPerLightLevel = 2;
    int minLightForWarmth = 5;

    int ultrawarmWarmRate = 15;

    int maxSnowAccumulationTicks = 100;

    boolean enableSeasonsIntegration = true;

    public boolean doDryBiomeNightFreezing() {
        return doDryBiomeNightFreezing;
    }

    public int getNightTemperatureShift() {
        return nightTemperatureShift;
    }

    public int getColdBiomeTemperatureChange() {
        return coldBiomeTemperatureChange;
    }

    public int getFreezingBiomeTemperatureChange() {
        return freezingBiomeTemperatureChange;
    }

    public int getRainWetnessIncrease() {
        return rainWetnessIncrease;
    }

    public int getTouchingWaterWetnessIncrease() {
        return touchingWaterWetnessIncrease;
    }

    public int getDryRate() {
        return dryRate;
    }

    public int getOnFireDryDate() {
        return onFireDryDate;
    }

    public int getOnFireWarmRate() {
        return onFireWarmRate;
    }

    public int getPowderSnowFreezeRate() {
        return powderSnowFreezeRate;
    }

    public int getWarmthPerLightLevel() {
        return warmthPerLightLevel;
    }

    public int getMinLightForWarmth() {
        return minLightForWarmth;
    }

    public int getUltrawarmWarmRate() {
        return ultrawarmWarmRate;
    }

    public int getMaxSnowAccumulationTicks() {
        return maxSnowAccumulationTicks;
    }

    public boolean enableSeasonsIntegration() {
        return enableSeasonsIntegration;
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        ConfigData.super.validatePostLoad();
        this.maxSnowAccumulationTicks = Math.max(0, this.maxSnowAccumulationTicks);
    }
}
