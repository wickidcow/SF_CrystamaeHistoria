# CrystamaeHistoria preservation contract

Target Minecraft/Paper 1.21.11+ with Java 21 bytecode; preserve the strict existing compiler gates. Do not suppress new deprecation/unchecked warnings or drop compatibility shims without evidence.

Keep item/research/spell IDs, player UUIDs, progress keys/counts, enabled/disabled settings, owner configuration, PDC types, storage identity, recipes and spell behavior. A failed data read must never become an empty progress file or default-enabled spell list. Load completely before registering items/listeners/tasks; handle partial shutdown without touching persisted state.

Run the source guard, all Maven tests, baseline/newer API builds, and disposable old-addon upgrade/restart fixtures. Preserve historical readers and unknown keys. Staged writes are best effort, not a full power-loss or concurrent-writer guarantee. Test dependencies must not enter the plugin JAR. Keep existing release versions unchanged until coordinated release approval and preserve concurrent branches.
