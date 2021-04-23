package me.sores.arrow.util.killstreaks;

import me.sores.impulse.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sores on 4/22/2021.
 */
public enum KillstreakType {

    X1_GAPPLE(KillstreakTier.ONE, "&bx1 Gapple", Material.GOLDEN_APPLE, true, Arrays.asList(
            StringUtil.color("&7Upon reaching " + KillstreakTier.ONE.getKillsNeeded() + " kills with this item selected"),
            StringUtil.color("&7you will receive 1 Golden Apple.")
    )),
    REFILL(KillstreakTier.ONE, "&bHealing Item Refill", Material.MUSHROOM_SOUP, Arrays.asList(
            StringUtil.color("&7Upon reaching " + KillstreakTier.ONE.getKillsNeeded() + " kills with this item selected"),
            StringUtil.color("&7your inventory will be refilled with your"),
            StringUtil.color("&7selected Healing Item.")
    )),

    X3_GAPPLE(KillstreakTier.TWO, "&bx3 Gapple", Material.GOLDEN_APPLE, true, Arrays.asList(
            StringUtil.color("&7Upon reaching " + KillstreakTier.TWO.getKillsNeeded() + " kills with this item selected"),
            StringUtil.color("&7you will receive 3 Golden Apples.")
    )),
    ARMOR_REPAIR(KillstreakTier.TWO, "&bArmor Repair", Material.IRON_CHESTPLATE, Arrays.asList(
            StringUtil.color("&7Upon reaching " + KillstreakTier.TWO.getKillsNeeded() + " kills with this item selected"),
            StringUtil.color("&7your armor will repaired to full durability.")
    )),

    GOD_APPLE(KillstreakTier.THREE, "&b1 God Apple", Material.GOLDEN_APPLE, (short) 1, true, Arrays.asList(
            StringUtil.color("&7Upon reaching " + KillstreakTier.THREE.getKillsNeeded() + " kills with this item selected"),
            StringUtil.color("&7you will receive 1 God Apple.")
    )),
    EXTRA_HEARTS(KillstreakTier.THREE, "&bExtra Hearts", Material.SPECKLED_MELON, Arrays.asList(
            StringUtil.color("&7Upon reaching " + KillstreakTier.THREE.getKillsNeeded() + " kills with this item selected"),
            StringUtil.color("&7you will gain 3 extra hearts towards your max health.")
    ));

    KillstreakType(KillstreakTier tier, String display, Material icon, List<String> description){
        this.tier = tier;
        this.display = display;
        this.icon = icon;
        this.description = description;
    }

    KillstreakType(KillstreakTier tier, String display, Material icon, boolean def, List<String> description){
        this.tier = tier;
        this.display = display;
        this.icon = icon;
        this.def = def;
        this.description = description;
    }

    KillstreakType(KillstreakTier tier, String display, Material icon, short data, List<String> description){
        this.tier = tier;
        this.display = display;
        this.icon = icon;
        this.dat = data;
        this.description = description;
    }

    KillstreakType(KillstreakTier tier, String display, Material icon, short data, boolean def, List<String> description){
        this.tier = tier;
        this.display = display;
        this.icon = icon;
        this.dat = data;
        this.def = def;
        this.description = description;
    }

    private KillstreakTier tier;
    private String display;
    private Material icon;
    private short dat;
    private boolean def;
    private List<String> description;

    public boolean has(Player player){
        return def || player.isOp() || player.hasPermission("arrow.killstreaks." + toString().toLowerCase());
    }

    public KillstreakTier getTier() {
        return tier;
    }

    public String getDisplay() {
        return display;
    }

    public Material getIcon() {
        return icon;
    }

    public short getDat() {
        return dat;
    }

    public boolean isDef() {
        return def;
    }

    public List<String> getDescription() {
        return description;
    }

}
