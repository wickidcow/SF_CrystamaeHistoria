from pathlib import Path
import hashlib,json
root=Path.cwd()
def put(name,text):
    p=root/name;p.parent.mkdir(parents=True,exist_ok=True);p.write_text(text)
def blob(data):return hashlib.sha1(b'blob '+str(len(data)).encode()+b'\0'+data).hexdigest()
helper=Path('dank-source/src/main/java/io/github/sefiraat/danktech2/managers/PackRegistryFile.java').read_text()
assert blob(helper.encode())=='2ff8bca18e5a58a6b18559f6c72b97c93ee84466'
helper=helper.replace('package io.github.sefiraat.danktech2.managers;','package io.github.sefiraat.crystamaehistoria.managers;').replace('PackRegistryFile','PersistentYamlFile').replace('never an empty recovery registry','never empty recovery data')
put('src/main/java/io/github/sefiraat/crystamaehistoria/managers/PersistentYamlFile.java',helper)
name='src/main/java/io/github/sefiraat/crystamaehistoria/managers/ConfigManager.java';t=Path(name).read_text()
t=t.replace('import java.io.BufferedReader;\n','').replace('import java.io.InputStreamReader;','import java.io.InputStreamReader;\nimport java.io.Reader;\nimport java.nio.charset.StandardCharsets;\nimport java.nio.file.Files;\nimport java.nio.file.LinkOption;\nimport java.util.logging.Level;')
start=t.index('    @Nonnull\n    @SuppressWarnings');end=t.index('    @ParametersAreNonnullByDefault\n    public boolean spellEnabled',start)
t=t[:start]+'''    @Nonnull
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

'''+t[end:]
t=t.replace('spells.save(file);','PersistentYamlFile.save(spells, file.toPath());').replace('playerStats.save(file);','PersistentYamlFile.save(playerStats, file.toPath());')
t=t.replace('} catch (IOException exception) {\n                    exception.printStackTrace();','} catch (IOException | RuntimeException exception) {\n                    CrystamaeHistoria.getInstance().getLogger().log(Level.SEVERE,\n                        "Unable to save spells.yml; the previous file was not intentionally truncated.", exception);')
t=t.replace('} catch (IOException exception) {\n            exception.printStackTrace();','} catch (IOException | RuntimeException exception) {\n            CrystamaeHistoria.getInstance().getLogger().log(Level.SEVERE,\n                "Unable to save player_stats.yml; the previous file was not intentionally truncated.", exception);')
put(name,t)
name='src/main/java/io/github/sefiraat/crystamaehistoria/CrystamaeHistoria.java';t=Path(name).read_text()
t=t.replace('    protected void disable() {\n','''    protected void disable() {
        // A rejected data load must not initialize caches or write an empty replacement.
        if (configManager == null) {
            instance = null;
            return;
        }
''').replace('        spellMemory.clearAll();','''        if (spellMemory != null) {
            spellMemory.clearAll();
        }''')
put(name,t)
t=Path('pom.xml').read_text().replace('    <plugins>','''    <plugins>
      <plugin><groupId>org.apache.maven.plugins</groupId><artifactId>maven-surefire-plugin</artifactId><version>3.5.2</version></plugin>''',1).replace('  <dependencies>','''  <dependencies>
    <dependency><groupId>org.junit.jupiter</groupId><artifactId>junit-jupiter</artifactId><version>5.12.2</version><scope>test</scope></dependency>''',1)
put('pom.xml',t)
test=Path('dank-source/src/test/java/io/github/sefiraat/danktech2/managers/PackRegistryFileTest.java').read_text()
assert blob(test.encode())=='e7077dcb6a8987983f7e7b6155a6eba513ff232f'
test=test.replace('package io.github.sefiraat.danktech2.managers;','package io.github.sefiraat.crystamaehistoria.managers;').replace('PackRegistryFile','PersistentYamlFile').replace('dank_packs.yml','player_stats.yml')
idx=test.index('    private void assertNoTemporaryFiles()')
test=test[:idx]+'''    @Test
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

'''+test[idx:]
put('src/test/java/io/github/sefiraat/crystamaehistoria/managers/PersistentYamlFileTest.java',test)
name='.github/workflows/maven.yml';t=Path(name).read_text().replace('      - name: Build universal release JAR\n        run: mvn --batch-mode --no-transfer-progress -DskipTests -Dpaper.version=1.21.11-R0.1-SNAPSHOT clean package','''      - name: Verify persisted data startup boundary
        run: python3 scripts/verify_persisted_data_safety.py
      - name: Build and test universal release JAR
        run: mvn --batch-mode --no-transfer-progress -Dpaper.version=1.21.11-R0.1-SNAPSHOT clean package
      - name: Retain persisted data regression results
        if: always()
        uses: actions/upload-artifact@v7
        with:
          name: persisted-data-test-evidence
          path: target/surefire-reports/TEST-*.xml
          if-no-files-found: warn''')
put(name,t)
put('scripts/verify_persisted_data_safety.py','''#!/usr/bin/env python3
"""Enforce startup ordering and strict persisted-YAML boundaries without hiding compiler warnings."""
from pathlib import Path
root = Path(__file__).resolve().parents[1]
main = (root / 'src/main/java/io/github/sefiraat/crystamaehistoria/CrystamaeHistoria.java').read_text()
manager = (root / 'src/main/java/io/github/sefiraat/crystamaehistoria/managers/ConfigManager.java').read_text()
startup = main.split('public void enable() {', 1)[1].split('protected void disable()', 1)[0]
load = startup.index('this.configManager = new ConfigManager();')
for step in ['new StoriesManager()', 'new ListenerManager()', 'new SpellMemory()', 'new RunnableManager()', 'setupSlimefun();']:
    assert load < startup.index(step), step
shutdown = main.split('protected void disable() {', 1)[1].split('private void setupSlimefun()', 1)[0]
assert shutdown.index('if (configManager == null)') < shutdown.index('ChroniclerPanel.getCaches()')
assert 'if (spellMemory != null)' in shutdown
assert 'YamlConfiguration.loadConfiguration(' not in manager
assert 'PersistentYamlFile.load(file.toPath())' in manager
assert 'PersistentYamlFile.save(playerStats, file.toPath())' in manager
assert 'PersistentYamlFile.save(spells, file.toPath())' in manager
assert 'PersistentYamlFile.save(config, file.toPath())' in manager
assert 'throw new IllegalStateException(' in manager
print('Strict persisted-data initialization and partial-shutdown boundaries passed.')
''')
expected={
 'pom.xml':'4332a2ac0cf65f94dcbb2683a5f4be4868c81e5c',
 'scripts/verify_persisted_data_safety.py':'df4436a22d759645afc5b5e8e88a979b88d55820',
 '.github/workflows/maven.yml':'4ec831e45e0dfff72fc07721f3affc90187f9200',
 'src/test/java/io/github/sefiraat/crystamaehistoria/managers/PersistentYamlFileTest.java':'3c2a1fed74836e7b9d8f5289c36888d8092ef753',
 'src/main/java/io/github/sefiraat/crystamaehistoria/CrystamaeHistoria.java':'c89f185f604fcb7526f261cf7480dcd09513cbb9',
 'src/main/java/io/github/sefiraat/crystamaehistoria/managers/ConfigManager.java':'1e7160017c59ce0b4c560bbe0356c1e07362d3fb',
 'src/main/java/io/github/sefiraat/crystamaehistoria/managers/PersistentYamlFile.java':'be59e926e66e8daa9f9cf14e81509e894fd78dc7'}
for name,sha in expected.items():assert blob(Path(name).read_bytes())==sha,(name,blob(Path(name).read_bytes()),sha)
Path('preservation-evidence').mkdir(exist_ok=True)
Path('preservation-evidence/reviewed-blobs.json').write_text(json.dumps(expected,indent=2)+'\n')
