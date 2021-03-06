package me.sores.arrow.kit.abilities;

import me.sores.arrow.Arrow;
import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityHandler;
import me.sores.arrow.kit.AbilityType;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.excep.AbilityPerformException;
import me.sores.arrow.kit.wrapper.IFish;
import me.sores.arrow.kit.wrapper.IProjectileLaunch;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.arrow.util.shop.ShopItems;
import me.sores.impulse.util.LocationUtil;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.TaskUtil;
import me.sores.impulse.util.json.JSONObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Created by sores on 4/20/2021.
 */
public class Ability_fisherman extends Ability implements IFish, IProjectileLaunch {

    public Ability_fisherman() {
        super(AbilityType.FISHERMAN);

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
    public void onPlayerFish(Kit kit, PlayerFishEvent event) {
        Player player = event.getPlayer();
        if(player.getItemInHand() == null || player.getItemInHand().getType() != Material.FISHING_ROD) return;

        if(event.getCaught() != null && event.getCaught() instanceof Player){
            try{
                canPerform(player, this);
            }catch (AbilityPerformException ex){
                MessageUtil.message(player, ex.getMessage());
            }

            if(AbilityHandler.getInstance().isOnCooldown(player)){
                sendCooldownMessage(player, this, AbilityHandler.getInstance().getCooldownTime(player));
                return;
            }

            if(!LocationUtil.hasAnyBlockUnder(player, 125)){
                MessageUtil.message(player, ChatColor.RED + "You cannot fish people while falling into the void.");
                return;
            }

            Player target = (Player) event.getCaught();
            ArrowProfile targetProfile = ProfileHandler.getInstance().getFrom(target.getUniqueId());

            if(targetProfile.hasShopItem(ShopItems.ANTI_FISHERMAN)){
                MessageUtil.message(player, ChatColor.RED + target.getName() + " has Anti-Fisherman!");
                perform(player, this);
                return;
            }

            target.teleport(player, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
            target.damage(0, player);
            TaskUtil.runTask(Arrow.getInstance(), () -> target.setVelocity(new Vector()), false);

            perform(player, this);
        }
    }

    @Override
    public void onProjectileLaunch(Kit kit, ProjectileLaunchEvent event, Player shooter) {
        if(shooter.getItemInHand() == null || shooter.getItemInHand().getType() != Material.FISHING_ROD) return;

        if(event.getEntity() instanceof FishHook){
            if(AbilityHandler.getInstance().isOnCooldown(shooter)){
                sendCooldownMessage(shooter, this, AbilityHandler.getInstance().getCooldownTime(shooter));
                event.setCancelled(true);
                return;
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
