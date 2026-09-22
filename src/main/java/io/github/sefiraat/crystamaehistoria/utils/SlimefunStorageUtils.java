package io.github.sefiraat.crystamaehistoria.utils;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public final class SlimefunStorageUtils {

    private SlimefunStorageUtils() {
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public static String getData(Location location, String key) {
        return StorageCacheUtils.getData(location, key);
    }

    @ParametersAreNonnullByDefault
    public static void setData(Location location, String key, String value) {
        StorageCacheUtils.setData(location, key, value);
    }

    @ParametersAreNonnullByDefault
    public static void removeData(Location location, String key) {
        StorageCacheUtils.removeData(location, key);
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public static BlockMenu getMenu(Location location) {
        return StorageCacheUtils.getMenu(location);
    }

    @ParametersAreNonnullByDefault
    public static void removeBlock(Location location) {
        Slimefun.getDatabaseManager().getBlockDataController().removeBlock(location);
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public static SlimefunItem getSlimefunItem(Location location) {
        final var blockData = StorageCacheUtils.getBlock(location);
        if (blockData != null) {
            return SlimefunItem.getById(blockData.getSfId());
        }

        final var universalData = StorageCacheUtils.getUniversalBlock(location);
        return universalData == null ? null : SlimefunItem.getById(universalData.getSfId());
    }
}
