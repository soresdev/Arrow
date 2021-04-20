package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IEntityExplode extends WrapperItem {

    void onEntityExplode(Kit kit, EntityExplodeEvent event, Entity entity);


}
