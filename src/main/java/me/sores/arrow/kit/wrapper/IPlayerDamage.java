package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IPlayerDamage extends WrapperItem {

    void onPlayerDamage(Kit kit, EntityDamageEvent event, Player damaged);

}
