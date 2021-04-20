package me.sores.arrow.kit;

import org.bukkit.inventory.ItemStack;

/**
 * Created by sores on 4/20/2021.
 */
public class Loadout {

    private ItemStack[] armor, contents;

    public Loadout(ItemStack[] armor, ItemStack[] contents) {
        this.armor = armor;
        this.contents = contents;
    }

    public Loadout() {
        armor = new ItemStack[4];
        contents = new ItemStack[36];
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public void setArmor(ItemStack[] armor) {
        this.armor = armor;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }

}
