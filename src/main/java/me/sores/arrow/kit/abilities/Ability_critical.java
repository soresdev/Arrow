package me.sores.arrow.kit.abilities;

import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityHandler;
import me.sores.arrow.kit.AbilityType;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.excep.AbilityPerformException;
import me.sores.arrow.kit.wrapper.IEntityDamageByPlayer;
import me.sores.arrow.util.ArrowUtil;
import me.sores.impulse.util.ItemUtil;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.json.JSONObject;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by sores on 4/26/2021.
 */
public class Ability_critical extends Ability implements IEntityDamageByPlayer {

    public Ability_critical() {
        super(AbilityType.CRITICAL);

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
                StringUtil.color("&eChance: &r" + getChance()),
                StringUtil.color("&eMult: &r" + getMult()),
                StringUtil.color("&8&m------------------------------------------------"),

        };
    }

    private final String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getType().getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - setCooldown <long> &f- Set the cooldown."),
            StringUtil.color("&e - setChance <int> &f- Set the chance for a crit strike."),
            StringUtil.color("&e - setMult <double> &f- Set the mult."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    private int chance = 5;
    private double mult = 1.5;

    @Override
    public void onEntityDamageByPlayer(Kit kit, EntityDamageByEntityEvent event, Entity entity, Player damager) {
        if(entity instanceof Player){
            Player damaged = (Player) entity;
            if(damager.getItemInHand() == null || !ItemUtil.isSword(damager.getItemInHand().getType())) return;

            try{
                canPerform(damager, this);
            }catch (AbilityPerformException ex){
                MessageUtil.message(damager, ex.getMessage());
                return;
            }

            if(AbilityHandler.getInstance().isOnCooldown(damager)){
                sendCooldownMessage(damager, this, AbilityHandler.getInstance().getCooldownTime(damager));
                return;
            }

            int rand = ArrowUtil.RAND.nextInt(getChance());

            if(rand == 2){
                damaged.damage(event.getDamage() + getMult());
                damaged.getLocation().getWorld().playEffect(damaged.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_WIRE);
                MessageUtil.message(damager, ChatColor.GREEN + "You landed a CRITICAL strike on " + damaged.getName() + ".");
                perform(damager, this);
            }
        }
    }

    @Override
    public boolean handleChange(CommandSender sender, String[] args) {
        if(args.length == 0){
            MessageUtil.sendList(sender, abilityUsage);
            return false;
        }

        switch (args[0].toLowerCase()){
            case "setcooldown":{
                if (args.length <= 1) {
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid cooldown.");
                    return false;
                }

                try {
                    long cooldown = Long.parseLong(args[1]);
                    setCooldown(cooldown);

                    AbilityHandler.getInstance().save(this);
                    MessageUtil.message(sender, "&7Updated " + getType().getDisplay() + "'s cooldown to " + getCooldown());
                }
                catch (Exception ex) {
                    MessageUtil.message(sender, ChatColor.RED + "You must input a proper cooldown.");
                }

                break;
            }

            case "setchance":{
                if(args.length <= 1){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid int.");
                    return false;
                }

                try{
                    int chance = Integer.parseInt(args[1]);
                    setChance(chance);

                    AbilityHandler.getInstance().save(this);
                    MessageUtil.message(sender, "&7Updated " + getType().getDisplay() + "'s chance to " + getChance());
                }catch (NumberFormatException ex){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid int.");
                }

                break;
            }

            case "setmult":{
                if(args.length <= 1){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid double.");
                    return false;
                }

                try{
                    double mult = Double.parseDouble(args[1]);
                    setMult(mult);

                    AbilityHandler.getInstance().save(this);
                    MessageUtil.message(sender, "&7Updated " + getType().getDisplay() + "'s mult to " + getMult());
                }catch (Exception ex){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid double.");
                }

                break;
            }

            default:{
                MessageUtil.sendList(sender, abilityUsage);
            }
        }

        return true;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public double getMult() {
        return mult;
    }

    public void setMult(double mult) {
        this.mult = mult;
    }

    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("cooldown", getCooldown());
        object.put("chance", getChance());
        object.put("mult", getMult());
        return object;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        if(jsonObject.has("cooldown")) setCooldown(jsonObject.getLong("cooldown"));
        if(jsonObject.has("chance")) setChance(jsonObject.getInt("chance"));
        if(jsonObject.has("mult")) setMult(jsonObject.getDouble("mult"));
    }

}
