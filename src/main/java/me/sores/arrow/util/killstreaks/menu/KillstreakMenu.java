package me.sores.arrow.util.killstreaks.menu;

import me.sores.arrow.util.killstreaks.KillstreakTier;
import me.sores.arrow.util.killstreaks.KillstreakType;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.menu.Button;
import me.sores.impulse.util.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sores on 4/22/2021.
 */
public class KillstreakMenu extends Menu {
    
    private Player player;
    private ArrowProfile profile;

    public KillstreakMenu(Player player, ArrowProfile profile) {
        this.player = player;
        this.profile = profile;
        
        setAutoUpdate(true);
        setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return "&6Killstreaks";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for(int i = 0; i < 27; i++){
            buttons.put(i, getPlaceholderButton());
        }

        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(KillstreakTier.ONE.getIcon()).setName(KillstreakTier.ONE.getDisplay()).setLore(Arrays.asList(
                        "",
                        StringUtil.color("&7Click here to select your " + KillstreakTier.ONE.getDisplay()),
                        "",
                        StringUtil.color("&7Currently Selected: " + getSelected(profile, KillstreakTier.ONE).getDisplay())
                )).build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                new KillstreakTierMenu(player, profile, KillstreakTier.ONE).openMenu(player);
            }
        });

        buttons.put(13, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(KillstreakTier.TWO.getIcon()).setName(KillstreakTier.TWO.getDisplay()).setLore(Arrays.asList(
                        "",
                        StringUtil.color("&7Click here to select your " + KillstreakTier.TWO.getDisplay()),
                        "",
                        StringUtil.color("&7Currently Selected: " + getSelected(profile, KillstreakTier.TWO).getDisplay())
                )).build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                new KillstreakTierMenu(player, profile, KillstreakTier.TWO).openMenu(player);
            }
        });

        buttons.put(15, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(KillstreakTier.THREE.getIcon()).setName(KillstreakTier.THREE.getDisplay()).setLore(Arrays.asList(
                        "",
                        StringUtil.color("&7Click here to select your " + KillstreakTier.THREE.getDisplay()),
                        "",
                        StringUtil.color("&7Currently Selected: " + getSelected(profile, KillstreakTier.THREE).getDisplay())
                )).build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                new KillstreakTierMenu(player, profile, KillstreakTier.THREE).openMenu(player);
            }
        });

        return buttons;
    }

    private KillstreakType getSelected(ArrowProfile profile, KillstreakTier tier){
        return profile.getSelectedStreak(tier);
    }
    
}
