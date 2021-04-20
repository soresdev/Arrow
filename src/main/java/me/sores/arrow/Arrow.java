package me.sores.arrow;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by sores on 4/20/2021.
 */
public class Arrow extends JavaPlugin {

    private static Arrow instance;

    @Override
    public void onEnable() {
        instance = this;

        new Init(this);
    }

    @Override
    public void onDisable() {
        Init.getInstance().unload();
        instance = null;
    }

    public static Arrow getInstance() {
        return instance;
    }
}
