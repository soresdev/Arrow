package me.sores.arrow.util.chatcolors.menu;

import me.sores.arrow.util.chatcolors.ArrowChatColor;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.menu.Button;
import me.sores.impulse.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sores on 4/22/2021.
 */
public class ColorMenu extends Menu {

    private Player player;
    private ArrowProfile profile;
    private ArrowChatColor selected;

    public ColorMenu(Player player, ArrowProfile profile) {
        this.player = player;
        this.profile = profile;
        this.selected = profile.getSelectedChatColor();

        setAutoUpdate(true);
        setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return "&aSelect a chat color";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        for(ArrowChatColor color : ArrowChatColor.values()){
            buttons.put(index, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(color.getData().getMaterial()).setData(color.getData().getData()).setName("&7[&r" + color.getColor() + color.getDisplay() + "&7]" +
                            (color == selected ? " &7(Selected)" : "")).setLore(Arrays.asList(
                            " ",
                            StringUtil.color(color.has(player) ? "&aYou have access to this color." : "&cYou do not have access to this color."))).build();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {
                    if(!color.has(player)){
                        MessageUtil.message(player, "&cYou do not have permission to use that chat color.");
                        return;
                    }

                    if(color == selected){
                        MessageUtil.message(player, "&cYou already have that chat color selected.");
                        return;
                    }

                    setSelected(color);
                    MessageUtil.message(player, "&7You have selected the " + color.getColor() + color.getDisplay() + " &7chat color.");
                    player.closeInventory();
                }
            });

            index++;
        }

        for(int i = 18; i < 26; i++){
            buttons.put(i, getPlaceholderButton());
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

    private void setSelected(ArrowChatColor selected) {
        this.selected = selected;
        selected.apply(profile);
    }
    
}
