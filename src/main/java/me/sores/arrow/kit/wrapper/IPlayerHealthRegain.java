package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.entity.EntityRegainHealthEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IPlayerHealthRegain extends WrapperItem {

    void onPlayerHealthRegen(Kit kit, EntityRegainHealthEvent event);


}
