package io.github.sefiraat.crystamaehistoria.managers;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersistentYamlFileTest {
    @TempDir
    Path directory;

    private Path registry() {
        return directory.resolve("player_stats.yml");
    }

    @Test
    void validEmptyRegistryRemainsValidAndReadOnly() throws Exception {
        byte[] original = "# Fresh registry\n".getBytes(StandardCharsets.UTF_8);
        Files.write(registry(), original);
        assertTrue(PersistentYamlFile.load(registry()).getKeys(false).isEmpty());
        assertArrayEquals(original, Files.readAllBytes(registry()));
    }

    @Test
    void corruptYamlDoesNotBecomeAnEmptyRegistryOrChangeOriginalBytes() throws Exception {
        byte[] original = "'9223372036854775807': [invalid\n".getBytes(StandardCharsets.UTF_8);
        Files.write(registry(), original);
        assertThrows(InvalidConfigurationException.class, () -> PersistentYamlFile.load(registry()));
        assertArrayEquals(original, Files.readAllBytes(registry()));
    }

    @Test
    void partialYamlBeforeTheErrorIsNeverReturnedAsUsableRegistry() throws Exception {
        Files.writeString(registry(), "'42':\n  last_user: Existing\ninvalid: [\n");
        assertThrows(InvalidConfigurationException.class, () -> PersistentYamlFile.load(registry()));
        assertTrue(Files.readString(registry()).contains("Existing"));
    }

    @Test
    void missingDirectoryAndInvalidUtf8FailWithoutCreatingReplacement() throws Exception {
        assertThrows(IOException.class, () -> PersistentYamlFile.load(registry()));
        assertFalse(Files.exists(registry()));
        assertThrows(IOException.class, () -> PersistentYamlFile.load(directory));
        byte[] original = {(byte) 0xff, (byte) 0xfe};
        Files.write(registry(), original);
        assertThrows(IOException.class, () -> PersistentYamlFile.load(registry()));
        assertArrayEquals(original, Files.readAllBytes(registry()));
    }

    @Test
    void preservesLongIdentityOwnerAndUnknownDataAcrossRepeatedSaveAndReopen() throws Exception {
        Files.writeString(registry(), "'9223372036854775807':\n  last_user: ExactOwner\n  unknown: keep\n"
            + "  amounts: [2147483647, 0, 37]\n'9007199254740993':\n  last_user: OtherOwner\n");
        for (int cycle = 0; cycle < 3; cycle++) {
            var data = PersistentYamlFile.load(registry());
            assertEquals("ExactOwner", data.getString("9223372036854775807.last_user"));
            assertEquals("OtherOwner", data.getString("9007199254740993.last_user"));
            assertEquals("keep", data.getString("9223372036854775807.unknown"));
            assertEquals(List.of(2147483647, 0, 37), data.getIntegerList("9223372036854775807.amounts"));
            assertEquals(2, data.getKeys(false).size());
            PersistentYamlFile.save(data, registry());
        }
        assertNoTemporaryFiles();
    }

    @Test
    void serializationFailureCannotTruncateThePreviousRegistry() throws Exception {
        byte[] original = "'42':\n  last_user: Existing\n".getBytes(StandardCharsets.UTF_8);
        Files.write(registry(), original);
        YamlConfiguration broken = new YamlConfiguration() {
            @Override
            public String saveToString() {
                throw new IllegalStateException("Synthetic serialization failure");
            }
        };
        assertThrows(IllegalStateException.class, () -> PersistentYamlFile.save(broken, registry()));
        assertArrayEquals(original, Files.readAllBytes(registry()));
        assertNoTemporaryFiles();
    }

    @Test
    void replacesOnlyTheRequestedRegistryAfterCompleteSerialization() throws Exception {
        Files.writeString(registry(), "old: data\n");
        var unrelated = directory.resolve("other.yml");
        Files.writeString(unrelated, "leave unchanged\n");
        var data = new YamlConfiguration();
        data.set("42.last_user", "Owner");
        PersistentYamlFile.save(data, registry());
        assertFalse(PersistentYamlFile.load(registry()).contains("old"));
        assertEquals("Owner", PersistentYamlFile.load(registry()).getString("42.last_user"));
        assertEquals("leave unchanged\n", Files.readString(unrelated));
        assertNoTemporaryFiles();
    }

    @Test
    void failedReplacementLeavesTheDestinationAndCleansTheStage() throws Exception {
        Files.createDirectory(registry());
        var child = registry().resolve("do-not-delete");
        Files.writeString(child, "preserve");
        assertThrows(IOException.class, () -> PersistentYamlFile.save(new YamlConfiguration(), registry()));
        assertEquals("preserve", Files.readString(child));
        assertNoTemporaryFiles();
    }

    @Test
    void fileSymlinkStillPointsToTheSameStorageTarget() throws Exception {
        var target = directory.resolve("real.yml");
        Files.writeString(target, "'42':\n  last_user: Before\n");
        Files.createSymbolicLink(registry(), target.getFileName());
        var data = PersistentYamlFile.load(registry());
        data.set("42.last_user", "After");
        PersistentYamlFile.save(data, registry());
        assertTrue(Files.isSymbolicLink(registry()));
        assertEquals(target.getFileName(), Files.readSymbolicLink(registry()));
        assertEquals("After", PersistentYamlFile.load(target).getString("42.last_user"));
        assertNoTemporaryFiles();
    }

    @Test
    void danglingSymlinkIsNotReplacedWithAnEmptyFile() throws Exception {
        var target = directory.resolve("missing.yml");
        Files.createSymbolicLink(registry(), target.getFileName());
        assertThrows(IOException.class, () -> PersistentYamlFile.load(registry()));
        assertThrows(IOException.class, () -> PersistentYamlFile.save(new YamlConfiguration(), registry()));
        assertTrue(Files.isSymbolicLink(registry()));
        assertFalse(Files.exists(target));
        assertNoTemporaryFiles();
    }

    @Test
    void existingPosixPermissionsArePreserved() throws Exception {
        Files.writeString(registry(), "'42': {}\n");
        var expected = PosixFilePermissions.fromString("rw-r-----");
        Files.setPosixFilePermissions(registry(), expected);
        PersistentYamlFile.save(PersistentYamlFile.load(registry()), registry());
        assertEquals(expected, Files.getPosixFilePermissions(registry()));
    }

    @Test
    void firstSaveCreatesParentsWithoutRequiringExistingData() throws Exception {
        var file = directory.resolve("new/nested/player_stats.yml");
        var data = new YamlConfiguration();
        data.set("42.last_user", "FirstOwner");
        PersistentYamlFile.save(data, file);
        assertEquals("FirstOwner", PersistentYamlFile.load(file).getString("42.last_user"));
    }

    @Test
    void exactProgressAndDisabledSpellValuesSurviveRepeatedWrites() throws Exception {
        String uuid = "2f017f3a-8442-4ef2-9fba-456789abcdef";
        var stats = new YamlConfiguration();
        stats.set(uuid + ".SPELL.heal.UNLOCKED", true);
        stats.set(uuid + ".SPELL.heal.TIMES_CAST", Integer.MAX_VALUE - 17);
        stats.set(uuid + ".STORY.STONE.TIMES_CHRONICLED", 123456);
        stats.set(uuid + ".STORY.STONE.TIMES_REALISED", 654321);
        stats.set(uuid + ".STORY.STONE.GILDED", true);
        stats.set(uuid + ".opaque_long", Long.MAX_VALUE);
        var spells = new YamlConfiguration();
        spells.set("heal", false);
        spells.set("unknown_external", "preserved");
        var path = directory.resolve("spells.yml");
        for (int cycle = 0; cycle < 3; cycle++) {
            PersistentYamlFile.save(stats, registry());
            PersistentYamlFile.save(spells, path);
            stats = PersistentYamlFile.load(registry());
            spells = PersistentYamlFile.load(path);
            assertEquals(Integer.MAX_VALUE - 17, stats.getInt(uuid + ".SPELL.heal.TIMES_CAST"));
            assertTrue(stats.getBoolean(uuid + ".SPELL.heal.UNLOCKED"));
            assertEquals(123456, stats.getInt(uuid + ".STORY.STONE.TIMES_CHRONICLED"));
            assertEquals(654321, stats.getInt(uuid + ".STORY.STONE.TIMES_REALISED"));
            assertTrue(stats.getBoolean(uuid + ".STORY.STONE.GILDED"));
            assertEquals(Long.MAX_VALUE, stats.getLong(uuid + ".opaque_long"));
            assertTrue(spells.contains("heal"));
            assertFalse(spells.getBoolean("heal"));
            assertEquals("preserved", spells.getString("unknown_external"));
        }
    }

    private void assertNoTemporaryFiles() throws IOException {
        try (var files = Files.list(directory)) {
            assertTrue(files.noneMatch(path -> path.getFileName().toString().endsWith(".tmp")));
        }
    }
}
