package io.github.sefiraat.crystamaehistoria.managers;

import io.github.sefiraat.crystamaehistoria.stories.Story;
import io.github.sefiraat.crystamaehistoria.stories.StoryMaterialResolver;
import io.github.sefiraat.crystamaehistoria.stories.definition.StoryRarity;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class StoryMaterialsTest {
    @TempDir
    Path directory;

    private final List<String> messages = new ArrayList<>();

    private StoriesManager load(YamlConfiguration blocks) throws Exception {
        Logger logger = Logger.getAnonymousLogger();
        logger.setUseParentHandlers(false);
        logger.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                messages.add(record.getMessage());
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        });
        return new StoriesManager(resource("generic-stories.yml"), blocks, logger);
    }

    private static YamlConfiguration resource(String name) throws Exception {
        var input = StoryMaterialsTest.class.getClassLoader().getResourceAsStream(name);
        assertNotNull(input, name);
        try (var reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            var config = new YamlConfiguration();
            config.load(reader);
            return config;
        }
    }

    private static void copyChain(YamlConfiguration source, YamlConfiguration target, String key) {
        var section = source.getConfigurationSection("CHAIN");
        assertNotNull(section);
        target.createSection(key);
        // Do not share mutable MemorySection instances between the two test stories.
        section.getValues(true).forEach((path, value) -> {
            if (!(value instanceof ConfigurationSection)) {
                target.set(key + "." + path, value);
            }
        });
    }

    @ParameterizedTest
    @CsvSource({"CHAIN,IRON_CHAIN", "GRASS,SHORT_GRASS", "SCUTE,TURTLE_SCUTE"})
    void historicalKeysResolveToTheirExactModernMaterials(String oldName, String currentName) {
        assertSame(Material.valueOf(currentName), StoryMaterialResolver.resolve(oldName));
        assertSame(Material.valueOf(currentName), StoryMaterialResolver.resolve(currentName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"STONE", "GRASS_BLOCK", "TALL_GRASS", "ARMADILLO_SCUTE", "COPPER_CHAIN"})
    void unrelatedMaterialsAreNotReinterpreted(String name) {
        assertSame(Material.valueOf(name), StoryMaterialResolver.resolve(name));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"NOT_A_MATERIAL", "CHIAN", "SCUTES"})
    void genuinelyInvalidKeysRemainInvalid(String name) {
        assertNull(StoryMaterialResolver.resolve(name));
    }

    @Test
    void everyBundledBlockStoryLoadsWithoutChangingConfigurationOrStoryIdentity() throws Exception {
        var blocks = resource("blocks.yml");
        String before = blocks.saveToString();
        var manager = load(blocks);
        assertEquals(blocks.getKeys(false).size(), manager.getBlockDefinitionMap().size());
        assertTrue(messages.stream().noneMatch(message -> message.contains("Ignoring")), messages.toString());
        Map<String, Story> expectedById = new HashMap<>();
        for (String key : blocks.getKeys(false)) {
            var material = StoryMaterialResolver.resolve(key);
            assertNotNull(material, key);
            var definition = manager.getBlockDefinitionMap().get(material);
            assertNotNull(definition, key);
            var story = definition.getUnique();
            assertEquals(blocks.getString(key + ".story.name"), story.getId(), key);
            assertEquals(blocks.getStringList(key + ".story.lore"), story.getStoryStrings(), key);
            // Some bundled block variants already share story names. Preserve their
            // existing last-definition lookup instead of inventing new saved IDs.
            expectedById.put(story.getId(), story);
        }
        assertEquals(expectedById.size(), manager.getStoryMapUnique().size());
        expectedById.forEach((id, story) ->
            assertSame(story, manager.getStory(id, StoryRarity.UNIQUE), id));
        assertEquals(before, blocks.saveToString());
        System.out.println("Verified " + manager.getBlockDefinitionMap().size()
            + " bundled block stories, including CHAIN, GRASS and SCUTE; no invalid-material skips.");
    }

    @ParameterizedTest
    @CsvSource({"CHAIN,IRON_CHAIN", "GRASS,SHORT_GRASS", "SCUTE,TURTLE_SCUTE"})
    void customizedLegacyStoriesSurviveSaveReopenAndRepeatedLoads(String oldName, String currentName)
        throws Exception {
        var blocks = resource("blocks.yml");
        blocks.set(oldName + ".story.name", "Owner-customized " + oldName);
        blocks.set(oldName + ".story.lore", List.of("Keep my exact lore.", "Second line."));
        blocks.set(oldName + ".story.author", "Original owner");
        blocks.set(oldName + ".owner-extension", Long.MAX_VALUE);
        var file = directory.resolve("blocks.yml");
        for (int cycle = 0; cycle < 3; cycle++) {
            PersistentYamlFile.save(blocks, file);
            byte[] before = Files.readAllBytes(file);
            blocks = PersistentYamlFile.load(file);
            var manager = load(blocks);
            var story = manager.getBlockDefinitionMap().get(Material.valueOf(currentName)).getUnique();
            assertEquals("Owner-customized " + oldName, story.getId());
            assertEquals(List.of("Keep my exact lore.", "Second line."), story.getStoryStrings());
            assertEquals("Original owner", story.getAuthor());
            assertSame(story, manager.getStory(story.getId(), StoryRarity.UNIQUE));
            assertTrue(blocks.isConfigurationSection(oldName));
            assertFalse(blocks.contains(currentName));
            assertEquals(Long.MAX_VALUE, blocks.getLong(oldName + ".owner-extension"));
            assertArrayEquals(before, Files.readAllBytes(file));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void currentNameOverridesAliasInEitherOrderWithoutDroppingHistoricalStoryIds(boolean currentFirst)
        throws Exception {
        var source = resource("blocks.yml");
        var blocks = new YamlConfiguration();
        for (String key : currentFirst ? List.of("IRON_CHAIN", "CHAIN") : List.of("CHAIN", "IRON_CHAIN")) {
            copyChain(source, blocks, key);
            blocks.set(key + ".story.name", key + " saved story");
            blocks.set(key + ".story.lore", List.of(key + " owner lore"));
        }
        var manager = load(blocks);
        assertEquals(1, manager.getBlockDefinitionMap().size());
        assertEquals("IRON_CHAIN saved story", manager.getBlockDefinitionMap().get(Material.IRON_CHAIN).getUnique().getId());
        assertNotNull(manager.getStory("CHAIN saved story", StoryRarity.UNIQUE));
        assertNotNull(manager.getStory("IRON_CHAIN saved story", StoryRarity.UNIQUE));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void currentNameAlsoWinsWhenAliasAndCurrentNameHaveTheSameStoryId(boolean currentFirst) throws Exception {
        var source = resource("blocks.yml");
        var blocks = new YamlConfiguration();
        for (String key : currentFirst ? List.of("IRON_CHAIN", "CHAIN") : List.of("CHAIN", "IRON_CHAIN")) {
            copyChain(source, blocks, key);
            blocks.set(key + ".story.name", "Existing saved identity");
            blocks.set(key + ".story.lore", List.of(key + " owner lore"));
        }
        var manager = load(blocks);
        var story = manager.getStory("Existing saved identity", StoryRarity.UNIQUE);
        assertNotNull(story);
        assertEquals(List.of("IRON_CHAIN owner lore"), story.getStoryStrings());
        assertSame(story, manager.getBlockDefinitionMap().get(Material.IRON_CHAIN).getUnique());
    }

    @Test
    void invalidMaterialStillLogsAndDoesNotGetAnArbitraryFallback() throws Exception {
        var source = resource("blocks.yml");
        var blocks = new YamlConfiguration();
        copyChain(source, blocks, "NOT_A_MATERIAL");
        var manager = load(blocks);
        assertTrue(manager.getBlockDefinitionMap().isEmpty());
        assertTrue(manager.getStoryMapUnique().isEmpty());
        assertTrue(messages.stream().anyMatch(message ->
            message.contains("Ignoring a story with an invalid material -> NOT_A_MATERIAL")));
    }
}
