package me.sores.arrow.commands;

import me.sores.arrow.kit.*;
import me.sores.arrow.util.ArrowUtil;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import me.sores.impulse.util.serialization.PotionEffectSerialization;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by sores on 4/20/2021.
 */
public class Command_sk extends BaseCommand {

    public Command_sk(){
        super("sk", "arrow.sk", CommandUsageBy.PLAYER, new String[]{"setupkit, kitsetup"});
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(10);
    }

    private final String[] usage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6&lKit Setup: "),
            StringUtil.color("&7/sk create <name> &r- Register a new kit."),
            StringUtil.color("&7/sk setdisplay <kit> <name> &r- Set the display name for a kit."),
            StringUtil.color("&7/sk seticon <kit> &r- Set the icon for a kit."),
            StringUtil.color("&7/sk setenabled <kit> &r- Enable/Disable a kit."),
            StringUtil.color("&7/sk setpublic <kit> &r- Make a kit public or unlisted."),
            StringUtil.color("&7/sk setloadout <kit> &r- Set the loadout of a kit."),
            StringUtil.color("&7/sk setpotions <kit> &r- Set potion effects for a kit."),
            StringUtil.color("&7/sk setability <kit> <ability> &r- Set the ability of a kit."),
            StringUtil.color("&7/sk removeability <kit> &r- Removes the kit's current ability."),
            StringUtil.color("&7/sk delete <kit> &r- Delete an existing kit."),
            StringUtil.color("&7/sk view <kit> &r- View info about an existing kit."),
            StringUtil.color("&7/sk copy <kit> <name> &r- Copy an existing kit to a new kit."),
            StringUtil.color("&7/sk bind &r- Bind the item you are holding."),
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
            case "setup":
            case "create":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /sk create <name>");
                    return;
                }

                String name = args[1].toLowerCase();

                if(KitsHandler.getInstance().getKits().contains(KitsHandler.getInstance().valueOf(name))){
                    MessageUtil.message(player, ChatColor.RED + "That kit already exists.");
                    return;
                }

                Kit kit = new Kit(name);
                KitsHandler.getInstance().save(kit);
                KitsHandler.getInstance().getKits().add(kit);

                MessageUtil.message(player, "&7You have created a kit with the name &a" + kit.getName() + ".");
                break;
            }

            case "setenabled":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /sk setenabled <kit>");
                    return;
                }

                String name = args[1].toLowerCase();
                Kit kit = KitsHandler.getInstance().valueOf(name);

                if(!KitsHandler.getInstance().getKits().contains(kit)){
                    MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                    return;
                }

                kit.setEnabled(!kit.isEnabled());
                MessageUtil.message(player, "&7You have " + (kit.isEnabled() ? "&aenabled &7" : "&cdisabled &7") + " kit " + kit.getName() + ".");
                break;
            }

            case "setpublic":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /sk setpublic <kit>");
                    return;
                }

                String name = args[1].toLowerCase();
                Kit kit = KitsHandler.getInstance().valueOf(name);

                if(!KitsHandler.getInstance().getKits().contains(kit)){
                    MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                    return;
                }

                kit.setPub(!kit.isPub());
                MessageUtil.message(player, "&7You have made kit " + kit.getName() + (kit.isPub() ? " &apublic." : "&cunlisted."));
                break;
            }

            case "setdisplay":{
                if(args.length < 3){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /sk setdisplay <kit> [displayName]");
                    return;
                }

                String name = args[1].toLowerCase();
                Kit kit = KitsHandler.getInstance().valueOf(name);

                if(!KitsHandler.getInstance().getKits().contains(kit)){
                    MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                    return;
                }

                String display = args[2];

                kit.setDisplayName(display);
                MessageUtil.message(player, "&7You have set the display name for kit &a" + kit.getName() + " &7to: &a" + display);
                break;
            }

            case "seticon":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /sk seticon <kit>");
                    return;
                }

                String name = args[1].toLowerCase();
                Kit kit = KitsHandler.getInstance().valueOf(name);

                if(!KitsHandler.getInstance().getKits().contains(kit)){
                    MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                    return;
                }

                if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR){
                    MessageUtil.message(player, ChatColor.RED + "You must be holding an item.");
                    return;
                }

                kit.setIcon(player.getItemInHand().getType());
                MessageUtil.message(player, "&7You have set the icon for kit &a" + kit.getName() + ".");
                break;
            }

            case "setloadout":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /sk setloadout <kit>");
                    return;
                }

                String name = args[1].toLowerCase();
                Kit kit = KitsHandler.getInstance().valueOf(name);

                if(!KitsHandler.getInstance().getKits().contains(kit)){
                    MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                    return;
                }

                kit.setLoadout(new Loadout(player.getInventory().getArmorContents(), player.getInventory().getContents()));
                MessageUtil.message(player, "&7You have set the loadout for kit &a" + kit.getName() + ".");
                break;
            }

            case "setpotions":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /sk setpotions <kit>");
                    return;
                }

                String name = args[1].toLowerCase();
                Kit kit = KitsHandler.getInstance().valueOf(name);

                if(!KitsHandler.getInstance().getKits().contains(kit)){
                    MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                    return;
                }

                String potions = PotionEffectSerialization.serializeEffects(player.getActivePotionEffects());
                kit.setPotions(potions);
                MessageUtil.message(player, "&7You have set the potion effects for kit &a" + kit.getName() + ".");
                break;
            }

            case "setability":{
                if(args.length < 3){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /sk setability <kit> [ability]");
                    return;
                }

                String name = args[1].toLowerCase();
                Kit kit = KitsHandler.getInstance().valueOf(name);

                if(!KitsHandler.getInstance().getKits().contains(kit)){
                    MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                    return;
                }

                String abilityName = args[2].toLowerCase();
                Ability ability = AbilityHandler.getInstance().valueOf(abilityName);

                if(!AbilityHandler.getInstance().getAbilities().contains(ability)){
                    MessageUtil.message(player, ChatColor.RED + "No ability with the name " + abilityName + " could be foumd.");
                    return;
                }

                if(kit.getRegisteredAbility() != null){
                    MessageUtil.message(player, ChatColor.RED + "This kit already has a registered ability, remove it to add a new one.");
                    return;
                }

                kit.setRegisteredAbility(ability);
                MessageUtil.message(player, "&7You have registered the &a" + ability.getType().toString() + " &7ability to kit &a" + kit.getName() + "&7.");
                break;
            }

            case "removeability":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /sk removeability <kit>");
                    return;
                }

                String name = args[1].toLowerCase();
                Kit kit = KitsHandler.getInstance().valueOf(name);

                if(!KitsHandler.getInstance().getKits().contains(kit)){
                    MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                    return;
                }

                if(kit.getRegisteredAbility() == null){
                    MessageUtil.message(player, ChatColor.RED + "That kit has no registered ability");
                    return;
                }

                kit.setRegisteredAbility(null);
                MessageUtil.message(player, "&7You have removed the ability from kit &a" + kit.getName() + ".");
                break;
            }

            case "view":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /sk view <kit>");
                    return;
                }

                String name = args[1].toLowerCase();
                Kit kit = KitsHandler.getInstance().valueOf(name);

                if(!KitsHandler.getInstance().getKits().contains(kit)){
                    MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                    return;
                }

                String[] info = {
                        StringUtil.color("&8&m" + StringUtils.repeat("-", 32)),
                        StringUtil.color("&6&lKit Info: "),
                        StringUtil.color("&eName: &r" + kit.getName()),
                        StringUtil.color("&eDisplay: &r" + (kit.getDisplayName() != null && !kit.getDisplayName().isEmpty() ? kit.getDisplayName() : "None")),
                        StringUtil.color("&eEnabled: &r" + kit.isEnabled()),
                        StringUtil.color("&ePublic: &r" + kit.isPub()),
                        StringUtil.color("&eIcon: &r" + (kit.getIcon() != null ? kit.getIcon().toString() : "None")),
                        StringUtil.color("&ePotions: &r" + (kit.getPotions() != null && !kit.getPotions().isEmpty() ?
                                PotionEffectSerialization.getPotionEffects(kit.getPotions()).toString() : "None")),
                        StringUtil.color("&eRegistered Ability: &r" + (kit.getRegisteredAbility() != null ? kit.getRegisteredAbility().getType().toString() : "None")),
                        StringUtil.color("&8&m" + StringUtils.repeat("-", 32)),
                };

                MessageUtil.sendList(player, info);
                break;
            }

            case "delete":{
                if(args.length < 2){
                    MessageUtil.message(player, ChatColor.RED + "Usage: /sk delete <kit>");
                    return;
                }

                String name = args[1].toLowerCase();
                Kit kit = KitsHandler.getInstance().valueOf(name);

                if(!KitsHandler.getInstance().getKits().contains(kit)){
                    MessageUtil.message(player, ChatColor.RED + "No kit with the name " + name + " was found.");
                    return;
                }

                KitsHandler.getInstance().makeConfigChange("kits." + kit.getName(), null);
                MessageUtil.message(player, "&7You have deleted kit &a" + kit.getName() + ".");

                KitsHandler.getInstance().getKits().remove(kit);
                break;
            }

            case "bind":{
                if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR){
                    MessageUtil.message(player, ChatColor.RED + "You must be holding an item.");
                    return;
                }

                ItemStack item = player.getItemInHand();
                ItemStack clone = item.clone();
                ItemMeta meta = clone.getItemMeta();
                meta.setLore(Arrays.asList(ArrowUtil.BOUND_LORE));
                clone.setItemMeta(meta);

                player.setItemInHand(clone);
                MessageUtil.message(player, ChatColor.GREEN + "You have bound the item you are holding.");
                break;
            }

            default:{
                MessageUtil.sendList(player, usage);
            }
        }
    }

}
