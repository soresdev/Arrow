package me.sores.arrow.kit;

import com.google.common.collect.Lists;
import me.sores.arrow.config.KitsConfig;
import me.sores.arrow.kit.other.KitCommand;
import me.sores.impulse.util.configuration.ConfigFile;
import me.sores.impulse.util.handler.Handler;
import me.sores.impulse.util.serialization.InventorySerialization;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by sores on 4/20/2021.
 */
public class KitsHandler extends Handler {

    private static KitsHandler instance;
    private List<Kit> kits;

    public KitsHandler() {
        instance = this;
    }

    @Override
    public void init() {
        kits = Lists.newArrayList();

        load();

        kits.forEach(KitCommand::new);
    }

    @Override
    public void unload() {
        kits.clear();
    }

    public boolean has(Player player, Kit kit){
        return player.hasPermission("arrow.kits." + kit.getName().toLowerCase());
    }

    public void makeConfigChange(String path, Object object){
        ConfigFile kitsConfig = KitsConfig.getFile();
        kitsConfig.set(path, object);
        kitsConfig.save();

        new KitsConfig();
    }

    public void load(){
        ConfigFile kitsConfig = KitsConfig.getFile();

        for(String key : kitsConfig.getConfigurationSection("kits").getKeys(false)){ //needs a null check
            String path = "kits." + key;

            Kit kit = new Kit(key);
            kit.setEnabled(kitsConfig.getBoolean(path + ".enabled"));

            if(kitsConfig.contains(path + ".public")){
                kit.setPub(kitsConfig.getBoolean(path + ".public"));
            }

            if(kitsConfig.contains(path + ".icon")){
                kit.setIcon(Material.valueOf(kitsConfig.getString(path + ".icon")));
            }

            if(kitsConfig.contains(path + ".display")){
                kit.setDisplayName(kitsConfig.getString(path + ".display"));
            }

            if(kitsConfig.contains(path + ".loadout.armor")){
                kit.getLoadout().setArmor(InventorySerialization.deserializeInventory(kitsConfig.getString(path + ".loadout.armor")));
            }

            if(kitsConfig.contains(path + ".loadout.contents")){
                kit.getLoadout().setContents(InventorySerialization.deserializeInventory(kitsConfig.getString(path + ".loadout.contents")));
            }

            if(kitsConfig.contains(path + ".potions")){
                kit.setPotions(kitsConfig.getString(path + ".potions"));
            }

            if(kitsConfig.contains(path + ".ability")){
                kit.setRegisteredAbility(AbilityHandler.getInstance().valueOf(kitsConfig.getString(path + ".ability")));
            }

            kits.add(kit);
        }
    }

    public void save(Kit kit){
        ConfigFile kitsConfig = KitsConfig.getFile();
        String path = "kits." + kit.getName();

        kitsConfig.set(path + ".enabled", kit.isEnabled());
        kitsConfig.set(path + ".public", kit.isPub());
        kitsConfig.set(path + ".display", kit.getDisplayName());
        kitsConfig.set(path + ".loadout.armor", kit.getLoadout().getArmor());
        kitsConfig.set(path + ".loadout.contents", kit.getLoadout().getContents());
        kitsConfig.set(path + ".potions", kit.getPotions());
        kitsConfig.set(path + ".icon", kit.getIcon());
        kitsConfig.set(path + ".ability", kit.getRegisteredAbility() != null ? kit.getRegisteredAbility().getType().toString() : "");

        try{
            kitsConfig.save();
            new KitsConfig();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Kit valueOf(String name){
        for(Kit kit : kits){
            if(kit.getName().equalsIgnoreCase(name)) return kit;
        }

        return null;
    }

    public List<Kit> getKits() {
        return kits;
    }

    public static KitsHandler getInstance() {
        return instance;
    }
}
