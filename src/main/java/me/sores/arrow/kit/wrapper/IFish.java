package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IFish extends WrapperItem {

    void onPlayerFish(Kit kit, PlayerFishEvent event);

}
