package me.sores.arrow.util.prefixes.menu;

import me.sores.arrow.config.ArrowConfig;
import me.sores.arrow.util.prefixes.ChatPrefix;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.menu.Button;
import me.sores.impulse.util.menu.pagination.PaginatedMenu;
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
public class PrefixMenu extends PaginatedMenu {

    private Player player;
    private ArrowProfile profile;
    private ChatPrefix.Prefix selected;

    public PrefixMenu(Player player, ArrowProfile profile) {
        this.player = player;
        this.profile = profile;
        if(profile.getSelectedPrefix() != null) this.selected = profile.getSelectedPrefix();

        setAutoUpdate(true);
        setUpdateAfterClick(true);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&aSelect your prefix";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 1;

        for(ChatPrefix.Prefix prefix : ArrowConfig.prefixes){
            buttons.put(index, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(prefix.getData().getMaterial()).setData(prefix.getData().getData()).setName("&7[&r" + prefix.getDisplay() + "&7]" +
                            (prefix == profile.getSelectedPrefix() ? " &7(Selected)" : "")).setLore(Arrays.asList(
                            " ",
                            StringUtil.color(prefix.has(player) ? "&aYou have access to this prefix." : "&cYou do not have access to this prefix.")
                    )).build();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {
                    if(!prefix.has(player)){
                        MessageUtil.message(player, "&cYou do not have permission to use that prefix.");
                        return;
                    }

                    setSelected(prefix);
                    new PrefixColorMenu(player, profile).openMenu(player);
                }
            });

            buttons.put(0, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.FENCE).setName("&cClear Prefix").build();
                }

                @Override
                public void clicked(Player player, ClickType clickType) {
                    if(selected == null){
                        MessageUtil.message(player, "&cYou have no active prefix.");
                        player.closeInventory();
                        return;
                    }

                    setSelected(null);

                    MessageUtil.message(player, "&aYou have cleared your active prefix.");
                    player.closeInventory();
                }
            });

            index++;
        }

        return buttons;
    }

    private void setSelected(ChatPrefix.Prefix selected) {
        this.selected = selected;
        if(selected == null){
            profile.setSelectedPrefix(null);
            profile.setSelectedPrefixColor(null);
        }else{
            if(selected.has(player)){
                selected.apply(profile);
            }else{
                MessageUtil.message(player, "&cYou do not have permission to use that prefix.");
            }
        }
    }

}
