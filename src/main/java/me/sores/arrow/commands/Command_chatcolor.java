package me.sores.arrow.commands;

import me.sores.arrow.util.chatcolors.menu.ColorMenu;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/22/2021.
 */
public class Command_chatcolor extends BaseCommand {

    public Command_chatcolor(){
        super("chatcolor", "arrow.chatcolor", CommandUsageBy.PLAYER, new String[]{"colors"});
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        new ColorMenu(player, ProfileHandler.getInstance().getFrom(player.getUniqueId())).openMenu(player);
    }

}
