package io.github.sefiraat.crystamaehistoria.slimefun.items.mechanisms;

import io.github.sefiraat.crystamaehistoria.utils.ArmourStandUtils;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public abstract class DisplayStandHolder extends AbstractCache {

    protected DisplayStandHolder(BlockMenu blockMenu) {
        super(blockMenu);
    }

    @Override
    public void kill(Location location) {
        super.kill(location);
        getDisplayStand().remove();
    }

    protected World getWorld() {
        return blockMenu.getLocation().getWorld();
    }

    protected Location getLocation() {
        return blockMenu.getLocation().clone();
    }

    protected Location getLocation(boolean centered) {
        if (centered) {
            return getLocation().add(0.5, 0.5, 0.5);
        } else {
            return getLocation();
        }
    }

    @ParametersAreNonnullByDefault
    protected ArmorStand getDisplayStand() {
        Block block = blockMenu.getBlock();
        String uuidString = StorageCacheUtils.getData(getLocation(), "ch_display_stand");
        if (uuidString != null) {
            UUID uuid = UUID.fromString(uuidString);
            return (ArmorStand) Bukkit.getEntity(uuid);
        } else {
            final ArmorStand armorStand = (ArmorStand) block.getWorld().spawnEntity(getLocation().add(0.5, -1.7, 0.5), EntityType.ARMOR_STAND);
            ArmourStandUtils.setDisplay(armorStand);
            StorageCacheUtils.setData(block.getLocation(), "ch_display_stand", armorStand.getUniqueId().toString());
            return armorStand;
        }
    }

}
