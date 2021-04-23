package me.sores.arrow.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.sores.arrow.Arrow;
import me.sores.arrow.util.prefixes.ChatPrefix;
import me.sores.arrow.util.theme.Theme;
import me.sores.impulse.util.ItemData;
import me.sores.impulse.util.configuration.ConfigFile;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sores on 4/20/2021.
 */
public class ArrowConfig {

    private static ConfigFile file;

    public static String DATABASE_HOST, DATABASE_USERNAME, DATABASE_PASSWORD, DATABASE_NAME, DATABASE_COLLECTION;

    public static String SCOREBOARD_TITLE;
    public static int COMBAT_TIMER, PEARL_TIMER;

    public static HashMap<String, Theme> themeHashMap = Maps.newHashMap();
    public static List<Theme> themeList = Lists.newArrayList();
    public static Theme defaultTheme;

    public static List<ChatPrefix.Prefix> prefixes = Lists.newArrayList();

    //Kill Worth
    public static double SUPER_RATIO, HIGH_RATIO, MIDDLE_RATIO, LOW_RATIO, DEFAULT_RATIO;
    public static int SUPER_MIN, SUPER_MAX, HIGH_MIN, HIGH_MAX, MIDDLE_MIN, MIDDLE_MAX, LOW_MIN, LOW_MAX, DEFAULT_MIN, DEFAULT_MAX;

    public ArrowConfig() {
        file = new ConfigFile("config.yml", Arrow.getInstance());

        DATABASE_HOST = file.getString("database.host");
        DATABASE_USERNAME = file.getString("database.username");
        DATABASE_PASSWORD = file.getString("database.password");
        DATABASE_NAME = file.getString("database.name");
        DATABASE_COLLECTION = file.getString("database.collection");

        SCOREBOARD_TITLE = file.getString("scoreboard.title");
        COMBAT_TIMER = file.getInt("combat.timer");
        PEARL_TIMER = file.getInt("combat.pearl");

        if(file.contains("themes")){
            for(String rawTheme : file.getConfigurationSection("themes").getKeys(false)){
                registerTheme(Theme.fromSection(file.getConfigurationSection("themes." + rawTheme), rawTheme));
            }
            defaultTheme = themeHashMap.get("default");
        }

        if(file.contains("prefixes")){
            ItemData data = new ItemData(Material.NAME_TAG);

            for(String index : file.getConfigurationSection("prefixes").getKeys(false)){
                String path = "prefixes." + index + ".";

                registerPrefix(new ChatPrefix.Prefix(index, StringEscapeUtils.unescapeJava(file.getString(path + "display")),
                        StringEscapeUtils.unescapeJava(file.getString(path + "prefix")), data));
            }
        }

        /////// WORTH RATIOS //////
        SUPER_RATIO = file.getDouble("worths.super.ratio");
        SUPER_MAX = file.getInt("worths.super.max");
        SUPER_MIN = file.getInt("worths.super.min");

        HIGH_RATIO = file.getDouble("worths.high.ratio");
        HIGH_MAX = file.getInt("worths.high.max");
        HIGH_MIN = file.getInt("worths.high.min");


        MIDDLE_RATIO = file.getDouble("worths.middle.ratio");
        MIDDLE_MAX = file.getInt("worths.middle.max");
        MIDDLE_MIN = file.getInt("worths.middle.min");

        LOW_RATIO = file.getDouble("worths.low.ratio");
        LOW_MAX = file.getInt("worths.low.max");
        LOW_MIN = file.getInt("worths.low.min");

        DEFAULT_RATIO = file.getDouble("worths.default.ratio");
        DEFAULT_MAX = file.getInt("worths.default.max");
        DEFAULT_MIN = file.getInt("worths.default.min");

    }

    public static void reload(){
        destroy();
        new ArrowConfig();
    }

    private static void destroy(){
        themeHashMap.clear();
        themeList.clear();
        prefixes.clear();
    }

    private void registerTheme(Theme theme){
        themeHashMap.put(theme.getIndex(), theme);
        themeList.add(theme);
    }

    private void registerPrefix(ChatPrefix.Prefix prefix){
        prefixes.add(prefix);
    }

    public static Theme getTheme(String raw){
        Theme theme = themeHashMap.get(raw);

        if(theme == null) theme = defaultTheme;

        return theme;
    }

    public static ConfigFile getFile() {
        return file;
    }
}
