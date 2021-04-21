package me.sores.arrow.commands;

import me.sores.arrow.kit.menu.KitSelectorMenu;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
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
    }

}
