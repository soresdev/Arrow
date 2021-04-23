package me.sores.arrow.commands;

import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.arrow.util.region.Region;
import me.sores.arrow.util.region.RegionHandler;
import me.sores.arrow.util.region.RegionType;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/22/2021.
 */
public class Command_clearkit extends BaseCommand {

    public Command_clearkit(){
        super("clearkit", "arrow.clearkit", CommandUsageBy.PLAYER, new String[]{"ck"});
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(!profile.hasKit()){
            MessageUtil.message(player, ChatColor.RED + "You do not have a kit.");
            return;
        }

        if(profile.inCombat()){
            MessageUtil.message(player, ChatColor.RED + "You cannot clear your kit in combat.");
            return;
        }

        if(ArrowUtil.isSpawnArea(player)){
            profile.clearKit(player);
            MessageUtil.message(player, ChatColor.GREEN + "You have cleared your kit.");
        }else{
            MessageUtil.message(player, ChatColor.RED + "You must be in spawn to clear your kit.");
        }
    }

}
