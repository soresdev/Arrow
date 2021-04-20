package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IBlockBreak extends WrapperItem {

    void onPlayerBlockBreakEvent(Kit kit, BlockBreakEvent event);

}
