package me.sores.arrow.commands;

import me.sores.arrow.util.EconUtil;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.PlayerUtil;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/23/2021.
 */
public class Command_pay extends BaseCommand {

    public Command_pay() {
        super("pay", "arrow.pay", CommandUsageBy.PLAYER, new String[] { "sendmoney" } );
        setUsage("/<command> <playerName> <amount>");
        setMinArgs(2);
        setMaxArgs(2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Player target = Bukkit.getPlayer(args[0]);

        if(!PlayerUtil.doesExist(target)){
            MessageUtil.message(player, "&cNo player with the name " + args[0] + " found.");
            return;
        }

        String amount = args[1];

        try{
            int number = Integer.parseInt(amount);

            EconUtil.pay(player, target, number);
        }catch (NumberFormatException ex){
            MessageUtil.message(player, ChatColor.RED + "Please input a proper number");
        }

    }

}
