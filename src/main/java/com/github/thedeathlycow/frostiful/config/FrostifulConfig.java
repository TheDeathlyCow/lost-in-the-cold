package com.github.thedeathlycow.frostiful.config;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.group.*;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = Frostiful.MODID)
public class FrostifulConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Gui.CollapsibleObject
    public final ClientConfigGroup clientConfig = new ClientConfigGroup();

    @ConfigEntry.Gui.CollapsibleObject
    public final UpdateConfigGroup updateConfig = new UpdateConfigGroup();

    @ConfigEntry.Gui.CollapsibleObject
    public final EnvironmentConfigGroup environmentConfig = new EnvironmentConfigGroup();

    @ConfigEntry.Gui.CollapsibleObject
    public final CombatConfigGroup combatConfig = new CombatConfigGroup();

    @ConfigEntry.Gui.CollapsibleObject
    public final FreezingConfigGroup freezingConfig = new FreezingConfigGroup();

    @ConfigEntry.Gui.CollapsibleObject
    public final IcicleConfigGroup icicleConfig = new IcicleConfigGroup();

    public FrostifulConfig() {

    }

    public static void updateConfig(ConfigHolder<FrostifulConfig> configHolder) {
        UpdateConfigGroup config = configHolder.getConfig().updateConfig;

        if (config.isConfigUpdatesEnabled() && config.currentConfigVersion != Frostiful.CONFIG_VERSION) {
            config.currentConfigVersion = Frostiful.CONFIG_VERSION;
            configHolder.resetToDefault();
            configHolder.save();

            Frostiful.LOGGER.info("The Frostiful Config has been reset due to an update to the default values. " +
                    "You may disable these updates if you don't want this to happen.");
        }
    }
}
