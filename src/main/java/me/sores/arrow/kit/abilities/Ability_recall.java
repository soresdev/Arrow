package me.sores.arrow.kit.abilities;

import com.google.common.collect.Maps;
import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.AbilityHandler;
import me.sores.arrow.kit.AbilityType;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.excep.AbilityPerformException;
import me.sores.arrow.kit.wrapper.IInteract;
import me.sores.impulse.util.LocationUtil;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.ParticleEffect;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.json.JSONObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

/**
 * Created by sores on 4/26/2021.
 */
public class Ability_recall extends Ability implements IInteract {

    private final Map<UUID, Location> locationMap;

    public Ability_recall() {
        super(AbilityType.RECALL);

        locationMap = Maps.newHashMap();
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
                StringUtil.color("&eDistance: &r" + getDistance() + " blocks"),
                StringUtil.color("&8&m------------------------------------------------"),

        };
    }

    private final String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getType().getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - setCooldown <long> &f- Set the cooldown."),
            StringUtil.color("&e - setDistance <int> &f - Set the distance."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    private int distance = 20;

    @Override
    public void destroy(Player player) {
        super.destroy(player);

        if(locationMap.containsKey(player.getUniqueId())){
            locationMap.get(player.getUniqueId()).getBlock().setType(Material.AIR);
            locationMap.remove(player.getUniqueId());
        }
    }

    @Override
    public void onPlayerInteract(Kit kit, PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item == null || item.getType() != Material.REDSTONE_TORCH_ON) return;

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && !player.isSneaking()){
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

            if(locationMap.containsKey(player.getUniqueId())){
                MessageUtil.message(player, ChatColor.RED + "You already have a recall point.");
                event.setCancelled(true);
                player.updateInventory();
                return;
            }

            Location above = event.getClickedBlock().getLocation().add(0, 1, 0);

            if(above.getBlock().getType() != Material.AIR || isBlacklisted(block.getType())){
                MessageUtil.message(player, ChatColor.RED + "You cannot place a recall point here.");
                return;
            }

            event.setCancelled(true);
            player.updateInventory();
            locationMap.put(player.getUniqueId(), above);
            above.getBlock().setType(Material.REDSTONE_TORCH_ON);

            MessageUtil.message(player, ChatColor.GREEN + "You have set your recall point at " + above.getBlockX() + ", "
                    + above.getBlockY() + ", " + above.getBlockZ() + ".");
            return;
        }

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(player.isSneaking()){
                try{
                    canPerform(player, this);
                }catch (AbilityPerformException ex){
                    MessageUtil.message(player, ex.getMessage());
                }

                if(AbilityHandler.getInstance().isOnCooldown(player)){
                    sendCooldownMessage(player, this, AbilityHandler.getInstance().getCooldownTime(player));
                    return;
                }

                if(!locationMap.containsKey(player.getUniqueId())){
                    MessageUtil.message(player, ChatColor.RED + "You do not have a recall point set.");
                    return;
                }

                Location location = player.getLocation(), check = locationMap.get(player.getUniqueId());

                if(LocationUtil.getDistance(location, check) > getDistance()){
                    MessageUtil.message(player, ChatColor.RED + "You must be within " + getDistance() + " blocks of your recall point.");
                    return;
                }

                player.getWorld().playSound(location, Sound.ENDERMAN_TELEPORT, 2f, 2f);
                player.teleport(check, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                check.getBlock().setType(Material.AIR);
                locationMap.remove(player.getUniqueId());

                LocationUtil.getLine(location, check).forEach(block -> {
                    ParticleEffect.CLOUD.display(0, 1, 0, 0, 3, block.getBlock().getLocation(), 30);
                    ParticleEffect.REDSTONE.display(0, 2, 0, 0, 3, block.getBlock().getLocation(), 30);
                });

                perform(player, this);
                MessageUtil.message(player, ChatColor.GREEN + "You have teleported to your recall point.");
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

            case "setdistance":{
                if(args.length <= 1){
                    MessageUtil.message(sender, ChatColor.RED + "You must input a valid distance.");
                    return false;
                }

                try{
                    int range = Integer.parseInt(args[1]);
                    setDistance(range);

                    AbilityHandler.getInstance().save(this);
                    MessageUtil.message(sender, "&7Updated " + getType().getDisplay() + "'s distance to " + getDistance() + " blocks.");
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

    private boolean isBlacklisted(Material material){
        return !LocationUtil.isSolid(material) || material == Material.TRAP_DOOR || material == Material.CARPET || material == Material.SIGN
                || material == Material.TRIPWIRE_HOOK || material == Material.TORCH || material == Material.RAILS || material == Material.FENCE_GATE
                || material == Material.WHEAT || material == Material.WOOD_BUTTON || material == Material.STONE_BUTTON || material == Material.LONG_GRASS
                || material == Material.IRON_TRAPDOOR || material == Material.WALL_SIGN || material == Material.SIGN_POST || material == Material.SPRUCE_FENCE_GATE
                || material == Material.ACACIA_FENCE_GATE || material == Material.DARK_OAK_FENCE_GATE || material == Material.BIRCH_FENCE_GATE
                || material == Material.JUNGLE_FENCE_GATE || material == Material.PISTON_BASE || material == Material.STAINED_GLASS_PANE
                || material == Material.ANVIL || material == Material.WOOD_STEP || material == Material.WOOD_DOUBLE_STEP;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("cooldown", getCooldown());
        object.put("distance", getDistance());
        return object;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        if(jsonObject.has("cooldown")) setCooldown(jsonObject.getLong("cooldown"));
        if(jsonObject.has("distance")) setDistance(jsonObject.getInt("distance"));
    }

}
