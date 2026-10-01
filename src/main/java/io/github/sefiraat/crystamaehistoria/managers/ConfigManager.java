package io.github.sefiraat.crystamaehistoria.managers;

import io.github.sefiraat.crystamaehistoria.CrystamaeHistoria;
import io.github.sefiraat.crystamaehistoria.magic.SpellType;
import io.github.sefiraat.crystamaehistoria.magic.spells.core.Spell;
import io.github.sefiraat.crystamaehistoria.slimefun.items.mechanisms.liquefactionbasin.LiquefactionBasinCache;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.logging.Level;

@Getter
public class ConfigManager {

    private final FileConfiguration blocks;
    private final FileConfiguration stories;
    private final FileConfiguration playerStats;
    private final FileConfiguration blockColors;
    private final FileConfiguration spells;

    public ConfigManager() {
        this.blocks = getConfig("blocks.yml", true);
        this.stories = getConfig("generic-stories.yml", true);
        this.playerStats = getConfig("player_stats.yml", false);
        this.blockColors = getConfig("block_colors.yml", false);
        this.spells = getConfig("spells.yml", false);
    }

    @Nonnull
    private FileConfiguration getConfig(@Nonnull String fileName, boolean updateWithDefaults) {
        final CrystamaeHistoria plugin = CrystamaeHistoria.getInstance();
        final File file = new File(plugin.getDataFolder(), fileName);
        try {
            if (Files.notExists(file.toPath(), LinkOption.NOFOLLOW_LINKS)) {
                Files.createDirectories(file.toPath().getParent());
                Files.createFile(file.toPath());
            }
            FileConfiguration configuration = PersistentYamlFile.load(file.toPath());
            if (updateWithDefaults) {
                updateConfig(configuration, file, fileName);
            }
            return configuration;
        } catch (IOException | InvalidConfigurationException failure) {
            throw new IllegalStateException("Unable to load " + fileName
                + "; CrystamaeHistoria is stopping to protect existing progress and settings.", failure);
        }
    }

    @ParametersAreNonnullByDefault
    private void updateConfig(FileConfiguration config, File file, String fileName)
        throws IOException, InvalidConfigurationException {
        final InputStream input = CrystamaeHistoria.getInstance().getResource(fileName);
        if (input == null) {
            throw new IOException("Missing bundled defaults for " + fileName);
        }
        try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            final YamlConfiguration defaults = new YamlConfiguration();
            defaults.load(reader);
            config.addDefaults(defaults);
            config.options().copyDefaults(true);
            PersistentYamlFile.save(config, file.toPath());
        }
    }

    @ParametersAreNonnullByDefault
    public boolean spellEnabled(Spell spell) {
        return spells.getBoolean(spell.getId());
    }

    public void loadConfig() {
        // Spells
        for (SpellType spellType : SpellType.getCachedValues()) {
            Spell spell = spellType.getSpell();
            if (!spells.contains(spell.getId())) {
                try {
                    final File file = new File(CrystamaeHistoria.getInstance().getDataFolder(), "spells.yml");
                    spells.set(spell.getId(), true);
                    PersistentYamlFile.save(spells, file.toPath());
                } catch (IOException | RuntimeException exception) {
                    CrystamaeHistoria.getInstance().getLogger().log(Level.SEVERE,
                        "Unable to save spells.yml; the previous file was not intentionally truncated.", exception);
                }
            }
            boolean enabled = spells.getBoolean(spell.getId());
            spell.setEnabled(enabled);
            if (enabled) {
                LiquefactionBasinCache.addSpellRecipe(spellType, spell.getRecipe());
            }
        }
    }

    public void saveAll() {
        CrystamaeHistoria.getInstance().getLogger().info("Crystamae saving data.");
        CrystamaeHistoria.getInstance().getConfig().save();
        saveResearches();
    }

    private void saveResearches() {
        File file = new File(CrystamaeHistoria.getInstance().getDataFolder(), "player_stats.yml");
        try {
            PersistentYamlFile.save(playerStats, file.toPath());
        } catch (IOException | RuntimeException exception) {
            CrystamaeHistoria.getInstance().getLogger().log(Level.SEVERE,
                "Unable to save player_stats.yml; the previous file was not intentionally truncated.", exception);
        }
    }
}
