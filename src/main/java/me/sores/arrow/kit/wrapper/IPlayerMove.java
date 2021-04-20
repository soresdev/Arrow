package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IPlayerMove extends WrapperItem {

    void onPlayerMove(Kit kit, PlayerMoveEvent event);

}
