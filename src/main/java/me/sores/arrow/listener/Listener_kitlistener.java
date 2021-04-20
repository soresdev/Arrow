package me.sores.arrow.listener;

import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.wrapper.IDeath;
import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by sores on 4/20/2021.
 */
public class Listener_kitlistener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        event.setDeathMessage(null);
        if(profile.hasKit()) profile.clearKit(player);

        profile.addDeath();
        profile.enterCombat(-1);

        if(profile.getLastPearl() != null){
            profile.cleanPearl(player);
        }

        //killer stuff

        profile.resetStreak();

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();
            Ability ability = kit.getRegisteredAbility();

            if(ability != null){
                ((IDeath) ability).onPlayerDeath(kit, event);
            }
        }

        if(player.getVehicle() != null){
            player.getVehicle().remove();
            player.eject();
        }

        player.setMaxHealth(20D);
        ArrowUtil.skipDeathScreen(player);
    }

}
