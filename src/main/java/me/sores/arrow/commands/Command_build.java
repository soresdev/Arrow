package me.sores.arrow.commands;

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
 * Created by sores on 4/20/2021.
 */
public class Command_build extends BaseCommand {

    public Command_build() {
        super("build", "arrow.build", CommandUsageBy.PLAYER, new String[] { "buildmode" } );
        setUsage("/<command> [playerName]");
        setMinArgs(0);
        setMaxArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0){
            ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());
            profile.setBuilding(!profile.isBuilding());

            MessageUtil.message(player, "&7You have toggled your build mode " + (profile.isBuilding() ? "&aon." : "&coff."));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(!PlayerUtil.doesExist(target)){
            MessageUtil.message(player, "&cNo player with the name " + args[0] + " found.");
            return;
        }

        ArrowProfile targetProfile = ProfileHandler.getInstance().getFrom(target.getUniqueId());
        targetProfile.setBuilding(!targetProfile.isBuilding());

        MessageUtil.message(player, "&7You have toggled &a" + target.getName() + "'s &7build mode " + (targetProfile.isBuilding() ? "&aon." : "&coff."));
    }

}
