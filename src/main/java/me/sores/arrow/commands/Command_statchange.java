package me.sores.arrow.commands;

import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.arrow.util.stats.StatChange;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.PlayerUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/20/2021.
 */
public class Command_statchange extends BaseCommand {

    public Command_statchange() {
        super("statchange", "arrow.statchange", CommandUsageBy.ALL, new String[] { "statschange" } );
        setUsage("/<command> [playerName]");
        setMinArgs(0);
        setMaxArgs(5);
    }

    private final String[] usage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6&lStats Editor usage:"),
            StringUtil.color("&6/statchange <user> <statType> <args>"),
            StringUtil.color("&6Stat Types: &fcoins, coinsadd, kills, deaths, killsadd, deathsadd, streak, Reset (resets profiles stats)"),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0){
            MessageUtil.sendList(sender, usage);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(!PlayerUtil.doesExist(target)){
            MessageUtil.message(sender, ChatColor.RED + "No player with the name " + args[0] + " found.");
            return;
        }

        ArrowProfile profile = ProfileHandler.getInstance().getFrom(target.getUniqueId());

        StatChange statChange = new StatChange(profile);
        statChange.handleChange(sender, StringUtil.trimList(args, 1));
    }

}
