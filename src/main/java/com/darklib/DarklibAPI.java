package com.darklib;

import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * Public API for mods that depend on Darklib.
 */
public final class DarklibAPI {
    private static final List<Supplier<Float>> REDUCERS = new CopyOnWriteArrayList<>();

    private DarklibAPI() {}

    /**
     * @return 0.0 during the day, 1.0 at deep night, smooth in the dusk/dawn transitions.
     * Always 0 outside the Overworld.
     */
    public static float getNightFactor(World world) {
        if (world == null || world.dimension() != World.OVERWORLD) return 0.0F;
        long t = world.getDayTime() % 24000L;
        if (t < 12000L) return 0.0F;                                  // day
        if (t < 13500L) return smooth((t - 12000L) / 1500.0F);        // dusk
        if (t < 22500L) return 1.0F;                                  // deep night
        return smooth(1.0F - (t - 22500L) / 1500.0F);                 // dawn
    }

    /** @return true if it is dark enough to be considered night. */
    public static boolean isNight(World world) {
        return getNightFactor(world) > 0.5F;
    }

    /**
     * CLIENT side: register a source that pushes the darkness back (any other mod can use this).
     * The supplier returns 0.0 (no effect) to 1.0 (removes all darkness).
     */
    public static void registerDarknessReducer(Supplier<Float> reducer) {
        if (reducer != null) REDUCERS.add(reducer);
    }

    /** @return combined darkness reduction of all registered sources, 0..1. */
    public static float getDarknessReduction() {
        float keep = 1.0F;
        for (Supplier<Float> r : REDUCERS) {
            Float v = r.get();
            if (v == null) continue;
            keep *= 1.0F - Math.max(0.0F, Math.min(1.0F, v));
        }
        return 1.0F - keep;
    }

    private static float smooth(float x) {
        x = Math.max(0.0F, Math.min(1.0F, x));
        return x * x * (3.0F - 2.0F * x);
    }
}
