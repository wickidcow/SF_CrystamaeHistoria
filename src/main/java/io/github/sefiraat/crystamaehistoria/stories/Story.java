package io.github.sefiraat.crystamaehistoria.stories;

import io.github.sefiraat.crystamaehistoria.CrystamaeHistoria;
import io.github.sefiraat.crystamaehistoria.stories.definition.StoryRarity;
import io.github.sefiraat.crystamaehistoria.stories.definition.StoryShardProfile;
import io.github.sefiraat.crystamaehistoria.stories.definition.StoryType;
import io.github.sefiraat.crystamaehistoria.utils.theme.ThemeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.blocks.BlockPosition;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Story {

    @Nonnull
    private final String id;
    @Nullable
    private final String author;
    @Nullable
    private final String sponsor;
    @Nonnull
    private final StoryRarity rarity;
    @Nonnull
    private final StoryType type;
    @Nonnull
    private final StoryShardProfile storyShardProfile;
    @Nonnull
    private final List<String> storyStrings;
    @Setter
    @Nullable
    private BlockPosition blockPosition;
    @Setter
    private boolean gilded = false;

    /**
     * @noinspection ConstantConditions
     */
    @ParametersAreNonnullByDefault
    public Story(ConfigurationSection section, StoryRarity storyRarity) {
        final List<Integer> shards = section.getIntegerList("shards");

        this.id = section.getString("name");

        StoryType storyType = StoryType.getByName(section.getString("type"));

        if (shards.size() != 9) {
            CrystamaeHistoria.getInstance().getLogger().warning(
                MessageFormat.format("The following story does not have a correctly setup shard profile: {0}", this.id)
            );
        }

        if (storyType == null) {
            CrystamaeHistoria.getInstance().getLogger().warning(
                MessageFormat.format("A block story has a badly typed element -> {0}", this.id)
            );
        }

        this.rarity = storyRarity;
        this.type = storyType;
        this.storyShardProfile = new StoryShardProfile(section.getIntegerList("shards"));
        this.storyStrings = section.getStringList("lore");
        this.author = section.getString("author");
        this.sponsor = section.getString("sponsor");
    }

    @ParametersAreNonnullByDefault
    private Story(Story story) {
        this.rarity = story.rarity;
        this.id = story.getId();
        this.type = story.type;
        this.storyShardProfile = story.getStoryShardProfile();
        this.storyStrings = story.storyStrings;
        this.author = story.author;
        this.sponsor = story.sponsor;
        this.blockPosition = story.blockPosition;
        this.gilded = story.gilded;
    }

    public String getDisplayName() {
        return ThemeType.getByRarity(this.rarity).getColor()
            + "§l"
            + getDisplayRarity()
            + ThemeType.CLICK_INFO.getColor()
            + this.id;
    }

    public String getDisplayRarity() {
        return "[" + rarity + "] ";
    }

    public List<String> getStoryLore() {
        final String passive = ThemeType.PASSIVE.getColor();
        final List<String> l = new ArrayList<>();

        for (String s : storyStrings) {
            l.add(passive + s);
        }
        if (author != null) {
            l.add("");
            l.add(ThemeType.PASSIVE.getColor() + "Author: " + author);
        }
        if (sponsor != null) {
            l.add("");
            l.add(ThemeType.PASSIVE.getColor() + "Sponsor: " + sponsor);
        }
        return l;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Story) {
            Story story = (Story) obj;
            return this.id.equals(story.id)
                && this.rarity == story.rarity
                && this.type == story.type;
        } else {
            return false;
        }
    }

    public Story copy() {
        return new Story(this);
    }
}
