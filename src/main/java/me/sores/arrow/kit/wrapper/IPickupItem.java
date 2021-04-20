package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IPickupItem extends WrapperItem {

    void onPlayerPickupItem(Kit kit, PlayerPickupItemEvent event);

}
