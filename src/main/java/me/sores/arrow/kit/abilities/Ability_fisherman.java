package me.sores.arrow.kit.abilities;

import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityType;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.wrapper.IDeath;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.json.JSONObject;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by sores on 4/20/2021.
 */
public class Ability_fisherman extends Ability implements IDeath {

    public Ability_fisherman() {
        super(AbilityType.FISHERMAN);

        register();
    }

    @Override
    public String[] getInfo() {
        return null;
    }

    @Override
    public void onPlayerDeath(Kit kit, PlayerDeathEvent event) {
        Player player = event.getEntity();

        MessageUtil.message(player, ChatColor.RED + "Hi there.");
    }

    @Override
    public boolean handleChange(CommandSender sender, String[] args) {
        return false;
    }

    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("cooldown", getCooldown());
        return object;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        if(jsonObject.has("cooldown")) setCooldown(jsonObject.getLong("cooldown"));
    }

}
