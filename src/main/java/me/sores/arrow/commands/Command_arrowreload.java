package me.sores.arrow.commands;

import me.sores.arrow.config.ArrowConfig;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/20/2021.
 */
public class Command_arrowreload extends BaseCommand {

    public Command_arrowreload(){
        super("arrowreload", "arrow.reload", CommandUsageBy.PLAYER, new String[]{"arrowrl"});
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        ArrowConfig.reload();
        MessageUtil.message(player, ChatColor.GREEN + "You have successfully reloaded Arrow's config file.");
    }

}
