package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IPlayerDamageByEntity extends WrapperItem {

    void onPlayerDamageByEntity(Kit kit, EntityDamageByEntityEvent event, Player damaaged, Entity damager);

}
