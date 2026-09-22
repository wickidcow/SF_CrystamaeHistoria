package io.github.sefiraat.crystamaehistoria;

import io.github.sefiraat.crystamaehistoria.magic.CastInformation;
import io.github.sefiraat.crystamaehistoria.magic.DisplayItem;
import io.github.sefiraat.crystamaehistoria.magic.spells.spellobjects.MagicFallingBlock;
import io.github.sefiraat.crystamaehistoria.magic.spells.spellobjects.MagicProjectile;
import io.github.sefiraat.crystamaehistoria.magic.spells.spellobjects.MagicSummon;
import io.github.sefiraat.crystamaehistoria.runnables.spells.SpellTickRunnable;
import io.github.thebusybiscuit.slimefun4.libraries.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.UUID;

public class SpellMemory {

    @Getter
    private final Map<MagicProjectile, Pair<CastInformation, Long>> projectileMap = new ConcurrentHashMap<>();
    @Getter
    private final Map<MagicFallingBlock, Pair<CastInformation, Long>> fallingBlockMap = new ConcurrentHashMap<>();
    @Getter
    private final Map<UUID, Pair<CastInformation, Long>> strikeMap = new ConcurrentHashMap<>();
    @Getter
    private final Map<SpellTickRunnable, Integer> tickingCastables = new ConcurrentHashMap<>();
    @Getter
    private final Map<BlockPosition, Long> blocksToRemove = new ConcurrentHashMap<>();
    @Getter
    private final Map<MagicSummon, Long> summonedEntities = new ConcurrentHashMap<>();
    @Getter
    private final Map<UUID, Long> playersWithFlight = new ConcurrentHashMap<>();
    @Getter
    private final Map<UUID, Long> playersWithFrozenTime = new ConcurrentHashMap<>();
    @Getter
    private final Map<UUID, Long> playersWithFrozenWeather = new ConcurrentHashMap<>();
    @Getter
    private final Map<UUID, Long> inhibitedEndermen = new ConcurrentHashMap<>();
    @Getter
    private final Map<BoundingBox, Long> noSpawningAreas = new ConcurrentHashMap<>();
    @Getter
    private final Map<DisplayItem, Long> displayItems = new ConcurrentHashMap<>();
    @Getter
    private final Map<UUID, Location> sleepingBags = new ConcurrentHashMap<>();

    public void clearAll() {
        // Cancels all outstanding spells being cast
        for (SpellTickRunnable spellTickRunnable : tickingCastables.keySet()) {
            spellTickRunnable.cancel();
        }
        tickingCastables.clear();

        // Clear all projectiles created from spells
        removeProjectiles(true);
        projectileMap.clear();

        // Clear all projectiles created from spells
        removeFallingBlocks(true);
        fallingBlockMap.clear();

        // Clear all spawned entities created from spells
        removeEntities(true);
        summonedEntities.clear();

        // Remove all temporary blocks
        removeBlocks(true);
        blocksToRemove.clear();

        // Remove and disable all players flight
        removeFlight(true);
        playersWithFlight.clear();

        // Reset all players personal time
        removeFrozenTime(true);
        playersWithFrozenTime.clear();

        // Reset all players personal weather
        removeFrozenWeather(true);
        playersWithFrozenWeather.clear();

        // Reenable Enderman teleporting
        removeEnderman(true);
        inhibitedEndermen.clear();

        // Re-enable Chunk Spawning
        enableSpawningInArea(true);
        noSpawningAreas.clear();

        // Remove Floating display items
        removeDisplayItems(true);
        displayItems.clear();

        // Remove Sleeping Bags
        removeSleepingBags();
        sleepingBags.clear();

    }

    public void removeProjectiles(boolean forceRemoveAll) {
        Set<MagicProjectile> set = new HashSet<>(projectileMap.keySet());
        for (MagicProjectile magicProjectile : set) {
            long expiry = projectileMap.get(magicProjectile).getSecondValue();
            if (System.currentTimeMillis() > expiry || forceRemoveAll) {
                magicProjectile.kill();
            }
        }
    }

    public void removeFallingBlocks(boolean forceRemoveAll) {
        Set<MagicFallingBlock> set = new HashSet<>(fallingBlockMap.keySet());
        for (MagicFallingBlock magicFallingBlock : set) {
            long expiry = fallingBlockMap.get(magicFallingBlock).getSecondValue();
            if (System.currentTimeMillis() > expiry || forceRemoveAll) {
                magicFallingBlock.kill();
            }
        }
    }

    public void removeEntities(boolean forceRemoveAll) {
        Set<MagicSummon> set = new HashSet<>(CrystamaeHistoria.getSummonedEntityMap().keySet());
        for (MagicSummon magicSummon : set) {
            long expiry = summonedEntities.get(magicSummon);
            if (System.currentTimeMillis() > expiry || magicSummon.getMob() == null || forceRemoveAll) {
                magicSummon.kill();
            } else {
                magicSummon.run();
            }
        }
    }

    public void removeBlocks(boolean forceRemoveAll) {
        long time = System.currentTimeMillis();
        final Set<Map.Entry<BlockPosition, Long>> set = new HashSet<>(blocksToRemove.entrySet());
        for (Map.Entry<BlockPosition, Long> entry : set) {
            if (forceRemoveAll || entry.getValue() < time) {
                final BlockPosition position = entry.getKey();
                final Location location = position.getBlock().getLocation();
                blocksToRemove.remove(position);
                CrystamaeHistoria.getInstance().getServer().getRegionScheduler()
                    .execute(CrystamaeHistoria.getInstance(), location, () -> position.getBlock().setType(Material.AIR));
            }
        }
    }

    public void removeFlight(boolean forceRemoveAll) {
        long time = System.currentTimeMillis();
        final Set<Map.Entry<UUID, Long>> set = new HashSet<>(playersWithFlight.entrySet());
        for (Map.Entry<UUID, Long> entry : set) {
            if (forceRemoveAll || entry.getValue() < time) {
                Player player = Bukkit.getPlayer(entry.getKey());
                playersWithFlight.remove(entry.getKey());
                if (player != null) {
                    player.getScheduler().execute(
                        CrystamaeHistoria.getInstance(),
                        () -> {
                            player.setAllowFlight(false);
                            player.setFlying(false);
                        },
                        null,
                        1L
                    );
                }
            }
        }
    }

    public void removeFrozenTime(boolean forceRemoveAll) {
        long time = System.currentTimeMillis();
        final Set<Map.Entry<UUID, Long>> set = new HashSet<>(playersWithFrozenTime.entrySet());
        for (Map.Entry<UUID, Long> entry : set) {
            if (forceRemoveAll || entry.getValue() < time) {
                Player player = Bukkit.getPlayer(entry.getKey());
                playersWithFrozenTime.remove(entry.getKey());
                if (player != null) {
                    player.getScheduler().execute(
                        CrystamaeHistoria.getInstance(),
                        player::resetPlayerTime,
                        null,
                        1L
                    );
                }
            }
        }
    }

    public void removeFrozenWeather(boolean forceRemoveAll) {
        long time = System.currentTimeMillis();
        final Set<Map.Entry<UUID, Long>> set = new HashSet<>(playersWithFrozenWeather.entrySet());
        for (Map.Entry<UUID, Long> entry : set) {
            if (forceRemoveAll || entry.getValue() < time) {
                Player player = Bukkit.getPlayer(entry.getKey());
                playersWithFrozenWeather.remove(entry.getKey());
                if (player != null) {
                    player.getScheduler().execute(
                        CrystamaeHistoria.getInstance(),
                        player::resetPlayerWeather,
                        null,
                        1L
                    );
                }
            }
        }
    }

    public void removeEnderman(boolean forceRemoveAll) {
        long time = System.currentTimeMillis();
        final Set<Map.Entry<UUID, Long>> set = new HashSet<>(inhibitedEndermen.entrySet());
        for (Map.Entry<UUID, Long> entry : set) {
            if (forceRemoveAll || entry.getValue() < time) {
                inhibitedEndermen.remove(entry.getKey());
            }
        }
    }

    public void enableSpawningInArea(boolean forceRemoveAll) {
        long time = System.currentTimeMillis();
        final Set<Map.Entry<BoundingBox, Long>> set = new HashSet<>(noSpawningAreas.entrySet());
        for (Map.Entry<BoundingBox, Long> entry : set) {
            if (forceRemoveAll || entry.getValue() < time) {
                noSpawningAreas.remove(entry.getKey());
            }
        }
    }

    public void removeDisplayItems(boolean forceRemoveAll) {
        long time = System.currentTimeMillis();
        final Set<Map.Entry<DisplayItem, Long>> set = new HashSet<>(displayItems.entrySet());
        for (Map.Entry<DisplayItem, Long> entry : set) {
            if (forceRemoveAll || entry.getValue() < time) {
                entry.getKey().kill();
                displayItems.remove(entry.getKey());
            }
        }
    }

    public void removeSleepingBags() {
        final Set<Map.Entry<UUID, Location>> set = new HashSet<>(sleepingBags.entrySet());
        for (Map.Entry<UUID, Location> entry : set) {
            final Location location = entry.getValue();
            CrystamaeHistoria.getInstance().getServer().getRegionScheduler()
                .execute(CrystamaeHistoria.getInstance(), location, () -> location.getBlock().setType(Material.AIR));
        }
    }

    public void removeFlight(Player player) {
        if (playersWithFlight.containsKey(player.getUniqueId())) {
            player.setAllowFlight(false);
            player.setFlying(false);
            playersWithFlight.remove(player.getUniqueId());
        }
    }

    public void stopBlockRemoval(Block block) {
        BlockPosition blockPosition = new BlockPosition(block);
        blocksToRemove.remove(blockPosition);
    }
}
