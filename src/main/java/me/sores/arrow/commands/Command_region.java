package me.sores.arrow.commands;

import me.sores.arrow.util.region.*;
import me.sores.impulse.util.Cuboid;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.PlayerUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/21/2021.
 */
public class Command_region extends BaseCommand {

    public Command_region(){
        super("region", "arrow.region", CommandUsageBy.PLAYER, new String[]{"rg", "regions"});
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(10);
    }

    private final String[] usage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6&lRegion Usage: "),
            StringUtil.color("&e/region create <name> <type> &f- Create a region."),
            StringUtil.color("&e/region delete <name> &f- Delete a region."),
            StringUtil.color("&e/region view <name> &r - View info of a region."),
            StringUtil.color("&e/region list &f- List the regions."),
            StringUtil.color("&e/region setflag <region> <flag> &f- Change or add flags to a region."),
            StringUtil.color("&e/region edit &f- Sets you into editing mode and gives you the editor."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0){
            MessageUtil.sendList(player, usage);
            return;
        }

        switch (args[0].toLowerCase()){
            case "create":{
                if(args.length < 3){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /rg create <name> <type>");
                    return;
                }

                String input = args[1];

                if(RegionHandler.getInstance().getSelection(player) == null){
                    MessageUtil.message(player, ChatColor.RED + "You need to select two points.");
                    return;
                }

                RegionSelection selection = RegionHandler.getInstance().getSelection(player);

                if(selection.getPos1() == null || selection.getPos2() == null){
                    MessageUtil.message(player, ChatColor.RED + "You do not have 2 points selected.");
                    return;
                }

                String type = args[2];
                RegionType regionType = null;

                try{
                    regionType = RegionType.valueOf(type);
                }catch (Exception ex){
                    MessageUtil.message(player, ChatColor.RED + "Region Types: SPAWN, OTHER");
                }

                if(RegionHandler.getInstance().doesExist(input)){
                    MessageUtil.message(player, ChatColor.RED + "That region already exists.");
                    return;
                }

                Region region = new Region(input, regionType, new Cuboid(selection.getPos1(), selection.getPos2()));
                RegionHandler.getInstance().save(region);
                RegionHandler.getInstance().getRegions().add(region);
                RegionHandler.getInstance().removeSelecting(player);
                RegionHandler.getInstance().removeSelection(player);
                PlayerUtil.clearInventory(player);

                MessageUtil.message(player, ChatColor.GREEN + "You have successfully created region " + input + " with type " + regionType.toString() + ".");
                break;
            }

            case "delete":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /region delete <name>");
                    return;
                }

                String input = args[1];

                if(!RegionHandler.getInstance().doesExist(input)){
                    MessageUtil.message(player, ChatColor.RED + "No region with the name " + input + " could be found.");
                    return;
                }

                Region region = RegionHandler.getInstance().getRegion(input);
                RegionHandler.getInstance().deleteRegion(region);
                MessageUtil.message(player, "&7You have deleted region &a" + input + ".");
                break;
            }

            case "edit":{
                if(RegionHandler.getInstance().isSelecting(player)){
                    RegionHandler.getInstance().removeSelecting(player);
                    RegionHandler.getInstance().removeSelection(player);
                    PlayerUtil.clearInventory(player);

                    MessageUtil.message(player, "&7You are no longer able to select regions.");
                }else{
                    PlayerUtil.clearInventory(player);
                    RegionHandler.getInstance().setSelecting(player);
                    RegionHandler.getInstance().addSelection(player);

                    player.setItemInHand(RegionTool.EDITOR.getItem());
                    MessageUtil.message(player, "&7You are now able to select regions.");
                }

                break;
            }

            case "view":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /region view <name>");
                    return;
                }

                String input = args[1];

                if(!RegionHandler.getInstance().doesExist(input)){
                    MessageUtil.message(player, ChatColor.RED + "No region with the name " + input + " could be found.");
                    return;
                }

                Region region = RegionHandler.getInstance().getRegion(input);
                MessageUtil.sendList(player, region.toDisplay());
                break;
            }

            case "list":{
                MessageUtil.message(player, ChatColor.GOLD + "Regions: ");
                for(Region region : RegionHandler.getInstance().getRegions()){
                    MessageUtil.message(player, ChatColor.YELLOW + "- " + region.getName() + " (" + region.getType().toString() + ")");
                }

                break;
            }

            case "setflag":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /region setflag <region> [flag]");
                    return;
                }

                String input = args[1];

                if(!RegionHandler.getInstance().doesExist(input)){
                    MessageUtil.message(player, ChatColor.RED + "No region with the name " + input + " could be found.");
                    return;
                }

                Region region = RegionHandler.getInstance().getRegion(input);
                region.handleChange(player, StringUtil.trimList(args, 2));
                break;
            }
        }
    }
}
