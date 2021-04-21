package me.sores.arrow.util.region;

import me.sores.arrow.config.RegionsConfig;
import me.sores.arrow.util.IChange;
import me.sores.impulse.util.Cuboid;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.json.JSONObject;
import me.sores.impulse.util.serialization.interf.JsonSerializable;
import org.bukkit.command.CommandSender;

/**
 * Created by sores on 4/21/2021.
 */
public class Region implements JsonSerializable, IChange {

    private String name;
    private RegionType type;
    private Cuboid cuboid;

    private boolean pvp, abilities;

    public Region(String name, RegionType type, Cuboid cuboid) {
        this.name = name;
        this.type = type;
        this.cuboid = cuboid;
    }

    public Region(String input){
        deserialize(new JSONObject(input));
    }

    public Region(JSONObject input){
        deserialize(input);
    }

    public String[] toDisplay(){
        return new String[] {
                StringUtil.color("&8&m------------------------------------------------"),
                StringUtil.color("&6&l" + getName() + "'s Info:"),
                StringUtil.color("&eWorld: &r" + getCuboid().getWorld().getName()),
                StringUtil.color("&eCuboid: &r" + getCuboid().toString()),
                StringUtil.color("&eType: &r" + getType().toString()),
                StringUtil.color("&eFlags: "),
                StringUtil.color("&e- PvP: &r" + isPvp()),
                StringUtil.color("&e- Abilities: &r" + isAbilities()),
                StringUtil.color("&8&m------------------------------------------------"),
        };
    }

    public String getName() {
        return name;
    }

    public RegionType getType() {
        return type;
    }

    public void setType(RegionType type) {
        this.type = type;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public boolean isPvp() {
        return pvp;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public boolean isAbilities() {
        return abilities;
    }

    public void setAbilities(boolean abilities) {
        this.abilities = abilities;
    }

    @Override
    public boolean handleChange(CommandSender sender, String[] args) {
        if(args.length == 0){
            MessageUtil.message(sender, "&6Flags: &ePvP, Abilities");
            return false;
        }

        switch (args[0].toLowerCase()){
            case "pvp":{
                setPvp(!isPvp());
                RegionHandler.getInstance().save(this);
                new RegionsConfig();
                MessageUtil.message(sender, "&7You have set the PvP Flag for &a" + getName() + " &7to " + (isPvp() ? "&aenabled&7." : "&cdisabled&7."));
                break;
            }

            case "abilities":{
                setAbilities(!isAbilities());
                RegionHandler.getInstance().save(this);
                new RegionsConfig();
                MessageUtil.message(sender, "&7You have set the Abilities Flag for &a" + getName() + " &7to " + (isAbilities() ? "&aenabled&7." : "&cdisabled&7."));
                break;
            }

            default:{
                MessageUtil.message(sender, "&6Flags: &ePvP, Abilities");
            }
        }

        return true;
    }

    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();

        object.put("name", name);
        if(type != null) object.put("type", type.toString());
        object.put("cuboid", cuboid.serialize());
        object.put("pvp", isPvp());
        object.put("abilities", isAbilities());

        return object;
    }

    @Override
    public void deserialize(JSONObject object) {
        this.name = object.getString("name");
        cuboid = new Cuboid(object.getString("cuboid"));

        if(object.has("type")){
            try{
                this.type = RegionType.valueOf(object.getString("type"));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        if(object.has("pvp")) pvp = object.getBoolean("pvp");
        if(object.has("abilities")) abilities = object.getBoolean("abilities");

    }
}
