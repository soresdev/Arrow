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
public class Command_stats extends BaseCommand {

    public Command_stats(){
        super("stats", "arrow.stats", CommandUsageBy.PLAYER);
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0){
            sendStats(player, player, ProfileHandler.getInstance().getFrom(player.getUniqueId()));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(!PlayerUtil.doesExist(target)){
            MessageUtil.message(player, "&cNo player with the name " + args[0] + " found.");
            return;
        }

        sendStats(player, target, ProfileHandler.getInstance().getFrom(target.getUniqueId()));
    }

    private void sendStats(Player player, Player target, ArrowProfile profile){
        String[] toSend = {
                ChatColor.GOLD.toString() + ChatColor.BOLD + (player == target ? "Your Stats: " : target.getName() + "'s Stats: "),
                ChatColor.GOLD + "Coins: " + ChatColor.YELLOW + profile.getCoins(),
                ChatColor.GOLD + "Kills: " + ChatColor.YELLOW + profile.getKills(),
                ChatColor.GOLD + "Deaths: " + ChatColor.YELLOW + profile.getDeaths(),
                ChatColor.GOLD + "Ratio: " + ChatColor.YELLOW + profile.calculateRatio(),
                ChatColor.GOLD + "Streak: " + ChatColor.YELLOW + profile.getStreak(),
                ChatColor.GOLD + "Worth Tier: " + ChatColor.YELLOW + KillWorth.getWorthDisplay(profile)
        };

        MessageUtil.sendList(player, toSend);
    }

}
