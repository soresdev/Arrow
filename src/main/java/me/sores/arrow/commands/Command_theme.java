package me.sores.arrow.commands;

import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.arrow.util.theme.menu.ThemesMenu;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/20/2021.
 */
public class Command_theme extends BaseCommand {

    public Command_theme(){
        super("theme", "arrow.theme", CommandUsageBy.PLAYER, new String[]{"themes"});
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        new ThemesMenu(player, ProfileHandler.getInstance().getFrom(player.getUniqueId())).openMenu(player);
    }

}
