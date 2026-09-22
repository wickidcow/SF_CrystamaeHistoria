package io.github.sefiraat.crystamaehistoria.slimefun.items.tools.covers;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class BlockVeil extends SlimefunItem {

    private final List<Class<? extends SlimefunItem>> classesToCover;
    private final List<String> itemIdsToCover;

    @ParametersAreNonnullByDefault
    public BlockVeil(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput, List<Class<? extends SlimefunItem>> classesToCover) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
        this.classesToCover = List.copyOf(classesToCover);
        this.itemIdsToCover = List.of();
    }

    @ParametersAreNonnullByDefault
    public BlockVeil(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput, List<String> itemIdsToCover, boolean byItemId) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
        this.classesToCover = List.of();
        this.itemIdsToCover = List.copyOf(itemIdsToCover);
    }

    @Override
    public void preRegister() {
        addItemHandler(onItemUse());
    }

    private ItemUseHandler onItemUse() {
        return e -> {
            e.cancel();
            if (e.getClickedBlock().isPresent()) {
                Block block = e.getClickedBlock().get();
                SlimefunItem slimefunItem = StorageCacheUtils.getSlimefunItem(block.getLocation());
                ItemStack offhand = e.getPlayer().getInventory().getItemInOffHand();

                if (slimefunItem == null) {
                    return;
                }

                for (Class<?> testClass : this.classesToCover) {
                    if (testClass.isInstance(slimefunItem)) {
                        applyCover(e.getItem(), offhand, block);
                        return;
                    }
                }

                for (String itemId : this.itemIdsToCover) {
                    if (itemId.equals(slimefunItem.getId())) {
                        applyCover(e.getItem(), offhand, block);
                        return;
                    }
                }
            }
        };
    }

    @ParametersAreNonnullByDefault
    private void applyCover(ItemStack coverItem, ItemStack offhand, Block block) {
        if (offhand.getType() != Material.AIR
            && offhand.getType().isBlock()
            && materialIsValid(offhand.getType())
        ) {
            block.setType(offhand.getType());
            coverItem.setAmount(coverItem.getAmount() - 1);
        }
    }

    @ParametersAreNonnullByDefault
    public boolean materialIsValid(Material material) {
        return material != Material.SPAWNER
            && material.getHardness() != -1
            && material.isSolid()
            && material.isOccluding();
    }
}
