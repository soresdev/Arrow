package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IPlayerRespawn extends WrapperItem {

    void onPlayerRespawn(Kit kit, PlayerRespawnEvent event);

}
