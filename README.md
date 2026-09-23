# Crystamae Historia — Slimefun Legacy

This repository is the maintained Slimefun Legacy build of **Crystamae Historia**, originally created by **Sefiraat**.

Crystamae Historia adds magical stories, crystals, mechanisms, gadgets, staves and spells while preserving the original progression and identifiers used by existing servers.

## Compatibility target

- Slimefun Legacy **4.1.58+**
- Minecraft **1.21.11 through 26.3**
- Paper, Purpur and Leaf
- Folia compatibility target
- Velocity-safe backend deployment
- Java 25 build toolchain with Java 21 release bytecode

## Release artifact

Canonical output:

`SF_CrystamaeHistoria1.0.0.jar`

GitHub Actions publishes the raw JAR artifact directly, with no ZIP wrapper. Version tags matching `v<version>` (for example `v1.0.0`) also create a GitHub Release and attach that same raw JAR.

## Velocity

Crystamae Historia is a backend Slimefun addon, not a Velocity proxy plugin. On a Velocity network, install it on each backend server that runs Slimefun Legacy. It does not need to be installed on the proxy.

## Folia

Folia support is treated as a runtime requirement, not only a metadata flag. Legacy scheduler paths have been moved onto Paper's global, region, or entity schedulers where appropriate; live-server testing is still recommended for spell/entity-heavy paths.

## Build matrix

CI compiles the same source against the maintained 1.21.11 baseline plus the 26.2 and 26.3 Paper API lines. Those builds are also the compatibility baseline used for Purpur and Leaf.

## Credits

- **Sefiraat** — original Crystamae Historia author and design
- **wickidcow** — Slimefun Legacy maintenance and modern compatibility
