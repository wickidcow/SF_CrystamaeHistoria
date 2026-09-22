package io.github.sefiraat.crystamaehistoria.listeners;

import io.github.sefiraat.crystamaehistoria.slimefun.items.gadgets.PhilosophersSpray;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.papermc.paper.event.block.BlockFailedDispenseEvent;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.sefiraat.crystamaehistoria.utils.SlimefunStorageUtils;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PhilosophersSprayListener implements Listener {

    @EventHandler
    public void onInteract(BlockFailedDispenseEvent e) {
        final Block block = e.getBlock();
        SlimefunItem item = SlimefunStorageUtils.getSlimefunItem(block.getLocation());
        if (item instanceof PhilosophersSpray) {
            PhilosophersSpray.triggerChange(block);
        }

    }
}
