package me.sores.arrow.util.prefixes;

import me.sores.arrow.config.ArrowConfig;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.ItemData;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/22/2021.
 */
public class ChatPrefix {

    public static class Prefix {

        public Prefix(String index, String display, String color, ItemData data) {
            this.index = index;
            this.display = display;
            this.prefix = color;
            this.data = data;
        }

        private final String index, display, prefix;
        private final ItemData data;

        public void apply(ArrowProfile profile){
            profile.setSelectedPrefix(this);
        }

        public boolean has(Player player){
            return player.isOp() || player.hasPermission("arrow.prefixes." + index.toLowerCase());
        }

        public String getIndex() {
            return index;
        }

        public String getDisplay() {
            return display;
        }

        public String getPrefix() {
            return prefix;
        }

        public ItemData getData() {
            return data;
        }

    }

    public static Prefix valueOf(String name){
        for(Prefix prefix : ArrowConfig.prefixes){
            if(prefix.getIndex().equalsIgnoreCase(name)) return prefix;
        }

        return null;
    }

}
