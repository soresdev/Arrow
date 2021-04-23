package me.sores.arrow.util.stats;

import me.sores.arrow.Arrow;
import me.sores.arrow.util.IChange;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by sores on 4/20/2021.
 */
public class StatChange implements IChange {

    private ArrowProfile profile;

    public StatChange(ArrowProfile profile) {
        this.profile = profile;
    }

    @Override
    public boolean handleChange(CommandSender sender, String[] args) {
        if(args.length == 0){
            MessageUtil.message(sender, ChatColor.GOLD + "Stat Types: ");
            MessageUtil.message(sender, ChatColor.YELLOW + "coins, coinsadd, kills, deaths, killsadd, deathsadd, streak, reset");
            return false;
        }

        new BukkitRunnable(){
            @Override
            public void run() {
                try{

                    switch (args[0].toLowerCase()){
                        case "coins":{
                            if(args.length < 2){
                                MessageUtil.message(sender, ChatColor.RED + "Usage: /statchange <user> coins <amount>");
                                return;
                            }

                            String input = args[1];

                            try{
                                int number = Integer.parseInt(input);

                                profile.setCoins(number);
                                profile.saveData();
                                MessageUtil.message(sender, "&7You have updated &6" + profile.getPlayer().getName() + "'s &7coins to &6" + profile.getCoins() + ".");
                            }catch (NumberFormatException ex){
                                MessageUtil.message(sender, ChatColor.RED + "Please input a proper number.");
                            }

                            break;
                        }

                        case "coinsadd":{
                            if(args.length < 2){
                                MessageUtil.message(sender, ChatColor.RED + "Usage: /statchange <user> coins <amount>");
                                return;
                            }

                            String input = args[1];

                            try{
                                int number = Integer.parseInt(input);

                                profile.setCoins(profile.getCoins() + number);
                                profile.saveData();
                                MessageUtil.message(sender, "&7You have updated &6" + profile.getPlayer().getName() + "'s &7coins to &6" + profile.getCoins() + "&7.");
                            }catch (NumberFormatException ex){
                                MessageUtil.message(sender, ChatColor.RED + "Please input a proper number.");
                            }
                            break;
                        }

                        case "kills":{
                            if(args.length < 2){
                                MessageUtil.message(sender, ChatColor.RED + "Usage: /statchange <user> kills <amount>");
                                return;
                            }

                            String input = args[1];

                            try{
                                int number = Integer.parseInt(input);

                                profile.setKills(number);
                                profile.saveData();
                                MessageUtil.message(sender, "&7You have updated &6" + profile.getPlayer().getName() + "'s &7kills to &6" + profile.getKills() + "&7.");
                            }catch (NumberFormatException ex){
                                MessageUtil.message(sender, ChatColor.RED + "Please input a proper number.");
                            }
                            break;
                        }

                        case "deaths":{
                            if(args.length < 2){
                                MessageUtil.message(sender, ChatColor.RED + "Usage: /statchange <user> deaths <amount>");
                                return;
                            }

                            String input = args[1];

                            try{
                                int number = Integer.parseInt(input);

                                profile.setDeaths(number);
                                profile.saveData();
                                MessageUtil.message(sender, "&7You have updated &6" + profile.getPlayer().getName() + "'s &7deaths to &6" + profile.getDeaths() + "&7.");
                            }catch (NumberFormatException ex){
                                MessageUtil.message(sender, ChatColor.RED + "Please input a proper number.");
                            }
                            break;
                        }

                        case "killsadd":{
                            if(args.length < 2){
                                MessageUtil.message(sender, ChatColor.RED + "Usage: /statchange <user> killsadd <amount>");
                                return;
                            }

                            String input = args[1];

                            try{
                                int number = Integer.parseInt(input);

                                profile.setKills(profile.getKills() + number);
                                profile.saveData();
                                MessageUtil.message(sender, "&7You have updated &6" + profile.getPlayer().getName() + "'s &7kills to &6" + profile.getKills() + "&7.");
                            }catch (NumberFormatException ex){
                                MessageUtil.message(sender, ChatColor.RED + "Please input a proper number.");
                            }
                            break;
                        }

                        case "deathsadd":{
                            if(args.length < 2){
                                MessageUtil.message(sender, ChatColor.RED + "Usage: /statchange <user> deathsadd <amount>");
                                return;
                            }

                            String input = args[1];

                            try{
                                int number = Integer.parseInt(input);

                                profile.setDeaths(profile.getDeaths() + number);
                                profile.saveData();
                                MessageUtil.message(sender, "&7You have updated &6" + profile.getPlayer().getName() + "'s &7deaths to &6" + profile.getDeaths() + "&7.");
                            }catch (NumberFormatException ex){
                                MessageUtil.message(sender, ChatColor.RED + "Please input a proper number.");
                            }
                            break;
                        }

                        case "streak":{
                            if(args.length < 2){
                                MessageUtil.message(sender, ChatColor.RED + "Usage: /statchange <user> deaths <amount>");
                                return;
                            }

                            String input = args[1];

                            try{
                                int number = Integer.parseInt(input);

                                profile.setStreak(number);
                                profile.saveData();
                                MessageUtil.message(sender, "&7You have updated &6" + profile.getPlayer().getName() + "'s &7streak to &6" + profile.getStreak() + "&7.");
                            }catch (NumberFormatException ex){
                                MessageUtil.message(sender, ChatColor.RED + "Please input a proper number.");
                            }
                            break;
                        }

                        case "reset":{
                            try{
                                profile.clean();
                                MessageUtil.message(sender, "&7You have reset &6" + profile.getPlayer().getName() + "'s &7profile.");
                            }catch (Exception ex){
                                MessageUtil.message(sender, ChatColor.RED + "Failed to reset profile, see console for log.");
                                ex.printStackTrace();
                            }
                            break;
                        }

                        default:{
                            MessageUtil.message(sender, ChatColor.GOLD + "Stat Types: ");
                            MessageUtil.message(sender, ChatColor.YELLOW + "coins, coinsadd, kills, deaths, killsadd, deathsadd, streak, reset");
                        }
                    }
                }catch (Exception ex){
                    MessageUtil.message(sender, ChatColor.RED + "Could not perform stat change, see console for log.");
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Arrow.getInstance());

        return true;
    }

}
