package me.sores.arrow.util.theme.menu;

import me.sores.arrow.config.ArrowConfig;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.theme.Theme;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.menu.Button;
import me.sores.impulse.util.menu.pagination.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sores on 4/20/2021.
 */
public class ThemesMenu extends PaginatedMenu {

    private Player player;
    private ArrowProfile profile;

    public ThemesMenu(Player player, ArrowProfile profile) {
        this.player = player;
        this.profile = profile;

        setAutoUpdate(true);
        setUpdateAfterClick(true);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6Select a theme: ";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        for(Theme theme : ArrowConfig.themeList){
            buttons.put(index, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.PAPER).setName(theme.getPrimary() + theme.getIndex() + (profile.getSelectedTheme() == theme ? " &7(Selected)" : ""))
                            .setLore(Arrays.asList(
                                    StringUtil.color("&fPrimary Color: " + theme.getPrimary() + "This is the primary color."),
                                    StringUtil.color("&fSecondary Color: " + theme.getSecondary() + "This is the secondary color."),
                                    StringUtil.color("&fValue Color: " + theme.getValue() + "This is the value color.")
                            )).build();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {
                    if(theme != ArrowConfig.defaultTheme && !player.hasPermission("zeno.themes." + theme.getIndex())){
                        MessageUtil.message(player, ChatColor.RED + "You don't have permission to use this theme.");
                        player.closeInventory();
                        return;
                    }

                    if(theme == profile.getSelectedTheme()){
                        MessageUtil.message(player, ChatColor.RED + "You already have this theme selected.");
                        return;
                    }

                    profile.setSelectedTheme(theme);
                    MessageUtil.message(player, "&7You have selected the " + theme.getPrimary() + theme.getIndex() + " &7theme.");
                    player.closeInventory();
                }
            });

            index++;
        }

        return buttons;
    }

}
