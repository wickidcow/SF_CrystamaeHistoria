package test.crystamae;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/** Disposable Paper integration fixture only; never packaged in the addon JAR. */
public final class NativeArmorStandProbe extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, this::verify, 40L);
    }

    private void verify() {
        ArmorStand stand = null;
        try {
            Plugin crystamae = Bukkit.getPluginManager().getPlugin("CrystamaeHistoria");
            if (crystamae == null || !crystamae.isEnabled()) {
                throw new IllegalStateException("CrystamaeHistoria was not enabled");
            }
            World world = Bukkit.getWorlds().get(0);
            Location loc = world.getSpawnLocation().clone().add(2.0, 2.0, 2.0);
            stand = world.spawn(loc, ArmorStand.class);
            Class<?> utils = crystamae.getClass().getClassLoader()
                    .loadClass("io.github.sefiraat.crystamaehistoria.utils.ArmourStandUtils");
            utils.getMethod("setDisplay", ArmorStand.class).invoke(null, stand);
            boolean removeWhenFarAway = (boolean) ArmorStand.class
                    .getMethod("getRemoveWhenFarAway").invoke(stand);
            if (removeWhenFarAway) {
                throw new AssertionError("Crystamae display armor stand must not distance-despawn");
            }
            if (!stand.isMarker() || stand.hasGravity() || stand.isVisible()) {
                throw new AssertionError("Display stand configuration changed");
            }
            getLogger().info("NATIVE_ARMORSTAND_POLICY_PASS");
        } catch (Throwable failure) {
            getLogger().log(Level.SEVERE, "NATIVE_ARMORSTAND_POLICY_FAIL", failure);
        } finally {
            if (stand != null) {
                stand.remove();
            }
        }
    }
}
