package me.sores.arrow.util;

import me.sores.arrow.Arrow;
import me.sores.impulse.util.PlayerUtil;
import me.sores.impulse.util.StringUtil;
import net.minecraft.server.v1_8_R3.CancelledPacketHandleException;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Created by sores on 4/20/2021.
 */
public class ArrowUtil {

    public static DecimalFormat decimalFormat = new DecimalFormat("0.0#"), combatFormat = new DecimalFormat("0.0");
    public static Random RAND = new Random();

    public static String SCOREBOARD_SPACER = "&8&m" + StringUtils.repeat("-", 32), BOUND_LORE = StringUtil.color("&4Bound");

    public static void skipDeathScreen(final Player player){
        skipDeathScreen(player, null);
    }

    public static void skipDeathScreen(final Player player, Consumer<Player> fun){
        final CraftPlayer craftPlayer = (CraftPlayer) player;

        try{
            Bukkit.getScheduler().scheduleSyncDelayedTask(Arrow.getInstance(), () -> {
                if(player.isDead()){
                    craftPlayer.getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
                }

                if(fun != null){
                    fun.accept(player);
                }
            }, 1L);
        }catch (CancelledPacketHandleException ex){
            ex.printStackTrace();
        }
    }

    public static void resetPlayer(Player player) {
        player.closeInventory();
        PlayerUtil.clearEffects(player);
        PlayerUtil.clearInventory(player);

        if (!player.isDead()) {
            player.setMaxHealth(20.0D);
            player.setHealth(player.getMaxHealth());
        }

        player.setFireTicks(0);
        player.setFoodLevel(20);
        player.setExp(0.0F);
        player.setLevel(0);
        player.setVelocity(new Vector());
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setWalkSpeed(0.2F);
    }

    public static void clean(Player player){
        resetPlayer(player);

        //spawnitems & Ability check
    }

    public static void repairPlayerArmor(Player player){
        for (ItemStack is : player.getInventory().getArmorContents()) {
            if ((is != null) && (is.getType() != Material.AIR) && (!is.getType().isBlock()) && (is.getDurability() != 0) && (is.getType().getMaxDurability() >= 1)) {
                is.setDurability((short) 0);
            }
        }
    }

    public static ItemStack giveItem(Player player, ItemStack item) {
        Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack is = inventory.getItem(i);

            if (is == null || is.getType() == Material.AIR || is.getType() == Material.MUSHROOM_SOUP || (is.getType() == Material.POTION && is.getDurability() == (short) 16421) || is.getType() == Material.BOWL) {
                inventory.setItem(i, item);
                return inventory.getItem(i);
            }

        }

        return null;
    }

    public static void giveItems(Player player, ItemStack... items) {
        Inventory inventory = player.getInventory();
        Iterator<ItemStack> iter = Arrays.asList(items).iterator();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack is = inventory.getItem(i);

            if (is == null || is.getType() == Material.AIR || is.getType() == Material.MUSHROOM_SOUP || (is.getType() == Material.POTION && is.getDurability() == (short) 16421) || is.getType() == Material.BOWL) {
                if (iter.hasNext()) {
                    inventory.setItem(i, iter.next());
                }
                else {
                    break;
                }
            }

        }
    }

}
