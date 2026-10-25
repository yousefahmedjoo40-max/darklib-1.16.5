package com.darklib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Darklib.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    /** Smoothed "how much sky can the player see" (0 = cave/indoors, 1 = open sky). */
    private static float skyExposure = 1.0F;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        BlockPos pos = mc.player.blockPosition();
        float target = mc.level.getBrightness(LightType.SKY, pos) / 15.0F;
        skyExposure += (target - skyExposure) * 0.05F;
    }

    private static float getFactor() {
        if (!DarklibConfig.ENABLED.get()) return 0.0F;
        Minecraft mc = Minecraft.getInstance();
        ClientWorld world = mc.level;
        if (world == null || mc.player == null) return 0.0F;

        float f = DarklibAPI.getNightFactor(world) * skyExposure * (1.0F - DarklibAPI.getDarknessReduction());
        if (mc.player.hasEffect(Effects.NIGHT_VISION)) {
            f *= DarklibConfig.NIGHT_VISION_MULTIPLIER.get().floatValue();
        }
        return f;
    }

    @SubscribeEvent
    public static void onFogColor(EntityViewRenderEvent.FogColors event) {
        float f = getFactor();
        if (f <= 0.001F) return;
        float keep = 1.0F - f;
        event.setRed(event.getRed() * keep);
        event.setGreen(event.getGreen() * keep);
        event.setBlue(event.getBlue() * keep);
    }

    @SubscribeEvent
    public static void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        float f = getFactor();
        if (f <= 0.001F) return;
        // Don't touch water / lava fog
        if (!event.getInfo().getFluidInCamera().isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        float renderBlocks = Math.max(2, mc.options.renderDistance) * 16.0F;
        float base = 2.5F / renderBlocks;                       // approx. vanilla fog
        float max = DarklibConfig.NIGHT_FOG_DENSITY.get().floatValue();
        float density = base + (Math.max(max, base) - base) * f;

        event.setDensity(density);
        event.setCanceled(true);                                // required for Forge to apply it
    }
}
