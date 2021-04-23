package me.sores.arrow.util.enumerations;

import me.sores.arrow.config.ArrowConfig;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.NumberUtil;

/**
 * Created by sores on 4/23/2021.
 */
public class KillWorth {

    public static int calculateKillWorth(ArrowProfile profile){
        double ratio = profile.calculateRatio();

        if(ratio >= ArrowConfig.SUPER_RATIO) return NumberUtil.getInRangeDiff(WorthTiers.SUPER.getMin(), WorthTiers.SUPER.getMax());
        if(ratio >= ArrowConfig.HIGH_RATIO) return NumberUtil.getInRangeDiff(WorthTiers.HIGH.getMin(), WorthTiers.HIGH.getMax());
        if(ratio >= ArrowConfig.MIDDLE_RATIO) return NumberUtil.getInRangeDiff(WorthTiers.MIDDLE.getMin(), WorthTiers.MIDDLE.getMax());
        if(ratio >= ArrowConfig.LOW_RATIO) return NumberUtil.getInRangeDiff(WorthTiers.LOW.getMin(), WorthTiers.LOW.getMax());

        return NumberUtil.getInRangeDiff(WorthTiers.DEFAULT.getMin(), WorthTiers.DEFAULT.getMax());
    }

    public static String getWorthDisplay(ArrowProfile profile){
        double ratio = profile.calculateRatio();

        if(ratio >= ArrowConfig.SUPER_RATIO) return WorthTiers.SUPER.getDisplay();
        if(ratio >= ArrowConfig.HIGH_RATIO) return WorthTiers.HIGH.getDisplay();
        if(ratio >= ArrowConfig.MIDDLE_RATIO) return WorthTiers.MIDDLE.getDisplay();
        if(ratio >= ArrowConfig.LOW_RATIO) return WorthTiers.LOW.getDisplay();

        return WorthTiers.DEFAULT.getDisplay();
    }

    public static WorthTiers getWorth(ArrowProfile profile){
        double ratio = profile.calculateRatio();

        if(ratio >= ArrowConfig.SUPER_RATIO) return WorthTiers.SUPER;
        if(ratio >= ArrowConfig.HIGH_RATIO) return WorthTiers.HIGH;
        if(ratio >= ArrowConfig.MIDDLE_RATIO) return WorthTiers.MIDDLE;
        if(ratio >= ArrowConfig.LOW_RATIO) return WorthTiers.LOW;

        return WorthTiers.DEFAULT;
    }

    public enum WorthTiers {

        DEFAULT("Default", ArrowConfig.DEFAULT_RATIO, ArrowConfig.DEFAULT_MIN, ArrowConfig.DEFAULT_MAX),
        LOW("Low", ArrowConfig.LOW_RATIO, ArrowConfig.LOW_MIN, ArrowConfig.LOW_MAX),
        MIDDLE("Middle", ArrowConfig.MIDDLE_RATIO, ArrowConfig.MIDDLE_MIN, ArrowConfig.MIDDLE_MAX),
        HIGH("High", ArrowConfig.HIGH_RATIO, ArrowConfig.HIGH_MIN, ArrowConfig.HIGH_MAX),
        SUPER("Super", ArrowConfig.SUPER_RATIO, ArrowConfig.SUPER_MIN, ArrowConfig.SUPER_MAX);

        WorthTiers(String display, double ratio, int min, int max) {
            this.display = display;
            this.ratio = ratio;
            this.min = min;
            this.max = max;
        }

        private String display;
        private double ratio;
        private int min, max;

        public String getDisplay() {
            return display;
        }

        public double getRatio() {
            return ratio;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

    }

}
