package me.sores.arrow.util.shop;

import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.EconUtil;
import me.sores.arrow.util.enumerations.HealingItem;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by sores on 4/24/2021.
 */
public enum ShopItems {

    REFILL("&aHealing Item Refill", new ItemBuilder(Material.MUSHROOM_SOUP).setName("&bHealing Item Refill").setLore(Arrays.asList(
            " ",
            StringUtil.color("&7Buying this will refill your inventory with "),
            StringUtil.color("&716 of your selected Healing Item."),
            StringUtil.color("&rCost: &6" + 10)

    )).build(), 10){
        @Override
        public void interact(Player player, ArrowProfile profile) {
            ItemStack soup = new ItemBuilder(Material.MUSHROOM_SOUP).setName(StringUtil.color("&6Soup")).build();
            ItemStack pots = new ItemBuilder(Material.POTION).setName(StringUtil.color("&6Potion")).setData((short) 16421).build();

            for(int i = 0; i < 16; i++){
                player.getInventory().addItem(profile.getHealingItem() == HealingItem.SOUP ? soup : pots);
            }
        }
    },

    REPAIR("&aArmor Repair", new ItemBuilder(Material.ANVIL).setName("&bArmor Repair").setLore(Arrays.asList(
            " ",
            StringUtil.color("&7Buying this item will repair your armor to "),
            StringUtil.color("&7it's full durability."),
            StringUtil.color("&rCost: &6" + 15)
    )).build(), 15){
        @Override
        public void interact(Player player, ArrowProfile profile) {
            if(!canAfford(profile)){
                player.sendMessage(StringUtil.color("&cYou cannot afford this item."));
                return;
            }

            ArrowUtil.repairPlayerArmor(player);
            purchase(player, profile);
            player.closeInventory();
        }
    },

    ANTI_SWITCHER("&aAnti Switcher", new ItemBuilder(Material.SNOW_BALL).setName("&bAnti Switcher").setLore(Arrays.asList(
            " ",
            StringUtil.color("&7The Switcher Ability will no longer work "),
            StringUtil.color("&7against you after purchasing this."),
            StringUtil.color("&rCost: &6" + 20),
            " ",
            StringUtil.color("&cThis will last for your current life.")
    )).build(), 20){
        @Override
        public void interact(Player player, ArrowProfile profile) {
            if(!canAfford(profile)){
                player.sendMessage(StringUtil.color("&cYou cannot afford this item."));
                return;
            }

            if(profile.hasShopItem(this)){
                player.sendMessage(StringUtil.color("&cYou already have this item."));
                return;
            }

            profile.addShopItem(this);
            purchase(player, profile);
            player.closeInventory();
        }
    },

    ANTI_FISHERMAN("&aAnti Fisherman", new ItemBuilder(Material.FISHING_ROD).setName("&bAnti Fisherman").setLore(Arrays.asList(
            " ",
            StringUtil.color("&7The Fisherman Ability will no longer work "),
            StringUtil.color("&7against you after purchasing this."),
            StringUtil.color("&rCost: &6" + 20),
            " ",
            StringUtil.color("&cThis will last for your current life.")
    )).build(), 20){
        @Override
        public void interact(Player player, ArrowProfile profile) {
            if(!canAfford(profile)){
                player.sendMessage(StringUtil.color("&cYou cannot afford this item."));
                return;
            }

            if(profile.hasShopItem(this)){
                player.sendMessage(StringUtil.color("&cYou already have this item."));
                return;
            }

            profile.addShopItem(this);
            purchase(player, profile);
            player.closeInventory();
        }
    };

    private final String display;
    private final ItemStack item;
    private final int cost;

    ShopItems(String display, ItemStack item, int cost) {
        this.display = display;
        this.item = item;
        this.cost = cost;
    }

    public abstract void interact(Player player, ArrowProfile profile);

    public void purchase(Player player, ArrowProfile profile){
        EconUtil.takeCoins(profile, getCost());
        MessageUtil.message(player, "&7You have purchased the " + getDisplay() + " &7item from the shop.");
    }

    public boolean canAfford(ArrowProfile profile){
        return profile.getCoins() >= getCost();
    }

    public String getDisplay() {
        return display;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getCost() {
        return cost;
    }

}
