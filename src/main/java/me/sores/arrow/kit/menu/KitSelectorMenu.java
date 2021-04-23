package me.sores.arrow.kit.menu;

import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.KitsHandler;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.menu.Button;
import me.sores.impulse.util.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sores on 4/20/2021.
 */
public class KitSelectorMenu extends PaginatedMenu {

    private Player player;
    private ArrowProfile profile;

    public KitSelectorMenu(Player player, ArrowProfile profile) {
        this.player = player;
        this.profile = profile;

        setAutoUpdate(true);
        setUpdateAfterClick(true);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6Kit Selector";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;
        for(Kit kit : KitsHandler.getInstance().getKits()){
            if(kit.isPub()){
                buttons.put(index, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(kit.getIcon()).setName(KitsHandler.getInstance().has(player, kit) ? "&a" + kit.getDisplayName() + " &7(Right Click to Preview)" :
                                "&c" + kit.getDisplayName() + " &7(Right Click to Preview)").build();
                    }

                    @Override
                    public void clicked(Player player, ClickType clickType) {
                        if(clickType == ClickType.RIGHT || clickType == ClickType.MIDDLE){
                            new KitPreview(player, profile, kit).openMenu(player);
                            return;
                        }

                        kit.apply(player, false);
                        player.closeInventory();
                    }
                });

                index++;
            }
        }

        return buttons;
    }

}
