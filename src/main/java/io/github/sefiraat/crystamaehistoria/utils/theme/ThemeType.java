package io.github.sefiraat.crystamaehistoria.utils.theme;

import io.github.sefiraat.crystamaehistoria.stories.definition.StoryRarity;
import io.github.sefiraat.crystamaehistoria.stories.definition.StoryType;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.Getter;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public enum ThemeType {
    WARNING("#FFFF55", "Warning"),
    ERROR("#FF5555", "Error"),
    NOTICE("#FFFFFF", "Notice"),
    PASSIVE("#AAAAAA", ""),
    SUCCESS("#55FF55", "Success"),
    MAIN("#21588f", "Crystamae Historia"),
    CLICK_INFO("#e4ed32", "Click here"),
    RESEARCH("#a60e03", "Research"),
    CRAFTING("#dbcea9", "Crafting Material"),
    CRYSTAL("#dbcea9", "Crystal"),
    MACHINE("#3295a8", "Machine"),
    MECHANISM("#3295a8", "Mechanism"),
    GADGET("#8732a8", "Gadget"),
    EXALTED("#8732a8", "Exalted"),
    GUIDE("#444444", "Guide"),
    CHEST("#b89b1c", "Chest"),
    DROP("#bf307f", "Rare Drop"),
    BASE("#9e9e9e", "Base Resource"),
    MOLTEN_METAL("#21588f", "Molten Metal"),
    LIQUID("#65dbb4", "Liquid"),
    CAST("#ffe138", "Cast"),
    PART("#42c8f5", "Part"),
    TOOL("#c2fc03", "Tool"),
    STAVE("#c2fc03", "Stave"),
    ARMOUR("#c2fc03", "Armour"),
    INFO("#21588f", "Information"),
    MOD("#bf307f", "Modification"),
    PROP("#bf307f", "Material Trait"),
    SPELL("#bf307f", "Spell"),
    RUNE("#32a852", "Rune"),
    MULTIBLOCK("#ba12af", "Multiblock"),
    RARITY_COMMON("#dbdbdb", "Common"),
    RARITY_UNCOMMON("#97d16b", "Uncommon"),
    RARITY_RARE("#d1db5c", "Rare"),
    RARITY_EPIC("#b355d9", "Epic"),
    RARITY_MYTHICAL("#c42336", "Mythical"),
    RARITY_UNIQUE("#b35f12", "Unique"),
    TYPE_ELEMENTAL("#ba0000", "Elemental"),
    TYPE_MECHANICAL("#ba5d00", "Mechanical"),
    TYPE_ALCHEMICAL("#e5e81a", "Alchemical"),
    TYPE_HISTORICAL("#24e81a", "Historical"),
    TYPE_HUMAN("#201ae8", "Human"),
    TYPE_ANIMAL("#701ae8", "Animal"),
    TYPE_CELESTIAL("#ffffff", "Celestial"),
    TYPE_VOID("#222222", "Void"),
    TYPE_PHILOSOPHICAL("#4d4aa8", "Philosophical"),
    RANK_SPELL_APPRENTICE("#cdbfff", "Apprentice"),
    RANK_SPELL_MAGE("#b5a1ff", "Mage"),
    RANK_SPELL_WIZARD("#9d82ff", "Wizard"),
    RANK_SPELL_CONJURER("#8969ff", "Conjurer"),
    RANK_SPELL_SORCERER("#6f47ff", "Sorcerer"),
    RANK_SPELL_MAGI("#5729ff", "Magi"),
    RANK_SPELL_MASTER_MAGI("#3d08ff", "Master Magi"),
    RANK_SPELL_GRANDMASTER_MAGI("#6b08ff", "Grandmaster Magi"),
    RANK_STORY_PUPIL("#eeffa8", "Pupil"),
    RANK_STORY_STUDENT("#e7ff82", "Student"),
    RANK_STORY_RESEARCHER("#e0ff5e", "Researcher"),
    RANK_STORY_READER("#d8ff33", "Reader"),
    RANK_STORY_LECTURER("#ceff00", "Lecturer"),
    RANK_STORY_PROFESSOR("#99ff00", "Professor"),
    RANK_STORY_ADJUNCT_PROFESSOR("#6aff00", "Adjunct Professor"),
    RANK_STORY_EMERITUS_PROFESSOR("#33ff00", "Emeritus Professor"),
    RANK_BLOCK_UNKNOWN("#a8ffb1", "Unknown"),
    RANK_BLOCK_HEARD_OF("#87ff94", "Heard-of"),
    RANK_BLOCK_KNOWN("#66ff77", "Known"),
    RANK_BLOCK_DETAILED("#4dff60", "Detailed"),
    RANK_BLOCK_RESEARCHED("#29ff40", "Researched"),
    RANK_BLOCK_EXPERT_OF("#0fff29", "Expert-of"),
    RANK_BLOCK_MASTER_OF("#00db18", "Master-of"),
    RANK_BLOCK_SME("#00820e", "S.M.E."),
    RANK_GILDING_NOVICE("#a8ffb1", "Novice"),
    RANK_GILDING_MEMBER("#87ff94", "Member"),
    RANK_GILDING_SECRETARY("#66ff77", "Secretary"),
    RANK_GILDING_OFFICER("#4dff60", "Officer"),
    RANK_GILDING_EXECUTIVE("#29ff40", "Executive"),
    RANK_GILDING_CHIEF("#0fff29", "Chief"),
    RANK_GILDING_MANAGER("#00db18", "Manager"),
    RANK_GILDING_OWNER("#00820e", "Owner");

    /**
     * List of names to be given to ArmourStands, invisible but mods and Minimaps can see them :)
     */
    @Nonnull
    private static final List<String> EGG_NAMES = Arrays.asList(
        "TheBusyBiscuit",
        "Alessio",
        "Walshy",
        "Jeff",
        "Seggan",
        "BOOMER_1",
        "svr333",
        "variananora",
        "ProfElements",
        "Riley",
        "FluffyBear",
        "GallowsDove",
        "Apeiros",
        "Martin",
        "Bunnky",
        "ReasonFoundDecoy",
        "Oah",
        "Azak",
        "andrewandy",
        "EpicPlayer10",
        "GentlemanCheesy",
        "ybw0014",
        "Ashian",
        "R.I.P",
        "OOOOMAGAAA",
        "TerslenK",
        "FN_FAL",
        "supertechxter"
    );

    @Getter
    private static final ThemeType[] cachedValues = values();
    private final String color;
    private final TextColor componentColor;
    private final String loreLine;

    ThemeType(String hexColor, String loreLine) {
        this.color = toLegacyColor(hexColor);
        this.componentColor = TextColor.color(Integer.parseInt(hexColor.substring(1), 16));
        this.loreLine = loreLine;
    }

    /**
     * Gets a SlimefunItemStack with a pre-populated lore and name with themed colors.
     *
     * @param id        The ID for the new {@link SlimefunItemStack}
     * @param itemStack The vanilla {@link ItemStack} used to base the {@link SlimefunItemStack} on
     * @param themeType The {@link ThemeType} {@link ChatColor} to apply to the {@link SlimefunItemStack} name
     * @param name      The name to apply to the {@link SlimefunItemStack}
     * @param lore      The lore lines for the {@link SlimefunItemStack}. Lore is book-ended with empty strings.
     * @return Returns the new {@link SlimefunItemStack}
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public static SlimefunItemStack themedSlimefunItemStack(String id,
                                                            ItemStack itemStack,
                                                            ThemeType themeType,
                                                            String name,
                                                            String... lore
    ) {
        ChatColor passiveColor = ThemeType.PASSIVE.getColor();
        List<String> finalLore = new ArrayList<>();
        finalLore.add("");
        for (String s : lore) {
            finalLore.add(passiveColor + s);
        }
        finalLore.add("");
        finalLore.add(applyThemeToString(ThemeType.CLICK_INFO, themeType.getLoreLine()));
        return new SlimefunItemStack(
            id,
            itemStack,
            ThemeType.applyThemeToString(themeType, name),
            finalLore.toArray(new String[finalLore.size() - 1])
        );
    }

    /**
     * Applies the theme color to a given string
     *
     * @param themeType The {@link ThemeType} to apply the color from
     * @param string    The string to apply the color to
     * @return Returns the string provides preceded by the color
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public static String applyThemeToString(ThemeType themeType, String string) {
        return themeType.getColor() + string;
    }

    /**
     * Gets an ItemStack with a pre-populated lore and name with themed colors.
     *
     * @param material  The {@link Material} used to base the {@link ItemStack} on
     * @param themeType The {@link ThemeType} {@link ChatColor} to apply to the {@link ItemStack} name
     * @param name      The name to apply to the {@link ItemStack}
     * @param lore      The lore lines for the {@link ItemStack}. Lore is book-ended with empty strings.
     * @return Returns the new {@link ItemStack}
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public static ItemStack themedItemStack(Material material, ThemeType themeType, String name, String... lore) {
        ChatColor passiveColor = ThemeType.PASSIVE.getColor();
        List<String> finalLore = new ArrayList<>();
        finalLore.add("");
        for (String s : lore) {
            finalLore.add(passiveColor + s);
        }
        finalLore.add("");
        finalLore.add(applyThemeToString(ThemeType.CLICK_INFO, themeType.getLoreLine()));
        return new CustomItemStack(
            material,
            ThemeType.applyThemeToString(themeType, name),
            finalLore.toArray(new String[finalLore.size() - 1])
        );
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static ThemeType getByRarity(StoryRarity storyRarity) {
        switch (storyRarity) {
            case COMMON:
                return RARITY_COMMON;
            case UNCOMMON:
                return RARITY_UNCOMMON;
            case RARE:
                return RARITY_RARE;
            case EPIC:
                return RARITY_EPIC;
            case MYTHICAL:
                return RARITY_MYTHICAL;
            case UNIQUE:
                return RARITY_UNIQUE;
            default:
                throw new IllegalStateException("Unexpected value: " + storyRarity);
        }
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static ThemeType getByType(StoryType storyType) {
        switch (storyType) {
            case ELEMENTAL:
                return TYPE_ELEMENTAL;
            case MECHANICAL:
                return TYPE_MECHANICAL;
            case ALCHEMICAL:
                return TYPE_ALCHEMICAL;
            case HISTORICAL:
                return TYPE_HISTORICAL;
            case HUMAN:
                return TYPE_HUMAN;
            case ANIMAL:
                return TYPE_ANIMAL;
            case CELESTIAL:
                return TYPE_CELESTIAL;
            case VOID:
                return TYPE_VOID;
            case PHILOSOPHICAL:
                return TYPE_PHILOSOPHICAL;
            default:
                throw new IllegalStateException("Unexpected value: " + storyType);
        }
    }

    @Nonnull
    public static String getRandomEggName() {
        int rnd = ThreadLocalRandom.current().nextInt(0, EGG_NAMES.size());
        return EGG_NAMES.get(rnd);
    }

    @Nonnull
    public static List<String> getEggNames() {
        return EGG_NAMES;
    }

    @Nonnull
    public Particle.DustOptions getDustOptions(float size) {
        return new Particle.DustOptions(
            Color.fromRGB(
                componentColor.red(),
                componentColor.green(),
                componentColor.blue()
            ),
            size
        );
    }

    @Nonnull
    public TextColor getComponentColor() {
        return componentColor;
    }

    private static String toLegacyColor(String hexColor) {
        final String digits = hexColor.substring(1);
        final StringBuilder builder = new StringBuilder("§x");
        for (int i = 0; i < digits.length(); i++) {
            builder.append('§').append(digits.charAt(i));
        }
        return builder.toString();
    }

}
