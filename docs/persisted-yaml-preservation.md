# Persisted YAML preservation

The previous ConfigManager used a forgiving YAML load, then attempted a second load whose failure was only printed. An unreadable player_stats.yml could therefore become empty progress and be saved on shutdown; malformed spells.yml could lose disabled values before default initialization.

All five managed files now use a strict UTF-8/YAML reader, with intentional creation only for genuinely absent fresh files. Existing corrupt and dangling-symlink files are not treated as empty new data. Resource defaults are strictly parsed using a closed UTF-8 reader. Failed initialization stops before registering listeners, tasks or items; partial shutdown avoids initializing caches or dereferencing absent services.

Normal managed-file saves serialize completely into a staged sibling, flush bytes and replace atomically where supported. Existing file symlinks and POSIX modes remain intact. The fallback applies only when atomic movement is unsupported. No full disk/power-failure or concurrent external-writer guarantee is claimed. InfinityLib's separate main configuration implementation is unchanged.

Thirteen YAML/filesystem regressions protect failures, exact progress/settings values, large counts, unknown keys, symlinks and staged replacement. The normal baseline build executes them under the existing strict compiler flags. Item and research IDs, spell definitions, counters/formulas, recipes and runtime behavior remain untouched. Actual old-addon progress and bad-file startup require independent runtime validation before release.
