package me.sores.arrow.listener;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Created by sores on 4/20/2021.
 */
public class Listener_worldlistener implements Listener {

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onSnowMelt(BlockFadeEvent event){
        Block block = event.getBlock();

        switch (block.getType()){
            case ICE:
            case SNOW:
            case PACKED_ICE:
            case SNOW_BLOCK:
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        if(event.toWeatherState()){
            event.setCancelled(true);
        }
    }

}
