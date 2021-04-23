package me.sores.arrow.util.chatcolors;

import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.ItemData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/22/2021.
 */
public enum ArrowChatColor {

    RESET("Reset", ChatColor.RESET, new ItemData(Material.WOOL, (short) 0)) {
        @Override
        public boolean has(Player player) {
            return true;
        }

        @Override
        public void apply(ArrowProfile profile) {
            profile.setSelectedChatColor(null);
        }
    },

    GREEN("Green", ChatColor.GREEN, new ItemData(Material.WOOL, (short) 5)),
    AQUA("Aqua", ChatColor.AQUA, new ItemData(Material.WOOL, (short) 3)),
    YELLOW("Yellow", ChatColor.YELLOW, new ItemData(Material.WOOL, (short) 4)),
    PINK("Pink", ChatColor.LIGHT_PURPLE, new ItemData(Material.WOOL, (short) 6)),
    RED("Red", ChatColor.RED, new ItemData(Material.WOOL, (short) 14)),
    DARK_GRAY("Dark Gray", ChatColor.DARK_GRAY, new ItemData(Material.WOOL, (short) 7)),
    DARK_GREEN("Dark Green", ChatColor.DARK_GREEN, new ItemData(Material.WOOL, (short) 13));

    ArrowChatColor(String display, ChatColor color, ItemData data){
        this.display = display;
        this.color = color.toString();
        this.data = data;
    }

    ArrowChatColor(String display, String color, ItemData data) {
        this.display = display;
        this.color = color;
        this.data = data;
    }

    private final String display, color;
    private final ItemData data;

    public void apply(ArrowProfile profile){
        profile.setSelectedChatColor(this);
    }

    public boolean has(Player player){
        return player.isOp() || player.hasPermission("arrow.chatcolor." + toString().toLowerCase());
    }

    public String getDisplay() {
        return display;
    }

    public String getColor() {
        return color;
    }

    public ItemData getData() {
        return data;
    }

}
