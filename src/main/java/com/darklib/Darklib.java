package com.darklib;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Darklib.MOD_ID)
public class Darklib {
    public static final String MOD_ID = "darklib";

    public Darklib() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DarklibConfig.SPEC);
    }
}
