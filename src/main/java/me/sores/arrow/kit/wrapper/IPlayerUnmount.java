package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.vehicle.VehicleExitEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IPlayerUnmount extends WrapperItem {

    void onPlayerUnmount(Kit kit, VehicleExitEvent event);

}
