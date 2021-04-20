package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.player.PlayerToggleSneakEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IToggleSneak extends WrapperItem {

    void onToggleSneak(Kit kit, PlayerToggleSneakEvent event);

}
