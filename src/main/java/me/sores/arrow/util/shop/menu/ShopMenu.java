package me.sores.arrow.util.shop.menu;

import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.shop.ShopItems;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.menu.Button;
import me.sores.impulse.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sores on 4/24/2021.
 */
public class ShopMenu extends Menu {

    private Player player;
    private ArrowProfile profile;

    public ShopMenu(Player player, ArrowProfile profile) {
        this.player = player;
        this.profile = profile;

        setAutoUpdate(true);
        setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return "&6Shop: ";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        for(ShopItems item : ShopItems.values()){
            buttons.put(index, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return item.getItem();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {
                    item.interact(player, profile);
                }
            });

            index++;
        }

        buttons.put(26, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.INK_SACK).setData((short) 1).setName("&cClose").build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                player.closeInventory();
            }
        });

        return buttons;
    }

}
