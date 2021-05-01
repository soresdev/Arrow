package me.sores.arrow.kit.abilities;

import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityHandler;
import me.sores.arrow.kit.AbilityType;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.excep.AbilityPerformException;
import me.sores.arrow.kit.wrapper.IPlayerInteractEntity;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.json.JSONObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sores on 5/1/2021.
 */
public class Ability_scatter extends Ability implements IPlayerInteractEntity {

    public Ability_scatter() {
        super(AbilityType.SCATTER);

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

    private final String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getType().getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - setCooldown <long> &f- Set the cooldown."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    @Override
    public void onPlayerInteractEntity(Kit kit, PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item == null || item.getType() != Material.ENCHANTMENT_TABLE) return;

        if(event.getRightClicked() instanceof Player){
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

            Player target = (Player) event.getRightClicked();

            List<ItemStack> contents = Arrays.asList(target.getInventory().getContents());
            Collections.shuffle(contents);

            target.getInventory().setContents(contents.toArray(new ItemStack[]{}));
            target.updateInventory();
            target.getWorld().playSound(target.getLocation(), Sound.WOOD_CLICK, 1f, 1f);
            MessageUtil.message(target, ChatColor.RED + "Your inventory was scattered by " + player.getName() + ".");

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

            default:{
                MessageUtil.sendList(sender, abilityUsage);
            }
        }

        return true;
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
