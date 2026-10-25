package com.darklib;

import net.minecraftforge.common.ForgeConfigSpec;

public class DarklibConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue ENABLED;
    public static final ForgeConfigSpec.DoubleValue NIGHT_FOG_DENSITY;
    public static final ForgeConfigSpec.DoubleValue NIGHT_VISION_MULTIPLIER;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.push("darkness");
        ENABLED = b.comment("Enable the heavy night darkness").define("enabled", true);
        NIGHT_FOG_DENSITY = b.comment("Fog density at full night (higher = darker/shorter view). 0.09 is scary, 0.2 is almost blind.")
                .defineInRange("nightFogDensity", 0.09D, 0.01D, 0.5D);
        NIGHT_VISION_MULTIPLIER = b.comment("How much the Night Vision effect reduces the darkness (0 = removes it completely, 1 = no effect)")
                .defineInRange("nightVisionMultiplier", 0.15D, 0.0D, 1.0D);
        b.pop();
        SPEC = b.build();
    }
}
