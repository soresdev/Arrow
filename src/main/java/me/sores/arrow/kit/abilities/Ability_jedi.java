package me.sores.arrow.kit.abilities;

import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityHandler;
import me.sores.arrow.kit.AbilityType;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.excep.AbilityPerformException;
import me.sores.arrow.kit.wrapper.IToggleSneak;
import me.sores.impulse.util.LocationUtil;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.ParticleEffect;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.json.JSONObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by sores on 5/1/2021.
 */
public class Ability_jedi extends Ability implements IToggleSneak {

    public Ability_jedi() {
        super(AbilityType.JEDI);

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
                StringUtil.color("&eRadius: &r" + getRadius()),
                StringUtil.color("&eDamage Mult: &r" + getDamageMult()),
                StringUtil.color("&eDirection Mult: &r" + getDirectionMult()),
                StringUtil.color("&8&m------------------------------------------------"),

        };
    }

    private final String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getType().getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - setCooldown <long> &f- Set the cooldown."),
            StringUtil.color("&e - setRadius <int> &f- Set the radius."),
            StringUtil.color("&e - setDamageMult <double> &f- Set the damage mult."),
            StringUtil.color("&e - setDirectionMult <double> &f- Set the direction mult."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    private int radius = 5;
    private double damageMult = 6.0D, directionMult = 2.0D;

    @Override
    public void onToggleSneak(Kit kit, PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item == null || item.getType() != Material.NETHER_STAR) return;

        if(event.isSneaking()){
            try{
                canPerform(player, this);
            }catch (AbilityPerformException ex){
                MessageUtil.message(player, ex.getMessage());
                return;
            }

            if(AbilityHandler.getInstance().isOnCooldown(player)){
                sendCooldownMessage(player, this, AbilityHandler.getInstance().getCooldownTime(player));
                return;
            }

            doPushEffect(player);
            perform(player, this);
        }
    }

    private void doPushEffect(Player player){
        List<Location> circle = LocationUtil.circle(player.getLocation(), getRadius(), 0, true, true, 0);

        for(Entity entity : player.getNearbyEntities(getRadius(), getRadius(), getRadius())){
            if(entity == null || entity.isDead()) return;

            if(entity instanceof Player){
                Player found = (Player) entity;

                if(!canAttack(player, found)) continue;

                found.damage(getDamageMult(), player);
                found.playSound(found.getLocation(), Sound.ENDERDRAGON_HIT, 1f, 1f);
                found.setVelocity(found.getLocation().getDirection().multiply(-getDirectionMult()).setY(0.6));
                MessageUtil.message(found, ChatColor.RED + "You have been pushed back by a jedi.");
            }
        }

        circle.forEach(location -> ParticleEffect.CLOUD.display(0, 0, 0, 0, 1, location, 10));
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

            case "setradius":{
                if(args.length <= 1){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid int.");
                    return false;
                }

                try{
                    int radius = Integer.parseInt(args[1]);
                    setRadius(radius);

                    AbilityHandler.getInstance().save(this);
                    MessageUtil.message(sender, "&7Updated " + getType().getDisplay() + "'s radius to " + getRadius() + ".");
                }catch (NumberFormatException ex){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a proper int.");
                }

                break;
            }

            case "setdamagemult":{
                if(args.length <= 1){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid double.");
                    return false;
                }

                try{
                    double mult = Double.parseDouble(args[1]);
                    setDamageMult(mult);

                    AbilityHandler.getInstance().save(this);
                    MessageUtil.message(sender, "&7Updated " + getType().getDisplay() + "'s damage mult to " + getDamageMult());
                }catch (Exception ex){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid double.");
                }

                break;
            }

            case "setdirectionmult":{
                if(args.length <= 1){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid double.");
                    return false;
                }

                try{
                    double mult = Double.parseDouble(args[1]);
                    setDirectionMult(mult);

                    AbilityHandler.getInstance().save(this);
                    MessageUtil.message(sender, "&7Updated " + getType().getDisplay() + "'s direction mult to " + getDirectionMult());
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public double getDamageMult() {
        return damageMult;
    }

    public void setDamageMult(double damageMult) {
        this.damageMult = damageMult;
    }

    public double getDirectionMult() {
        return directionMult;
    }

    public void setDirectionMult(double directionMult) {
        this.directionMult = directionMult;
    }

    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("cooldown", getCooldown());
        object.put("radius", getRadius());
        object.put("damagemult", getDamageMult());
        object.put("directionmult", getDirectionMult());
        return object;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        if(jsonObject.has("cooldown")) setCooldown(jsonObject.getLong("cooldown"));
        if(jsonObject.has("radius")) setRadius(jsonObject.getInt("radius"));
        if(jsonObject.has("damagemult")) setDamageMult(jsonObject.getDouble("damagemult"));
        if(jsonObject.has("directionmult")) setDirectionMult(jsonObject.getDouble("directionmult"));
    }

}
