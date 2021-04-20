package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.inventory.CraftItemEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IPlayerCraft extends WrapperItem {

    void onPlayerCraft(Kit kit, CraftItemEvent event);


}
