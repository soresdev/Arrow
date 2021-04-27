package me.sores.arrow.kit.abilities;

import me.sores.arrow.Arrow;
import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityHandler;
import me.sores.arrow.kit.AbilityType;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.excep.AbilityPerformException;
import me.sores.arrow.kit.runnable.SkunkRunner;
import me.sores.arrow.kit.wrapper.IInteract;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.TaskUtil;
import me.sores.impulse.util.json.JSONObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by sores on 4/26/2021.
 */
public class Ability_skunk extends Ability implements IInteract {

    public Ability_skunk() {
        super(AbilityType.SKUNK);

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
                StringUtil.color("&eMax Ticks: &r" + getMaxTicks()),
                StringUtil.color("&eCircle Radius: &r" + getCircleRadius()),
                StringUtil.color("&8&m------------------------------------------------"),

        };
    }

    private final String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getType().getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - setCooldown <long> &f- Set the cooldown."),
            StringUtil.color("&e - setMaxTicks <int> &f - Set the maxTicks."),
            StringUtil.color("&e - setCircleRadius <int> &f - Set the circle radius."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    private int maxTicks = 5, circleRadius = 5;

    @Override
    public void onPlayerInteract(Kit kit, PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item == null || item.getType() != Material.COAL) return;

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
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

            TaskUtil.runTaskTimer(Arrow.getInstance(), new SkunkRunner(player, getMaxTicks(), getCircleRadius()), 0L, 10L, true);
            MessageUtil.message(player, ChatColor.GREEN + "Releasing toxins in a " + ChatColor.GREEN.toString() + ChatColor.BOLD + circleRadius +
                    ChatColor.GREEN + " block radius.");
            perform(player, this);
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

            case "setmaxticks":{
                if(args.length <= 1){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid int.");
                    return false;
                }

                try{
                    int ticks = Integer.parseInt(args[1]);
                    setMaxTicks(ticks);

                    AbilityHandler.getInstance().save(this);
                    MessageUtil.message(sender, "&7Updated " + getType().getDisplay() + "'s maxticks to " + getMaxTicks() + ".");
                }catch (NumberFormatException ex){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a proper int.");
                }

                break;
            }

            case "setcircleradius":{
                if(args.length <= 1){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid int.");
                    return false;
                }

                try{
                    int radius = Integer.parseInt(args[1]);
                    setCircleRadius(radius);

                    AbilityHandler.getInstance().save(this);
                    MessageUtil.message(sender, "&7Updated " + getType().getDisplay() + "'s circle radius to " + getCircleRadius() + ".");
                }catch (NumberFormatException ex){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a proper int.");
                }

                break;
            }

            default:{
                MessageUtil.sendList(sender, abilityUsage);
            }
        }

        return true;
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public void setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("cooldown", getCooldown());
        object.put("maxticks", getMaxTicks());
        object.put("radius", getCircleRadius());
        return object;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        if(jsonObject.has("cooldown")) setCooldown(jsonObject.getLong("cooldown"));
        if(jsonObject.has("radius")) setCircleRadius(jsonObject.getInt("radius"));
        if(jsonObject.has("maxticks")) setMaxTicks(jsonObject.getInt("maxticks"));
    }

}
