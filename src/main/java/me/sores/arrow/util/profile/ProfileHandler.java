package me.sores.arrow.util.profile;

import com.google.common.collect.Maps;
import me.sores.arrow.Arrow;
import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.tasks.SaveDataTask;
import me.sores.impulse.util.PlayerUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.TaskUtil;
import me.sores.impulse.util.handler.Handler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

/**
 * Created by sores on 4/20/2021.
 */
public class ProfileHandler extends Handler {

    private static ProfileHandler instance;
    private Map<UUID, ArrowProfile> profiles;

    public ProfileHandler() {
        instance = this;
        profiles = Maps.newHashMap();
    }

    @Override
    public void init() {
        if(Bukkit.getOnlinePlayers().size() != 0){
            Bukkit.getOnlinePlayers().forEach(player -> {
                if(getFrom(player.getUniqueId()) == null){
                    profiles.put(player.getUniqueId(), new ArrowProfile(player.getUniqueId()));
                }

                load(getFrom(player.getUniqueId()));
            });
        }

        TaskUtil.runTaskTimer(Arrow.getInstance(), new SaveDataTask(), 20L * 60 * 5, 20L * 60 * 5, true);
    }

    @Override
    public void unload() {
        getProfiles().values().forEach(ArrowProfile::saveData);
        instance = null;
    }

    public void load(ArrowProfile profile){
        Bukkit.getScheduler().runTaskAsynchronously(Arrow.getInstance(), profile::loadData);
    }

    public void save(ArrowProfile profile){
        Bukkit.getScheduler().runTaskAsynchronously(Arrow.getInstance(), profile::saveData);
    }

    @EventHandler
    public void onAsyncPlayerLogin(AsyncPlayerPreLoginEvent event){
        if(getFrom(event.getUniqueId()) == null){
            profiles.put(event.getUniqueId(), new ArrowProfile(event.getUniqueId()));
        }

        ArrowProfile profile = getFrom(event.getUniqueId());
        load(profile);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        ArrowProfile profile = getFrom(player.getUniqueId());

        if(profile.hasKit()) profile.clearKit(player);

        if(profile.inCombat()){
            player.setHealth(0);
            player.spigot().respawn();
        }

        save(profile);
        profiles.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        ArrowProfile profile = getFrom(player.getUniqueId());

        TaskUtil.runTaskLater(Arrow.getInstance(), () -> {
            PlayerUtil.gotoSpawn(player);
            ArrowUtil.resetPlayer(player);

            if(profile.hasKit()) profile.clearKit(player);
            if(!profile.isScoreboard()) profile.hideScoreboard();
        }, 2L, false);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        ArrowProfile profile = getFrom(event.getPlayer().getUniqueId());

        if(profile.getSelectedPrefix() != null){
            if(profile.getSelectedPrefixColor() != null){
                event.setFormat(StringUtil.color(profile.getSelectedPrefixColor().getColor() +
                        profile.getSelectedPrefix().getPrefix().replace("_", " ")) + " " + ChatColor.RESET + event.getFormat());
            }else{
                event.setFormat(profile.getSelectedPrefix().getPrefix().replace("_", " ") + " " + ChatColor.RESET + event.getFormat());
            }
        }

        if(profile.getSelectedChatColor() != null) event.setMessage(StringUtil.color(profile.getSelectedChatColor().getColor().replace("_", " ") + event.getMessage()));
    }

    /**
     * Handle build mode
     * @param event
     */
    @EventHandler
    public void onBuild(BlockPlaceEvent event){
        Player player = event.getPlayer();
        ArrowProfile profile = getFrom(player.getUniqueId());

        if(!profile.isBuilding()) event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        ArrowProfile profile = getFrom(player.getUniqueId());

        if(!profile.isBuilding()) event.setCancelled(true);
    }

    public ArrowProfile getFrom(UUID uuid){
        for(ArrowProfile profile : profiles.values()){
            if(profile.getID().equals(uuid)) return profile;
        }

        return null;
    }

    public Map<UUID, ArrowProfile> getProfiles() {
        return profiles;
    }

    public static ProfileHandler getInstance() {
        return instance;
    }

}
