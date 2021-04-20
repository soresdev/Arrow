package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.player.PlayerToggleFlightEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IToggleFlight extends WrapperItem {

    void onPlayerToggleFlight(Kit kit, PlayerToggleFlightEvent event);

}
