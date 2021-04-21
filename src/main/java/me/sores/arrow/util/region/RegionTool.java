package me.sores.arrow.util.region;

import me.sores.arrow.util.ArrowUtil;
import me.sores.impulse.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by sores on 4/21/2021.
 */
public enum RegionTool {

    EDITOR(new ItemBuilder(Material.GOLD_HOE).setName("&cRegion Selector").setLore(Arrays.asList(ArrowUtil.BOUND_LORE)).build());

    private ItemStack item;

    RegionTool(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

}
