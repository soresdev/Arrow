package me.sores.arrow.util.prefixes;

import me.sores.impulse.util.ItemData;
import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * Created by sores on 4/22/2021.
 */
public enum ChatPrefixColor {

    DEFAULT("White", ChatColor.WHITE, new ItemData(Material.WOOL, (short) 0)),

    GOLD("Gold", ChatColor.GOLD, new ItemData(Material.WOOL, (short) 1)),

    YELLOW("Yellow", ChatColor.YELLOW, new ItemData(Material.WOOL, (short) 4)),

    GREEN("Green", ChatColor.GREEN, new ItemData(Material.WOOL, (short) 5)),

    DARK_GREEN("Dark Green", ChatColor.DARK_GREEN, new ItemData(Material.WOOL, (short) 13)),

    RED("Red", ChatColor.RED, new ItemData(Material.WOOL, (short) 14)),

    DARK_RED("Dark Red", ChatColor.DARK_RED, new ItemData(Material.WOOL, (short) 14)),

    BLUE("Blue", ChatColor.BLUE, new ItemData(Material.WOOL, (short) 11)),

    DARK_BLUE("Dark Blue", ChatColor.DARK_BLUE, new ItemData(Material.WOOL, (short) 11)),

    AQUA("Aqua", ChatColor.AQUA, new ItemData(Material.WOOL, (short) 3)),

    DARK_AQUA("Dark Aqua", ChatColor.DARK_AQUA, new ItemData(Material.WOOL, (short) 9)),

    PURPLE("Purple", ChatColor.DARK_PURPLE, new ItemData(Material.WOOL, (short) 10)),

    PINK("Pink", ChatColor.LIGHT_PURPLE, new ItemData(Material.WOOL, (short) 6)),

    GRAY("Gray", ChatColor.GRAY, new ItemData(Material.WOOL, (short) 8)),

    DARK_GRAY("Dark Gray", ChatColor.DARK_GRAY, new ItemData(Material.WOOL, (short) 7));

    private final ItemData data;
    private final String display, color;

    ChatPrefixColor(ChatColor color, ItemData data){
        this(color + color.name(), color.toString(), data);
    }

    ChatPrefixColor(String display, ChatColor color, ItemData data){
        this(display, color.toString(), data);
    }

    ChatPrefixColor(String display, String color, ItemData data){
        this.display = display;
        this.color = color;
        this.data = data;
    }

    public ItemData getData() {
        return data;
    }

    public String getDisplay() {
        return display;
    }

    public String getColor() {
        return color;
    }

}
