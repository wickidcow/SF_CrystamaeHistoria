package io.github.sefiraat.crystamaehistoria.runnables.spells;

import io.github.sefiraat.crystamaehistoria.CrystamaeHistoria;
import io.github.sefiraat.crystamaehistoria.utils.ArmourStandUtils;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.entity.ArmorStand;

import javax.annotation.ParametersAreNonnullByDefault;

public class FloatingHeadAnimation implements Runnable {

    public static final double Y_DEVIANCY = 0.2;
    public static final long SPEED = 1;

    private final ArmorStand armorStand;
    private final double baseY;
    private boolean directionUp = true;
    private ScheduledTask task;

    @ParametersAreNonnullByDefault
    public FloatingHeadAnimation(ArmorStand armorStand) {
        this.armorStand = armorStand;
        this.baseY = armorStand.getLocation().getY();
    }

    public void start() {
        if (task != null) {
            return;
        }

        task = armorStand.getScheduler().runAtFixedRate(
            CrystamaeHistoria.getInstance(),
            scheduledTask -> run(),
            () -> task = null,
            1L,
            SPEED
        );
    }

    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public void run() {
        if (directionUp) {
            ArmourStandUtils.panelAnimationStep(armorStand, true);
            if (armorStand.getLocation().getY() >= (baseY + Y_DEVIANCY)) {
                directionUp = false;
            }
        } else {
            ArmourStandUtils.panelAnimationStep(armorStand, false);
            if (armorStand.getLocation().getY() <= (baseY - Y_DEVIANCY)) {
                directionUp = true;
            }
        }
    }
}
