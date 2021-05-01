package me.sores.arrow.kit.abilities;

import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityHandler;
import me.sores.arrow.kit.AbilityType;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.excep.AbilityPerformException;
import me.sores.arrow.kit.wrapper.IInteract;
import me.sores.impulse.util.LocationUtil;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.json.JSONObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Created by sores on 5/1/2021.
 */
public class Ability_smite extends Ability implements IInteract {

    public Ability_smite() {
        super(AbilityType.SMITE);

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
                StringUtil.color("&8&m------------------------------------------------"),

        };
    }

    private final String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getType().getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - setCooldown <long> &f- Set the cooldown."),
            StringUtil.color("&e - setRadius <int> &f- Set the radius."),
            StringUtil.color("&e - setDamageMult <double> &f- Set the damage mult."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    private int radius = 5;
    private double damageMult = 4.0D;

    @Override
    public void onPlayerInteract(Kit kit, PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item == null || item.getType() != Material.GOLD_AXE) return;

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock() != null){
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

                Block block = event.getClickedBlock();
                Vector blockVector = new Vector();
                Vector playerVector = new Vector();

                smite(player, block, blockVector, playerVector);
                perform(player, this);
            }
        }
    }

    private void smite(Player player, Block block, Vector blockVec, Vector playerVec){
        block.getWorld().strikeLightningEffect(block.getLocation());

        for(Entity entity : LocationUtil.getNearbyEntities(block.getLocation(), getRadius())){
            Location location = entity.getLocation();
            LocationUtil.setupVector(block.getX(), block.getY(), block.getZ(), blockVec);
            LocationUtil.setupVector(location.getX(), location.getY(), location.getZ(), playerVec);

            if(entity instanceof Player && entity != player){
                if(canAttack(player, (Player) entity)){
                    ((Player) entity).damage(getDamageMult());
                    entity.setVelocity(playerVec.subtract(blockVec).normalize().multiply(1.2));
                }
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

    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("cooldown", getCooldown());
        object.put("radius", getRadius());
        object.put("damagemult", getDamageMult());
        return object;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        if(jsonObject.has("cooldown")) setCooldown(jsonObject.getLong("cooldown"));
        if(jsonObject.has("radius")) setRadius(jsonObject.getInt("radius"));
        if(jsonObject.has("damagemult")) setDamageMult(jsonObject.getDouble("damagemult"));
    }

}
