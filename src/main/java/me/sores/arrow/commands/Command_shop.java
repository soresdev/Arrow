package me.sores.arrow.commands;

import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.arrow.util.shop.menu.ShopMenu;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/24/2021.
 */
public class Command_shop extends BaseCommand {

    public Command_shop(){
        super("shop", "arrow.shop", CommandUsageBy.PLAYER);
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        new ShopMenu(player, ProfileHandler.getInstance().getFrom(player.getUniqueId())).openMenu(player);
    }

}
