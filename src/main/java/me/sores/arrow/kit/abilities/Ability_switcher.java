package me.sores.arrow.kit.abilities;

import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityType;
import me.sores.impulse.util.StringUtil;
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
        return new String[] {
                StringUtil.color("&8&m------------------------------------------------"),
                StringUtil.color("&6&lAbility Info: "),
                StringUtil.color("&eName: &r" + getType().toString()),
                StringUtil.color("&eDisplay: &r" + getType().getDisplay()),
                StringUtil.color("&eCooldown: &r" + (getCooldown() == -1 ? "None" : getCooldown())),
                StringUtil.color("&8&m------------------------------------------------"),

        };
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
