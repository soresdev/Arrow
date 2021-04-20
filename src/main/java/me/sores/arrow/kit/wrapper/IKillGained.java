package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IKillGained extends WrapperItem {

    void onKillGained(Kit kit, PlayerDeathEvent event, Player killed, Player killer);

}
