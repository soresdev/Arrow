package me.sores.arrow.commands;

import me.sores.arrow.util.prefixes.menu.PrefixMenu;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/22/2021.
 */
public class Command_prefix extends BaseCommand {

    public Command_prefix(){
        super("prefix", "arrow.prefix", CommandUsageBy.PLAYER, new String[] { "tag", "tags", "prefixes", "pref" });
        setUsage("/<command>");
        setArgRange(0, 0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        new PrefixMenu(player, ProfileHandler.getInstance().getFrom(player.getUniqueId())).openMenu(player);
    }

}
