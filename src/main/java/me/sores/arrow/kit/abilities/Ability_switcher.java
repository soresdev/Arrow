package me.sores.arrow.kit.abilities;

import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityType;
import me.sores.impulse.util.json.JSONObject;
import org.bukkit.command.CommandSender;

/**
 * Created by sores on 4/20/2021.
 */
public class Ability_switcher extends Ability {

    public Ability_switcher() {
        super(AbilityType.SWITCHER);

        register();
    }

    @Override
    public String[] getInfo() {
        return null;
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
