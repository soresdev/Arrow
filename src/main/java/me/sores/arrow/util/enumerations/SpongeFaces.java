package me.sores.arrow.util.enumerations;

import me.sores.impulse.util.LocationUtil;
import org.bukkit.util.Vector;

/**
 * Created by sores on 4/20/2021.
 */
public enum SpongeFaces {

    NORTH(0, 0, -1) {
        @Override
        public void addToVector(Vector send, double mult) {
            LocationUtil.addToVector(0, 0, mult, send);
        }
    },
    EAST(1, 0, 0) {
        @Override
        public void addToVector(Vector send, double mult) {
            LocationUtil.addToVector(-mult, 0, 0, send);
        }
    },
    SOUTH(0, 0, 1) {
        @Override
        public void addToVector(Vector send, double mult) {
            LocationUtil.addToVector(0, 0, -mult, send);
        }
    },
    WEST(-1, 0, 0) {
        @Override
        public void addToVector(Vector send, double mult) {
            LocationUtil.addToVector(mult, 0, 0, send);
        }
    },

    DOWN(0, -1, 0) {
        @Override
        public void addToVector(Vector send, double mult) {
            LocationUtil.addToVector(0, mult, 0, send);
        }
    };

    SpongeFaces(int xOffset, int yOffset, int zOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }
    private int xOffset, yOffset, zOffset;

    public abstract void addToVector(Vector send, double mult);

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public int getzOffset() {
        return zOffset;
    }

}
