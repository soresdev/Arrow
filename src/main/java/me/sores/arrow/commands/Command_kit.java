package me.sores.arrow.commands;

import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.KitsHandler;
import me.sores.arrow.kit.menu.KitSelectorMenu;
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
 * Created by sores on 4/20/2021.
 */
public class Command_kit extends BaseCommand {

    public Command_kit(){
        super("kit", "arrow.kit", CommandUsageBy.PLAYER);
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(5);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(args.length == 0){
            new KitSelectorMenu(player, profile).openMenu(player);
            return;
        }

        switch (args[0].toLowerCase()){
            case "apply":{
                if(!player.hasPermission("arrow.kit.apply")){
                    MessageUtil.message(player, ChatColor.RED + "You do not have permission to use that command.");
                    return;
                }

                if(args.length == 2){
                    String name = args[1].toLowerCase();
                    Kit kit = KitsHandler.getInstance().valueOf(name);

                    if(name.equalsIgnoreCase("none")){
                        if(profile.getSelectedKit() != null){
                            profile.clearKit(player);
                            MessageUtil.message(player, ChatColor.GREEN + "You have cleared your kit.");
                        }else{
                            MessageUtil.message(player, ChatColor.RED + "You do not have a kit.");
                            return;
                        }

                        return;
                    }

                    if(!KitsHandler.getInstance().getKits().contains(kit)){
                        MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                        return;
                    }

                    kit.apply(player, true);
                    MessageUtil.message(player, ChatColor.GREEN + "You have applied kit " + kit.getName() + ".");
                    return;
                }

                if(args.length == 3){
                    String name = args[1].toLowerCase();
                    Kit kit = KitsHandler.getInstance().valueOf(name);
                    Player target = Bukkit.getPlayer(args[2]);

                    if(name.equalsIgnoreCase("none")){
                        if(!PlayerUtil.doesExist(target)){
                            MessageUtil.message(player, "&cNo player with the name " + args[0] + " found.");
                            return;
                        }

                        ArrowProfile targetProfile = ProfileHandler.getInstance().getFrom(target.getUniqueId());

                        if(targetProfile.getSelectedKit() != null){
                            targetProfile.clearKit(target);
                            MessageUtil.message(player, ChatColor.GREEN + "You have cleared " + target.getName() + "'s kit.");
                        }else{
                            MessageUtil.message(player, ChatColor.RED + target.getName() + " does not have a kit.");
                        }

                        return;
                    }

                    if(!KitsHandler.getInstance().getKits().contains(kit)){
                        MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                        return;
                    }

                    if(!PlayerUtil.doesExist(target)){
                        MessageUtil.message(player, "&cNo player with the name " + args[0] + " found.");
                        return;
                    }

                    kit.apply(target, true);
                    MessageUtil.message(player, ChatColor.GREEN + "You have applied kit " + kit.getName() + " to " + target.getName() + ".");
                }else{
                    MessageUtil.message(player, ChatColor.RED + "Usage: /kit apply <kit> [player]");
                }

                break;
            }
        }
    }

}
