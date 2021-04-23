package me.sores.arrow.util.killstreaks.menu;

import me.sores.arrow.util.killstreaks.KillstreakTier;
import me.sores.arrow.util.killstreaks.KillstreakType;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.menu.Button;
import me.sores.impulse.util.menu.Menu;
import me.sores.impulse.util.menu.buttons.BackButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sores on 4/22/2021.
 */
public class KillstreakTierMenu extends Menu {
    
    private Player player;
    private ArrowProfile profile;
    private KillstreakTier tier;

    public KillstreakTierMenu(Player player, ArrowProfile profile, KillstreakTier tier) {
        this.player = player;
        this.profile = profile;
        this.tier = tier;

        setAutoUpdate(true);
        setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return tier.getDisplay();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        for(KillstreakType type : KillstreakType.values()){
            if(type.getTier() == tier){
                buttons.put(index, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(type.getIcon()).setName(type.getDisplay() + (profile.getSelectedStreak(tier) == type ? " &7(Selected)" : ""))
                                .setData(type.getDat()).setLore(type.getDescription()).build();
                    }

                    @Override
                    public void clicked(Player player, ClickType clickType) {
                        if(!type.has(player)){
                            player.sendMessage(StringUtil.color("&cYou do not have permission to use this killstreak."));
                            return;
                        }

                        if(type == profile.getSelectedStreak(tier)){
                            player.sendMessage(StringUtil.color("&cYou already have that streak selected."));
                            return;
                        }

                        profile.setStreakTier(tier, type);
                        player.sendMessage(StringUtil.color("&7You have selected the " + type.getDisplay() + " &7killstreak."));
                        new KillstreakMenu(player, profile).openMenu(player);
                    }
                });

                index++;
            }
        }

        buttons.put(26, new BackButton(new KillstreakMenu(player, profile)));

        return buttons;
    }
    
}
