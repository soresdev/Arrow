package me.sores.arrow.kit.abilities;

import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityHandler;
import me.sores.arrow.kit.AbilityType;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.excep.AbilityPerformException;
import me.sores.arrow.kit.wrapper.IInteract;
import me.sores.arrow.kit.wrapper.IPlayerHitEntityWithProjectile;
import me.sores.arrow.kit.wrapper.IProjectileLaunch;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.arrow.util.shop.ShopItems;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.json.JSONObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Created by sores on 4/20/2021.
 */
public class Ability_switcher extends Ability implements IPlayerHitEntityWithProjectile, IInteract, IProjectileLaunch {

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

    private final String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getType().getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - setCooldown <long> &f- Set the cooldown."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    @Override
    public void onPlayerInteract(Kit kit, PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getItem() == null || event.getItem().getType() != Material.SNOW_BALL) return;

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            try{
                canPerform(player, this);
            }catch (AbilityPerformException ex){
                MessageUtil.message(player, ex.getMessage());
                event.setCancelled(true);
                player.updateInventory();
                return;
            }

            if(AbilityHandler.getInstance().isOnCooldown(player)){
                sendCooldownMessage(player, this, AbilityHandler.getInstance().getCooldownTime(player));
                event.setCancelled(true);
                player.updateInventory();
            }
        }
    }

    @Override
    public void onPlayerHitEntityWithProjectile(Kit kit, EntityDamageByEntityEvent event, Projectile projectile, Player shooter, Entity hit) {
        if(projectile instanceof Snowball && !projectile.hasMetadata("fakehook") && hit instanceof Player){
            ArrowProfile hitProfile = ProfileHandler.getInstance().getFrom(hit.getUniqueId());
            Location hitLoc = hit.getLocation();

            if(hitProfile.hasShopItem(ShopItems.ANTI_SWITCHER)){
                MessageUtil.message(shooter, ChatColor.RED + hit.getName() + " has Anti-Switcher!");
                return;
            }

            if(hit == shooter){
                event.setCancelled(true);
                return;
            }

            event.setDamage(0);
            event.getDamager().remove();

            hit.teleport(shooter.getLocation(), PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
            shooter.teleport(hitLoc, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);

            MessageUtil.message(shooter, "&7You have switched places with &a" + hit.getName() + ".");
            MessageUtil.message(hit, "&7You have been switched by &a" + shooter.getName() + ".");
        }
    }

    @Override
    public void onProjectileLaunch(Kit kit, ProjectileLaunchEvent event, Player shooter) {
        if(event.getEntity() instanceof Snowball){
            perform(shooter, this);
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
