package io.github.sefiraat.crystamaehistoria.managers;

import io.github.sefiraat.crystamaehistoria.CrystamaeHistoria;
import io.github.sefiraat.crystamaehistoria.runnables.ParticleDisplayRunnable;
import io.github.sefiraat.crystamaehistoria.runnables.SaveConfigRunnable;
import io.github.sefiraat.crystamaehistoria.runnables.TemporaryEffectsRunnable;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;

public class RunnableManager {

    @Getter
    private final TemporaryEffectsRunnable temporaryEffectsRunnable;
    @Getter
    private final SaveConfigRunnable saveConfigRunnable;
    @Getter
    private final ParticleDisplayRunnable particleDisplayRunnable;

    private final ScheduledTask temporaryEffectsTask;
    private final ScheduledTask saveConfigTask;
    private final ScheduledTask particleDisplayTask;

    public RunnableManager() {
        final CrystamaeHistoria plugin = CrystamaeHistoria.getInstance();

        this.temporaryEffectsRunnable = new TemporaryEffectsRunnable();
        this.saveConfigRunnable = new SaveConfigRunnable();
        this.particleDisplayRunnable = new ParticleDisplayRunnable();

        this.temporaryEffectsTask = plugin.getServer().getGlobalRegionScheduler()
            .runAtFixedRate(plugin, task -> temporaryEffectsRunnable.run(), 1L, 20L);
        this.saveConfigTask = plugin.getServer().getGlobalRegionScheduler()
            .runAtFixedRate(plugin, task -> saveConfigRunnable.run(), 1L, 12000L);
        this.particleDisplayTask = plugin.getServer().getGlobalRegionScheduler()
            .runAtFixedRate(plugin, task -> particleDisplayRunnable.run(), 1L, 80L);
    }

    public void shutdown() {
        temporaryEffectsTask.cancel();
        saveConfigTask.cancel();
        particleDisplayTask.cancel();
    }
}
