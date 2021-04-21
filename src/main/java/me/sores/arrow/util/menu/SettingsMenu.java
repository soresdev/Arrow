package me.sores.arrow.util.menu;

import me.sores.arrow.util.enumerations.HealingItem;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.theme.menu.ThemesMenu;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.menu.Button;
import me.sores.impulse.util.menu.Menu;
import me.sores.impulse.util.menu.buttons.JumpToMenuButton;
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
public class SettingsMenu extends Menu {

    private Player player;
    private ArrowProfile profile;

    public SettingsMenu(Player player, ArrowProfile profile) {
        this.player = player;
        this.profile = profile;

        setAutoUpdate(true);
        setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return "&6Your Settings: ";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for(int i = 0; i < 54; i++){
            buttons.put(i, getPlaceholderButton());
        }

        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.MAP).setName("&bScoreboard").setLore(Arrays.asList(
                        StringUtil.color(" "),
                        StringUtil.color("&7Click to toggle your scoreboard on/off."),
                        StringUtil.color(" "),
                        StringUtil.color("&7Current Setting: " + (profile.isScoreboard() ? "&aEnabled" : "&cDisabled"))
                )).build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                if(profile.isScoreboard()){
                    profile.hideScoreboard();
                }else{
                    profile.showScoreboard();
                }

                MessageUtil.message(player, "&7You have " + (profile.isScoreboard() ? "&aenabled " : "&cdisabled ") + "&7your scoreboard.");
            }

            @Override
            public boolean shouldUpdate(Player player, ClickType clickType) {
                return true;
            }
        });

        buttons.put(13, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                if(profile.getHealingItem() == HealingItem.POTION){
                    return new ItemBuilder(Material.POTION).setData((short) 16421).setName("&bHealing Item").setLore(Arrays.asList(
                            StringUtil.color(" "),
                            StringUtil.color("&7Click to change your Healing Item."),
                            StringUtil.color(" "),
                            StringUtil.color("&7Current Setting: &a" + profile.getHealingItem().toString())
                    )).build();
                }
                return new ItemBuilder(Material.MUSHROOM_SOUP).setName("&bHealing Item").setLore(Arrays.asList(
                        StringUtil.color(" "),
                        StringUtil.color("&7Click to change your Healing Item."),
                        StringUtil.color(" "),
                        StringUtil.color("&7Current Setting: &a" + profile.getHealingItem().toString())
                )).build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                HealingItem item = profile.getHealingItem();

                if(item == HealingItem.SOUP){
                    profile.setHealingItem(HealingItem.POTION);
                }else{
                    profile.setHealingItem(HealingItem.SOUP);
                }

                MessageUtil.message(player, "&7You have set your Healing Item to &a" + profile.getHealingItem().toString() + "&7.");
            }

            @Override
            public boolean shouldUpdate(Player player, ClickType clickType) {
                return true;
            }
        });

        buttons.put(15, new JumpToMenuButton(new ThemesMenu(player, profile), new ItemBuilder(Material.PAPER).setName("&bTheme").setLore(Arrays.asList(
                StringUtil.color(" "),
                StringUtil.color("&7Click to change your theme."),
                StringUtil.color(" "),
                StringUtil.color("&7Current Theme: &a" + profile.getSelectedTheme().getPrimary() + profile.getSelectedTheme().getIndex())
        )).build()));

        buttons.put(31, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.REDSTONE).setName("&bBlood Effect").setLore(Arrays.asList(
                        StringUtil.color(" "),
                        StringUtil.color("&7Click to toggle your Blood Effect on/off."),
                        StringUtil.color(" "),
                        StringUtil.color("&7Current Setting: " + (profile.isBloodEffect() ? "&aEnabled" : "&cDisabled"))
                )).build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                profile.setBloodEffect(!profile.isBloodEffect());

                MessageUtil.message(player, "&7You have " + (profile.isBloodEffect() ? "&aenabled " : "&cdisabled ") + "&7your Blood Effect.");
            }

            @Override
            public boolean shouldUpdate(Player player, ClickType clickType) {
                return true;
            }
        });

        return buttons;
    }

}
