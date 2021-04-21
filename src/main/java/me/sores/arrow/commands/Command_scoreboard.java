package me.sores.arrow.commands;

import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/20/2021.
 */
public class Command_scoreboard extends BaseCommand {

    public Command_scoreboard(){
        super("scoreboard", "arrow.scoreboard", CommandUsageBy.PLAYER, new String[]{"sb", "togglescoreboard"});
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(profile.isScoreboard()){
            profile.hideScoreboard();
        }else{
            profile.showScoreboard();
        }

        MessageUtil.message(player, "&7You have toggled your scoreboard " + (profile.isScoreboard() ? "&aon&7." : "&coff&7."));
    }

}
