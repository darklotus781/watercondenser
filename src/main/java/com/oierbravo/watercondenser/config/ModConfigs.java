package com.oierbravo.watercondenser.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.oierbravo.watercondenser.WaterCondenser;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfigs {
    public static ModConfigSpec COMMON;

    public static void register(ModContainer modContainer) {
        registerCommonConfigs(modContainer);
    }
    private static void registerCommonConfigs(ModContainer modContainer) {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
        WaterCondenserConfig.registerCommonConfig(COMMON_BUILDER);
        COMMON = COMMON_BUILDER.build();

        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, COMMON);
        ModConfigs.loadConfig(COMMON, FMLPaths.CONFIGDIR.get().resolve(WaterCondenser.MODID + "-common.toml"));

    }
    public static void loadConfig(ModConfigSpec spec, java.nio.file.Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.correct(configData);
    }
}

