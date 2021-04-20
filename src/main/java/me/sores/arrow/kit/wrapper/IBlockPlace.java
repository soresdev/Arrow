package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IBlockPlace extends WrapperItem {

    void onPlayerBlockPlace(Kit kit, BlockPlaceEvent event);

}
