#!/usr/bin/env python3
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
