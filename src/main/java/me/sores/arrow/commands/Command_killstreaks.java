package me.sores.arrow.commands;

import me.sores.arrow.util.killstreaks.menu.KillstreakMenu;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/22/2021.
 */
public class Command_killstreaks extends BaseCommand {

    public Command_killstreaks(){
        super("killstreaks", "arrow.killstreaks", CommandUsageBy.PLAYER, new String[]{"streaks", "killstreak"});
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        new KillstreakMenu(player, ProfileHandler.getInstance().getFrom(player.getUniqueId())).openMenu(player);
    }

}
