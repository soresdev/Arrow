package me.sores.arrow.util.enumerations;

import me.sores.impulse.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by sores on 4/20/2021.
 */
public enum HealingItem {

    SOUP(new ItemBuilder(Material.MUSHROOM_SOUP).setName("&6Soup").build()),
    POTION(new ItemBuilder(Material.POTION).setName("&6Potion").setData((short) 16421).build());

    ItemStack item;

    HealingItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

}
