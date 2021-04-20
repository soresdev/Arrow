package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IProjectileLaunch extends WrapperItem {

    void onProjectileLaunch(Kit kit, ProjectileLaunchEvent event, Player shooter);

}
