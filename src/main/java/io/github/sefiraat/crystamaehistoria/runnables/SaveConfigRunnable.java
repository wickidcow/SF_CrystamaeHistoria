package io.github.sefiraat.crystamaehistoria.runnables;

import io.github.sefiraat.crystamaehistoria.CrystamaeHistoria;

public class SaveConfigRunnable implements Runnable {

    @Override
    public void run() {
        CrystamaeHistoria.getConfigManager().saveAll();
    }
}
