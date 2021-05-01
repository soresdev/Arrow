package me.sores.arrow.commands;

import me.sores.arrow.util.enumerations.KillWorth;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.PlayerUtil;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 5/1/2021.
 */
public class Command_worth extends BaseCommand {

    public Command_worth(){
        super("worth", "arrow.worth", CommandUsageBy.PLAYER, new String[]{"killworth"});
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0){
            sendWorth(player, player, ProfileHandler.getInstance().getFrom(player.getUniqueId()));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(!PlayerUtil.doesExist(target)){
            MessageUtil.message(player, "&cNo player with the name " + args[0] + " found.");
            return;
        }

        sendWorth(player, target, ProfileHandler.getInstance().getFrom(target.getUniqueId()));
    }

    private void sendWorth(Player player, Player target, ArrowProfile profile){
        String[] toSend = {
                ChatColor.GOLD.toString() + ChatColor.BOLD + (player == target ? "Your Worth: " : target.getName() + "'s Worth: "),
                ChatColor.GOLD + "Worth Tier: " + ChatColor.YELLOW + KillWorth.getWorthDisplay(profile),
                ChatColor.GOLD + "Kill Payout: " + ChatColor.YELLOW +  KillWorth.getWorth(profile).getMin() + " - " + KillWorth.getWorth(profile).getMax() + " Coins"
        };

        MessageUtil.sendList(player, toSend);
    }

}
