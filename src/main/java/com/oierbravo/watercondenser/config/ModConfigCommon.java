package com.oierbravo.watercondenser.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfigCommon {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<String> CONDENSER_FLUID;
    public static final String CONDENSER_FLUID_DEFAULT = "minecraft:water";
    public static final ModConfigSpec.ConfigValue<Integer> CONDENSER_CAPACITY;
    public static final ModConfigSpec.ConfigValue<Integer> CONDENSER_TICKS_PER_CYCLE;
    public static final ModConfigSpec.ConfigValue<Integer> CONDENSER_MB_PER_CYCLE;
    public static final ModConfigSpec.ConfigValue<Float> CONDENSER_MB_MULTI_MIN;
    public static final ModConfigSpec.ConfigValue<Float> CONDENSER_MB_MULTI_MAX;
    public static final ModConfigSpec.ConfigValue<Integer> CONDENSER_BOTTLE_MB_CONSUMPTION;

    static {
        BUILDER.push("Configs for WaterCondenser");

        CONDENSER_FLUID = BUILDER.comment("The fluid to generate. If not valid, will revert to minecraft:water. Existing worlds will retroactively change their fluid type on change (but keep the same amount).")
                .define("Output fluid", CONDENSER_FLUID_DEFAULT);
        CONDENSER_CAPACITY = BUILDER.comment("Tank capacity in mB")
                .define("Condenser capacity", 1000);
        CONDENSER_TICKS_PER_CYCLE = BUILDER.comment("The length of a fill cycle, in ticks")
                .define("Ticks between cycles", 1);
        CONDENSER_MB_PER_CYCLE = BUILDER.comment("How much mB to generate per fill cycle")
                .define("Fluid per cycle", 2);
        CONDENSER_MB_MULTI_MIN = BUILDER.comment("For random variance, the minimum multiplier for each fill cycle")
                .define("Fluid multiplier chance min", 0.0f);
        CONDENSER_MB_MULTI_MAX = BUILDER.comment("For random variance, the maximum multiplier for each fill cycle")
                .define("Fluid multiplier chance max", 1.0f);

        CONDENSER_BOTTLE_MB_CONSUMPTION = BUILDER.comment("Bottle consumption per bottle, in mB")
                .define("Fluid amount in mB", 250);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
