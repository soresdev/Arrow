package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IItemDrop extends WrapperItem {

    void onItemDrop(Kit kit, PlayerDropItemEvent event);

}
