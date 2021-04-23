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
