package io.github.mooy1.infinitylib.common;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import org.bukkit.plugin.Plugin;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.TimeUnit;

/**
 * Folia-safe compatibility replacement for the legacy InfinityLib scheduler.
 *
 * <p>Crystamae Historia still uses InfinityLib's AbstractAddon, which schedules
 * its internal Slimefun tick counter through this class. The upstream
 * implementation uses BukkitScheduler directly, which is not valid on Folia.
 * Keeping this API-compatible replacement lets the rest of InfinityLib remain
 * unchanged while routing generic tasks through Paper's supported schedulers.</p>
 */
@ParametersAreNonnullByDefault
public final class Scheduler {

    private static final long MILLIS_PER_TICK = 50L;

    private Scheduler() {
    }

    public static void run(Runnable runnable) {
        final Plugin plugin = AbstractAddon.instance();
        plugin.getServer().getGlobalRegionScheduler().execute(plugin, runnable);
    }

    public static void runAsync(Runnable runnable) {
        final Plugin plugin = AbstractAddon.instance();
        plugin.getServer().getAsyncScheduler().runNow(plugin, task -> runnable.run());
    }

    public static void run(int delayTicks, Runnable runnable) {
        final Plugin plugin = AbstractAddon.instance();
        plugin.getServer().getGlobalRegionScheduler()
            .runDelayed(plugin, task -> runnable.run(), Math.max(1L, delayTicks));
    }

    public static void runAsync(int delayTicks, Runnable runnable) {
        final Plugin plugin = AbstractAddon.instance();
        plugin.getServer().getAsyncScheduler().runDelayed(
            plugin,
            task -> runnable.run(),
            toMillis(delayTicks),
            TimeUnit.MILLISECONDS
        );
    }

    public static void repeat(int intervalTicks, Runnable runnable) {
        repeat(intervalTicks, 1, runnable);
    }

    public static void repeatAsync(int intervalTicks, Runnable runnable) {
        repeatAsync(intervalTicks, 1, runnable);
    }

    public static void repeat(int intervalTicks, int delayTicks, Runnable runnable) {
        final Plugin plugin = AbstractAddon.instance();
        plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(
            plugin,
            task -> runnable.run(),
            Math.max(1L, delayTicks),
            Math.max(1L, intervalTicks)
        );
    }

    public static void repeatAsync(int intervalTicks, int delayTicks, Runnable runnable) {
        final Plugin plugin = AbstractAddon.instance();
        plugin.getServer().getAsyncScheduler().runAtFixedRate(
            plugin,
            task -> runnable.run(),
            toMillis(delayTicks),
            toMillis(intervalTicks),
            TimeUnit.MILLISECONDS
        );
    }

    private static long toMillis(int ticks) {
        return Math.max(1L, ticks) * MILLIS_PER_TICK;
    }
}
