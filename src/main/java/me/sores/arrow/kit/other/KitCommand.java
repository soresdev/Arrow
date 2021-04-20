package me.sores.arrow.kit.other;

import me.sores.arrow.Init;
import me.sores.arrow.kit.Kit;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/20/2021.
 */
public class KitCommand {

    private final Kit kit;

    public KitCommand(Kit kit) {
        this.kit = kit;

        setup();
    }

    private void setup(){
        try{
            Init.getInstance().getCommandRegistrar().registerCommand(kit.getName(), new BaseCommand(kit.getName(), "arrow.kits." + kit.getName().toLowerCase(), CommandUsageBy.PLAYER) {
                @Override
                public void execute(CommandSender sender, String[] args) {
                    Player player = (Player) sender;
                    kit.apply(player);
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
