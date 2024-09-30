package com.oierbravo.watercondenser.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfig {
    public static void register(ModContainer modContainer) {
        registerCommonConfigs(modContainer);
    }
    private static void registerCommonConfigs(ModContainer modContainer) {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
        WaterCondenserConfig.registerCommonConfig(COMMON_BUILDER);
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }
}

