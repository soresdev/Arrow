package me.sores.arrow.kit;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.sores.arrow.config.AbilityConfig;
import me.sores.impulse.util.configuration.ConfigFile;
import me.sores.impulse.util.handler.Handler;
import me.sores.impulse.util.json.JSONObject;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sores on 4/20/2021.
 */
public class AbilityHandler extends Handler {

    private static AbilityHandler instance;
    private List<Ability> abilities;

    private Map<UUID, AbilityCooldown> cooldowns;

    public AbilityHandler() {
        instance = this;
    }

    @Override
    public void init() {
        abilities = Lists.newArrayList();
        cooldowns = Maps.newHashMap();

        load();
    }

    @Override
    public void unload() {
        abilities.clear();
    }

    public void registerAbility(Ability ability){
        abilities.add(ability);
    }

    public void addCooldown(Player player, AbilityCooldown cooldown){
        cooldowns.put(player.getUniqueId(), cooldown);
    }

    public boolean isOnCooldown(Player player){
        long expire = cooldowns.containsKey(player.getUniqueId()) ? cooldowns.get(player.getUniqueId()).getTime() : -1;
        if(expire > 0 && !hasExpired(expire)) return true;
        return false;
    }

    public boolean hasExpired(long time){
        return System.currentTimeMillis() >= time;
    }

    public void load(){
        ConfigFile config = AbilityConfig.getFile();

        for(Ability ability : abilities){
            String path = "abilities." + ability.getType().toString();

            if(config.contains(path)){
                JSONObject object = new JSONObject(config.getString(path));
                ability.deserialize(object);
            }else{
                save(ability);
            }
        }
    }

    public void save(Ability ability){
        ConfigFile config = AbilityConfig.getFile();
        String path = "abilities." + ability.getType().toString();

        config.set(path, ability.serialize().toString());
    }

    public Ability valueOf(String name){
        for(Ability ability : abilities){
            if(ability.getType().toString().equalsIgnoreCase(name)) return ability;
        }

        return null;
    }

    public Map<UUID, AbilityCooldown> getCooldowns() {
        return cooldowns;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public static AbilityHandler getInstance() {
        return instance;
    }
}
