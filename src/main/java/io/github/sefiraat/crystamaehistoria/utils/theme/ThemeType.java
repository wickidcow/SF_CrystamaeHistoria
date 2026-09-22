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

public enum ThemeType {
    WARNING(TextColor.color(0xFFFF55), "Warning"),
    ERROR(TextColor.color(0xFF5555), "Error"),
    NOTICE(TextColor.color(0xFFFFFF), "Notice"),
    PASSIVE(TextColor.color(0xAAAAAA), ""),
    SUCCESS(TextColor.color(0x55FF55), "Success"),
    MAIN(TextColor.color(0x21588F), "Crystamae Historia"),
    CLICK_INFO(TextColor.color(0xE4ED32), "Click here"),
    RESEARCH(TextColor.color(0xA60E03), "Research"),
    CRAFTING(TextColor.color(0xDBCEA9), "Crafting Material"),
    CRYSTAL(TextColor.color(0xDBCEA9), "Crystal"),
    MACHINE(TextColor.color(0x3295A8), "Machine"),
    MECHANISM(TextColor.color(0x3295A8), "Mechanism"),
    GADGET(TextColor.color(0x8732A8), "Gadget"),
    EXALTED(TextColor.color(0x8732A8), "Exalted"),
    GUIDE(TextColor.color(0x444444), "Guide"),
    CHEST(TextColor.color(0xB89B1C), "Chest"),
    DROP(TextColor.color(0xBF307F), "Rare Drop"),
    BASE(TextColor.color(0x9E9E9E), "Base Resource"),
    MOLTEN_METAL(TextColor.color(0x21588F), "Molten Metal"),
    LIQUID(TextColor.color(0x65DBB4), "Liquid"),
    CAST(TextColor.color(0xFFE138), "Cast"),
    PART(TextColor.color(0x42C8F5), "Part"),
    TOOL(TextColor.color(0xC2FC03), "Tool"),
    STAVE(TextColor.color(0xC2FC03), "Stave"),
    ARMOUR(TextColor.color(0xC2FC03), "Armour"),
    INFO(TextColor.color(0x21588F), "Information"),
    MOD(TextColor.color(0xBF307F), "Modification"),
    PROP(TextColor.color(0xBF307F), "Material Trait"),
    SPELL(TextColor.color(0xBF307F), "Spell"),
    RUNE(TextColor.color(0x32A852), "Rune"),
    MULTIBLOCK(TextColor.color(0xBA12AF), "Multiblock"),
    RARITY_COMMON(TextColor.color(0xDBDBDB), "Common"),
    RARITY_UNCOMMON(TextColor.color(0x97D16B), "Uncommon"),
    RARITY_RARE(TextColor.color(0xD1DB5C), "Rare"),
    RARITY_EPIC(TextColor.color(0xB355D9), "Epic"),
    RARITY_MYTHICAL(TextColor.color(0xC42336), "Mythical"),
    RARITY_UNIQUE(TextColor.color(0xB35F12), "Unique"),
    TYPE_ELEMENTAL(TextColor.color(0xBA0000), "Elemental"),
    TYPE_MECHANICAL(TextColor.color(0xBA5D00), "Mechanical"),
    TYPE_ALCHEMICAL(TextColor.color(0xE5E81A), "Alchemical"),
    TYPE_HISTORICAL(TextColor.color(0x24E81A), "Historical"),
    TYPE_HUMAN(TextColor.color(0x201AE8), "Human"),
    TYPE_ANIMAL(TextColor.color(0x701AE8), "Animal"),
    TYPE_CELESTIAL(TextColor.color(0xFFFFFF), "Celestial"),
    TYPE_VOID(TextColor.color(0x222222), "Void"),
    TYPE_PHILOSOPHICAL(TextColor.color(0x4D4AA8), "Philosophical"),
    RANK_SPELL_APPRENTICE(TextColor.color(0xCDBFFF), "Apprentice"),
    RANK_SPELL_MAGE(TextColor.color(0xB5A1FF), "Mage"),
    RANK_SPELL_WIZARD(TextColor.color(0x9D82FF), "Wizard"),
    RANK_SPELL_CONJURER(TextColor.color(0x8969FF), "Conjurer"),
    RANK_SPELL_SORCERER(TextColor.color(0x6F47FF), "Sorcerer"),
    RANK_SPELL_MAGI(TextColor.color(0x5729FF), "Magi"),
    RANK_SPELL_MASTER_MAGI(TextColor.color(0x3D08FF), "Master Magi"),
    RANK_SPELL_GRANDMASTER_MAGI(TextColor.color(0x6B08FF), "Grandmaster Magi"),
    RANK_STORY_PUPIL(TextColor.color(0xEEFFA8), "Pupil"),
    RANK_STORY_STUDENT(TextColor.color(0xE7FF82), "Student"),
    RANK_STORY_RESEARCHER(TextColor.color(0xE0FF5E), "Researcher"),
    RANK_STORY_READER(TextColor.color(0xD8FF33), "Reader"),
    RANK_STORY_LECTURER(TextColor.color(0xCEFF00), "Lecturer"),
    RANK_STORY_PROFESSOR(TextColor.color(0x99FF00), "Professor"),
    RANK_STORY_ADJUNCT_PROFESSOR(TextColor.color(0x6AFF00), "Adjunct Professor"),
    RANK_STORY_EMERITUS_PROFESSOR(TextColor.color(0x33FF00), "Emeritus Professor"),
    RANK_BLOCK_UNKNOWN(TextColor.color(0xA8FFB1), "Unknown"),
    RANK_BLOCK_HEARD_OF(TextColor.color(0x87FF94), "Heard-of"),
    RANK_BLOCK_KNOWN(TextColor.color(0x66FF77), "Known"),
    RANK_BLOCK_DETAILED(TextColor.color(0x4DFF60), "Detailed"),
    RANK_BLOCK_RESEARCHED(TextColor.color(0x29FF40), "Researched"),
    RANK_BLOCK_EXPERT_OF(TextColor.color(0x0FFF29), "Expert-of"),
    RANK_BLOCK_MASTER_OF(TextColor.color(0x00DB18), "Master-of"),
    RANK_BLOCK_SME(TextColor.color(0x00820E), "S.M.E."),
    RANK_GILDING_NOVICE(TextColor.color(0xA8FFB1), "Novice"),
    RANK_GILDING_MEMBER(TextColor.color(0x87FF94), "Member"),
    RANK_GILDING_SECRETARY(TextColor.color(0x66FF77), "Secretary"),
    RANK_GILDING_OFFICER(TextColor.color(0x4DFF60), "Officer"),
    RANK_GILDING_EXECUTIVE(TextColor.color(0x29FF40), "Executive"),
    RANK_GILDING_CHIEF(TextColor.color(0x0FFF29), "Chief"),
    RANK_GILDING_MANAGER(TextColor.color(0x00DB18), "Manager"),
    RANK_GILDING_OWNER(TextColor.color(0x00820E), "Owner");

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
    private final TextColor color;
    private final String loreLine;

    ThemeType(TextColor color, String loreLine) {
        this.color = color;
        this.loreLine = loreLine;
    }

    public String getColor() {
        return legacyColor(color);
    }

    public String getLoreLine() {
        return loreLine;
    }

    @Nonnull
    public static String legacyColor(TextColor color) {
        final String hex = String.format("%06x", color.value());
        final StringBuilder builder = new StringBuilder("§x");
        for (int i = 0; i < hex.length(); i++) {
            builder.append('§').append(hex.charAt(i));
        }
        return builder.toString();
    }

    /**
     * Gets a SlimefunItemStack with a pre-populated lore and name with themed colors.
     *
     * @param id        The ID for the new {@link SlimefunItemStack}
     * @param itemStack The vanilla {@link ItemStack} used to base the {@link SlimefunItemStack} on
     * @param themeType The {@link ThemeType} {@link TextColor} to apply to the {@link SlimefunItemStack} name
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
        String passiveColor = ThemeType.PASSIVE.getColor();
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
     * @param themeType The {@link ThemeType} {@link TextColor} to apply to the {@link ItemStack} name
     * @param name      The name to apply to the {@link ItemStack}
     * @param lore      The lore lines for the {@link ItemStack}. Lore is book-ended with empty strings.
     * @return Returns the new {@link ItemStack}
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public static ItemStack themedItemStack(Material material, ThemeType themeType, String name, String... lore) {
        String passiveColor = ThemeType.PASSIVE.getColor();
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
        return new Particle.DustOptions(Color.fromRGB(color.value()), size);
    }

    @Nonnull
    public TextColor getComponentColor() {
        return color;
    }

}
