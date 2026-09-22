package io.github.sefiraat.crystamaehistoria.utils;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Location;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public final class SlimefunStorageUtils {

    private SlimefunStorageUtils() {
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
