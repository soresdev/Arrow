package me.sores.arrow.commands;

import me.sores.arrow.kit.Kit;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.PlayerUtil;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 5/1/2021.
 */
public class Command_getkit extends BaseCommand {

    public Command_getkit(){
        super("getkit", "arrow.getkit", CommandUsageBy.PLAYER);
        setUsage("/<command> [player]");
        setMinArgs(1);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);

        if(!PlayerUtil.doesExist(target)){
            MessageUtil.message(player, "&cNo player with the name " + args[0] + " found.");
            return;
        }

        ArrowProfile targetProfile = ProfileHandler.getInstance().getFrom(target.getUniqueId());

        if(targetProfile.getSelectedKit() != null){
            Kit kit = targetProfile.getSelectedKit();
            if(kit.isPub()){
                MessageUtil.message(player, "&6" + target.getName() + "'s Kit: &r" + kit.getDisplayName());
            }else{
                MessageUtil.message(player, "&6" + target.getName() + "'s Kit: &rNone");
            }
        }else{
            MessageUtil.message(player, "&6" + target.getName() + "'s Kit: &rNone");
        }
    }

}
