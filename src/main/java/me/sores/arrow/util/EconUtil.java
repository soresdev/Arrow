package me.sores.arrow.util;

import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/20/2021.
 */
public class EconUtil {

    public static void giveCoins(ArrowProfile profile, int amount){
        profile.setCoins(profile.getCoins() + amount);
    }

    public static void takeCoins(ArrowProfile profile, int amount){
        profile.setCoins(profile.getCoins() - amount);
    }

    public static boolean canAfford(ArrowProfile profile, int price){
        return profile.getCoins() >= price;
    }

    public static void pay(Player player, Player target, int amount){
        ArrowProfile playerProfile = ProfileHandler.getInstance().getFrom(player.getUniqueId());
        ArrowProfile targetProfile = ProfileHandler.getInstance().getFrom(target.getUniqueId());

        if(target == player){
            MessageUtil.message(player, ChatColor.RED + "You cannot pay yourself.");
            return;
        }

        if(amount < 1){
            MessageUtil.message(player, ChatColor.RED + "You cannot pay less than 1 coin.");
            return;
        }

        if(amount > 500){
            MessageUtil.message(player, ChatColor.RED + "You cannot pay more than 500 coins in one payment.");
            return;
        }

        if(playerProfile.getCoins() < amount){
            MessageUtil.message(player, ChatColor.RED + "You do not have enough coins.");
            return;
        }

        takeCoins(playerProfile, amount);
        giveCoins(targetProfile, amount);

        MessageUtil.message(player, "&7You have paid &a" + amount + " &7coins to &a" + target.getName() + ".");
        MessageUtil.message(target, "&7You have received &a" + amount + " &7coins from &a" + player.getName() + ".");
    }

}
