package me.sores.arrow.util.enumerations;

import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.KitsHandler;
import me.sores.arrow.kit.menu.KitSelectorMenu;
import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.menu.SettingsMenu;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

/**
 * Created by sores on 4/23/2021.
 */
public enum SpawnItems {

    KIT_SELECTOR(new ItemBuilder(Material.CHEST).setName("&bKit Selector &7(Right Click)").setLore(Collections.singletonList(ArrowUtil.BOUND_LORE)).build()){
        @Override
        public void interact(Player player, ArrowProfile profile) {
            new KitSelectorMenu(player, profile).openMenu(player);
        }
    },

    PREVIOUS_KIT(new ItemBuilder(Material.WATCH).setName("&bPrevious Kit &7(%)").setLore(Collections.singletonList(ArrowUtil.BOUND_LORE)).build()){
        @Override
        public void interact(Player player, ArrowProfile profile) {
            if(profile.hasPreviousKit()){
                Kit kit = KitsHandler.getInstance().valueOf(profile.getPreviousKit());
                kit.apply(player, false);
            }
        }
    },

    SETTINGS(new ItemBuilder(Material.EYE_OF_ENDER).setName("&bSettings").setLore(Collections.singletonList(ArrowUtil.BOUND_LORE)).build()){
        @Override
        public void interact(Player player, ArrowProfile profile) {
            new SettingsMenu(player, profile).openMenu(player);
        }
    };

    private ItemStack item;

    SpawnItems(ItemStack item) {
        this.item = item;
    }

    public abstract void interact(Player player, ArrowProfile profile);

    public static void apply(Player player){
        PlayerUtil.clearInventory(player);
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        player.getInventory().setItem(0, KIT_SELECTOR.getItem());

        if(profile.hasPreviousKit()){
            Kit kit = KitsHandler.getInstance().valueOf(profile.getPreviousKit());

            if(kit != null) player.getInventory().setItem(1, clone(profile, PREVIOUS_KIT.getItem()));
        }

        player.getInventory().setItem(8, SETTINGS.getItem());
    }

    public static ItemStack clone(ArrowProfile profile, ItemStack item){
        Kit kit = KitsHandler.getInstance().valueOf(profile.getPreviousKit());
        ItemStack toReturn = item.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(meta.getDisplayName().replace("%", kit.getDisplayName()));
        toReturn.setItemMeta(meta);

        return toReturn;
    }

    public ItemStack getItem() {
        return item;
    }

}
