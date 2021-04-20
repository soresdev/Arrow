package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IPlayerProjectileHitEvent extends WrapperItem {

    void onPlayerProjectileHitEvent(Kit kit, ProjectileHitEvent event);

}
