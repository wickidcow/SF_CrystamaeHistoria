package io.github.sefiraat.crystamaehistoria.runnables.spells;

import io.github.sefiraat.crystamaehistoria.CrystamaeHistoria;
import io.github.sefiraat.crystamaehistoria.magic.CastInformation;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;

public class SpellTickRunnable implements Runnable {

    private final CastInformation castInformation;
    private int numberOfRuns;
    private ScheduledTask task;

    @ParametersAreNonnullByDefault
    public SpellTickRunnable(CastInformation castInformation, int numberOfRuns) {
        this.castInformation = castInformation;
        this.numberOfRuns = numberOfRuns;
    }

    public void start(long period) {
        final Player caster = castInformation.getCasterAsPlayer();
        if (caster == null) {
            retire();
            return;
        }

        task = caster.getScheduler().runAtFixedRate(
            CrystamaeHistoria.getInstance(),
            scheduledTask -> run(),
            this::retire,
            1L,
            Math.max(1L, period)
        );

        if (task == null) {
            retire();
        }
    }

    @Override
    public void run() {
        if (numberOfRuns <= 0) {
            castInformation.runAfterTicksEvent();
            cancel();
        } else {
            castInformation.runTickEvent();
            numberOfRuns--;
        }
    }

    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        retire();
    }

    private void retire() {
        CrystamaeHistoria.getSpellMemory().getTickingCastables().remove(this);
    }
}
