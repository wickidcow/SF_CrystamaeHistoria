package io.github.sefiraat.crystamaehistoria.listeners;

import io.github.sefiraat.crystamaehistoria.slimefun.items.mechanisms.liquefactionbasin.LiquefactionBasin;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;

public class MaintenanceListener implements Listener {

    @EventHandler
    public void onRemovableBlockBreak(CauldronLevelChangeEvent event) {
        if (StorageCacheUtils.getSlimefunItem(event.getBlock().getLocation()) instanceof LiquefactionBasin) {
            event.setCancelled(true);
        }
    }
}
