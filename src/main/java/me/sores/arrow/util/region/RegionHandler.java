package me.sores.arrow.util.region;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.sores.arrow.config.RegionsConfig;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.configuration.ConfigFile;
import me.sores.impulse.util.handler.Handler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sores on 4/21/2021.
 */
public class RegionHandler extends Handler {

    private static RegionHandler instance;

    private Map<UUID, RegionSelection> selections;
    private List<Region> regions;
    private List<UUID> selecting;

    public RegionHandler() {
        instance = this;
    }

    @Override
    public void init() {
        selections = Maps.newHashMap();
        regions = Lists.newArrayList();
        selecting = Lists.newArrayList();

        load();
    }

    @Override
    public void unload() {
        selections.clear();
        regions.forEach(this::save);
        regions.clear();
        selecting.clear();
    }

    public RegionSelection getSelection(Player player){
        return selections.get(player.getUniqueId());
    }

    public void addSelection(Player player){
        RegionSelection selection = new RegionSelection();
        selections.put(player.getUniqueId(), selection);
    }

    public void removeSelection(Player player){
        selections.remove(player.getUniqueId());
    }

    public boolean isSelecting(Player player){
        return selecting.contains(player.getUniqueId());
    }

    public void setSelecting(Player player){
        selecting.add(player.getUniqueId());
    }

    public void removeSelecting(Player player){
        selecting.remove(player.getUniqueId());
    }

    public Region getRegion(Location location){
        for(Region region : regions){
            if(region.getCuboid().contains(location)) return region;
        }

        return null;
    }

    public Region getRegion(String name){
        for(Region region : regions){
            if(region.getName().equalsIgnoreCase(name)) return region;
        }

        return null;
    }

    public boolean doesExist(String name){
        for(Region region : regions){
            if(name.equalsIgnoreCase(region.getName())) return true;
        }

        return false;
    }

    public void load(){
        ConfigFile file = RegionsConfig.getFile();
        ConfigurationSection section = file.getConfigurationSection("regions");

        if(section != null){
            for(String key : section.getKeys(false)){
                String path = "regions." + key;
                Region region = new Region(file.getString(path));
                regions.add(region);
            }
        }
    }

    public void save(Region region){
        ConfigFile file = RegionsConfig.getFile();
        String path = "regions." + region.getName();

        file.set(path, region.serialize().toString());
    }

    public void deleteRegion(Region region){
        regions.remove(region);
        if(!RegionsConfig.getFile().contains("regions." + region.getName())) return;

        RegionsConfig.getFile().set("regions." + region.getName(), null);
        new RegionsConfig();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        if(isSelecting(player) && item != null && item.isSimilar(RegionTool.EDITOR.getItem())){
            if(event.getClickedBlock() != null){
                Location location = event.getClickedBlock().getLocation();
                RegionSelection selection = getSelection(player);

                switch (event.getAction()){
                    case LEFT_CLICK_BLOCK:{
                        selection.setPos1(location);
                        MessageUtil.message(player, ChatColor.GREEN + "You have set position 1 - " + location.toString());
                        break;
                    }

                    case RIGHT_CLICK_BLOCK:{
                        selection.setPos2(location);
                        MessageUtil.message(player, ChatColor.GREEN + "You have set position 2 - " + location.toString());
                        break;
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();

        if(isSelecting(player)) removeSelecting(player);
        if(getSelection(player) != null) removeSelection(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();

        if(isSelecting(player)) removeSelecting(player);
        if(getSelection(player) != null) removeSelection(player);
    }

    public Map<UUID, RegionSelection> getSelections() {
        return selections;
    }

    public List<UUID> getSelecting() {
        return selecting;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public static RegionHandler getInstance() {
        return instance;
    }
}
