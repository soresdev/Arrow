package me.sores.arrow.util.region;

import org.bukkit.Location;

/**
 * Created by sores on 4/21/2021.
 */
public class RegionSelection {

    private Location pos1, pos2;

    public RegionSelection() {
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }
}
