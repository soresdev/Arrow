package me.sores.arrow.util.killstreaks;

import me.sores.impulse.util.StringUtil;
import org.bukkit.Material;

/**
 * Created by sores on 4/22/2021.
 */
public enum KillstreakTier {

    ONE(5, StringUtil.color("&65 Killstreak"), Material.GOLD_SWORD, KillstreakType.X1_GAPPLE),

    TWO(10, StringUtil.color("&610 Killstreak"), Material.IRON_SWORD, KillstreakType.X3_GAPPLE),

    THREE(15, StringUtil.color("&615 Killstreak"), Material.DIAMOND_SWORD, KillstreakType.GOD_APPLE);

    KillstreakTier(int killsNeeded, String display, Material icon, KillstreakType defaultStreak) {
        this.killsNeeded = killsNeeded;
        this.display = display;
        this.icon = icon;
        this.defaultStreak = defaultStreak;
    }

    private int killsNeeded;
    private String display;
    private Material icon;
    private KillstreakType defaultStreak;

    public int getKillsNeeded() {
        return killsNeeded;
    }

    public String getDisplay() {
        return display;
    }

    public Material getIcon() {
        return icon;
    }

    public KillstreakType getDefaultStreak() {
        return defaultStreak;
    }

    public void setDefaultStreak(KillstreakType defaultStreak) {
        this.defaultStreak = defaultStreak;
    }

}
