# Actual persisted-data validation

## Additional lifecycle defect found and corrected

The first strict-reader candidate kept malformed data intact, but InfinityLib's AbstractAddon.onEnable catches RuntimeException and does not disable the plugin. Real server tests correctly rejected that half-initialized enabled state. ConfigManager creation now has a bounded failure guard that logs the original cause, explicitly disables this plugin and returns before addon services or items register. The existing partial-shutdown guard avoids cache initialization and saving absent data.

Only the two tested lifecycle/source-guard blobs are added to the initial preservation candidate. No item, research or spell IDs, formulas, player UUIDs, progress keys, recipes, rates or data formats change.

## Build and file regressions

Explicit guard run 36802165446 built against the exact coordinated Legacy core. All thirteen real YAML/filesystem tests passed with no failures, errors or skips. Existing strict compiler flags and Java 21 bytecode checks passed. Evidence artifact 11135822494 matched SHA-256 80c4f498bbf7c45afac83117d37e51383a96faaa2d1d411d452b920810d5fd3e; both exported source blobs were independently inspected and hash-checked.

The same guarded JAR was used for every runtime lane: SHA-256 0f01cd9d9ac84abc4e0a87a92d05767fdc35f372e990115fc77c1ef72d504ec5. Original addon SHA-256 9d68be9097f69d7f03bce624934d2027e4af3c73efc0d2268500c058769d4ad4; exact core SHA-256 ffbdfcbf0eab274eaaf56916f8ddc3f12347e76a7b07fa4b8d8fe25eabac7ace.

## Actual old-addon and failed-load server tests

Runtime run 36802462865 passed ten cycles each on Paper 1.21.11 build 132 / Java 21, Paper 26.2 build 129 / Java 25, and Paper 26.3 beta 140 / Java 25:

- Original-addon controls reproduce malformed player_stats.yml and spells.yml being overwritten.
- The corrected addon refuses malformed player_stats.yml, spells.yml, blocks.yml, generic-stories.yml and block_colors.yml. The source bytes and existing progress/settings files remain intact; no addon items, listeners or scheduled work remain active and shutdown does not save an incomplete configuration.
- Valid state seeded through original spell/story APIs survives an upgrade and two corrected starts. Exact UUID, HEAL unlock and large cast count, chronicle/realisation counts, gilded state and opaque LONG/text values remain intact. An owner's disabled HEAL setting remains disabled both in the file and at runtime. Unknown settings are preserved.
- player_stats.yml and spells.yml are byte-identical across the original seed and both corrected saves within each server version.

Downloaded positive runtime artifacts were inspected, not just their green workflow labels:

| Version | Artifact | SHA-256 |
| --- | --- | --- |
| 1.21.11 | 11136194686 | 389594ce57e3b592df8d0da334540c4a7c88e9319340bb0a53d0b128370ace44 |
| 26.2 | 11135653352 | f706bf2888b67af0d58d6aa73d50d264b212d2580a1a51600dd0b79dc0c34b78 |
| 26.3 | 11135723372 | 0868ab1be3532e94aa329575a8b23a3f4ff80b2c473b61064f952a9552e9ff0f |

These are generated old-addon fixtures, not captured player-world certification, complete spell gameplay coverage or absolute power-loss durability. Initial fixture compilation errors were fixed to call the actual unchanged PlayerStatistics API; no production API was invented or altered to satisfy the tests. Temporary workflows and runtime fixtures remain outside the production patch. Normal PR checks and the final exact-source bundle remain independent release gates. Version remains 1.0.1 and no stable release or production migration is performed here.
