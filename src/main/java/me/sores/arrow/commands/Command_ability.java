package me.sores.arrow.commands;

import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityHandler;
import me.sores.arrow.kit.AbilityType;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.PlayerUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/20/2021.
 */
public class Command_ability extends BaseCommand {

    public Command_ability(){
        super("ability", "arrow.ability", CommandUsageBy.PLAYER, new String[]{"abilities"});
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(5);
    }

    private final String[] usage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6&lAbility Usage: "),
            StringUtil.color("&e/ability edit <ability> &f- Edit an ability's individual attributes."),
            StringUtil.color("&e/ability info <ability> &f - View info of an ability."),
            StringUtil.color("&e/ability list &f- List all abilities."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0){
            MessageUtil.sendList(player, usage);
            return;
        }

        String input = args[0].toLowerCase();

        switch (input){
            case "edit":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /ability edit <ability>");
                    return;
                }

                Ability ability = AbilityHandler.getInstance().valueOf(args[1]);

                if(!AbilityHandler.getInstance().getAbilities().contains(ability)){
                    MessageUtil.message(player, ChatColor.RED + "No ability with the name " + args[1] + " could be found.");
                    return;
                }

                ability.handleChange(player, StringUtil.trimList(args, 2));
                break;
            }

            case "view":
            case "info":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /ability info <ability>");
                    return;
                }

                Ability ability = AbilityHandler.getInstance().valueOf(args[1]);

                if(!AbilityHandler.getInstance().getAbilities().contains(ability)){
                    MessageUtil.message(player, ChatColor.RED + "No ability with the name " + args[1] + " could be found.");
                    return;
                }

                MessageUtil.sendList(player, ability.getInfo());
                break;
            }

            case "list":{
                MessageUtil.message(player, ChatColor.GOLD + "Ability Types: ");
                MessageUtil.message(player, ChatColor.YELLOW + AbilityType.toPrettyList());
                break;
            }

            default:{
                MessageUtil.sendList(player, usage);
            }
        }
    }

}
