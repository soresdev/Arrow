package me.sores.arrow.util;

import org.bukkit.command.CommandSender;

/**
 * Created by sores on 3/8/2021.
 */
public interface IChange {

    boolean handleChange(CommandSender sender, String[] args);

}
