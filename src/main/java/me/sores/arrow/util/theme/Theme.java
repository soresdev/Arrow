package me.sores.arrow.util.theme;

import me.sores.arrow.config.ArrowConfig;
import me.sores.impulse.util.StringUtil;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by sores on 4/20/2021.
 */
public class Theme {

    private int ind;
    private String index, primary, secondary, value;

    public Theme(int ind, String index, String primary, String secondary, String value) {
        this.ind = ind;
        this.index = index;
        this.primary = primary;
        this.secondary = secondary;
        this.value = value;
    }

    public static Theme fromSection(ConfigurationSection section, String index){
        return new Theme(ArrowConfig.themeHashMap.size(), index, StringUtil.color(section.getString("primary")),
                StringUtil.color(section.getString("secondary")), StringUtil.color(section.getString("value")));
    }

    public static Theme valueOf(String name){
        for(Theme theme : ArrowConfig.themeList){
            if(theme.getIndex().equalsIgnoreCase(name)) return theme;
        }

        return null;
    }

    public int getInd() {
        return ind;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
