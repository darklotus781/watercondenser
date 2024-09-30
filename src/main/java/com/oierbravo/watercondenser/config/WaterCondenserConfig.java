package com.oierbravo.watercondenser.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Objects;

public class WaterCondenserConfig {

    public static ModConfigSpec.ConfigValue<String> CONDENSER_FLUID;
    public static final String CONDENSER_FLUID_DEFAULT = "minecraft:water";
    public static ModConfigSpec.ConfigValue<Integer> CONDENSER_CAPACITY;
    public static ModConfigSpec.ConfigValue<Integer> CONDENSER_TICKS_PER_CYCLE;
    public static ModConfigSpec.ConfigValue<Integer> CONDENSER_MB_PER_CYCLE;
    public static ModConfigSpec.ConfigValue<Float> CONDENSER_MB_MULTI_MIN;
    public static ModConfigSpec.ConfigValue<Float> CONDENSER_MB_MULTI_MAX;
    public static ModConfigSpec.ConfigValue<Integer> CONDENSER_BOTTLE_MB_CONSUMPTION;

    public static void registerCommonConfig(ModConfigSpec.Builder builder) {
        builder.push("Configs for WaterCondenser");

        CONDENSER_FLUID = builder.comment("The fluid to generate. If not valid, will revert to minecraft:water. Existing worlds will retroactively change their fluid type on change (but keep the same amount).")
                .define("Output fluid", CONDENSER_FLUID_DEFAULT,WaterCondenserConfig::validateFluidName);
        CONDENSER_CAPACITY = builder.comment("Tank capacity in mB")
                .define("Condenser capacity", 1000);
        CONDENSER_TICKS_PER_CYCLE = builder.comment("The length of a fill cycle, in ticks")
                .define("Ticks between cycles", 1);
        CONDENSER_MB_PER_CYCLE = builder.comment("How much mB to generate per fill cycle")
                .define("Fluid per cycle", 2);
        CONDENSER_MB_MULTI_MIN = builder.comment("For random variance, the minimum multiplier for each fill cycle")
                .define("Fluid multiplier chance min", 0.0f);
        CONDENSER_MB_MULTI_MAX = builder.comment("For random variance, the maximum multiplier for each fill cycle")
                .define("Fluid multiplier chance max", 1.0f);

        CONDENSER_BOTTLE_MB_CONSUMPTION = builder.comment("Bottle consumption per bottle, in mB")
                .define("Fluid amount in mB", 250);

        builder.pop();
    }

    private static boolean validateFluidName(final Object obj)
    {
        return obj instanceof final String fluidName && BuiltInRegistries.FLUID.containsKey(Objects.requireNonNull(ResourceLocation.tryParse(fluidName)));
    }
}
