package io.github.sefiraat.crystamaehistoria.stories;

import org.bukkit.Material;

import javax.annotation.Nullable;

/** Resolves historical blocks.yml keys without rewriting configuration or story IDs. */
public final class StoryMaterialResolver {

    private StoryMaterialResolver() {
    }

    @Nullable
    public static Material resolve(@Nullable String name) {
        if (name == null) {
            return null;
        }

        // An exact runtime material always takes precedence over a historical alias.
        final Material exact = Material.getMaterial(name);
        if (exact != null) {
            return exact;
        }

        // Only known renames are accepted: never guess a replacement for invalid data.
        final String replacement = switch (name) {
            case "CHAIN" -> "IRON_CHAIN";
            case "GRASS" -> "SHORT_GRASS";
            case "SCUTE" -> "TURTLE_SCUTE";
            default -> null;
        };
        return replacement == null ? null : Material.getMaterial(replacement);
    }
}
