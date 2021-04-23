package me.sores.arrow.listener;

import com.google.common.collect.Lists;
import me.sores.arrow.Arrow;
import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.enumerations.HealingItem;
import me.sores.arrow.util.enumerations.SpongeFaces;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.arrow.util.tasks.ItemRemovalTask;
import me.sores.arrow.util.tasks.DropsRemovalTask;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.LocationUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.List;

/**
 * Created by sores on 4/20/2021.
 */
public class Listener_playerlistener implements Listener {

    @EventHandler
    public void onJoinMessage(PlayerJoinEvent event){
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onLeaveMessage(PlayerQuitEvent event){
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onBed(PlayerBedEnterEvent event){
        event.setCancelled(true);
    }

    /**
     * Str Fix
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            if (event.getDamager() != null) {
                if ((event.getDamager() instanceof Player)) {
                    Player player = (Player) event.getDamager();

                    Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();
                    while (iterator.hasNext()) {
                        PotionEffect eff = iterator.next();
                        if (eff.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                            int level = eff.getAmplifier() + 1;
                            event.setDamage(10.0D * event.getDamage() / (10.0D + 13.0D * level) + 13.0D * event.getDamage() * level * 30 / 200.0D / (10.0D + 13.0D * level));
                        }
                    }
                }
            }
        }
    }

    /**
     * Achievement cancel
     */
    @EventHandler
    public void onAchievementCancel(PlayerAchievementAwardedEvent event){
        event.setCancelled(true);
    }

    /**
     * Disable hunger
     */
    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

    /**
     * Soup logic
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(player.isDead()) return;

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(item != null && item.getType() != Material.AIR){
                if(item.getType() == Material.MUSHROOM_SOUP){
                    if(player.getHealth() == player.getMaxHealth()) return;

                    player.getItemInHand().setType(Material.BOWL);
                    player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 6.5F));
                }
            }
        }
    }

    /**
     * Refill signs
     */
    @EventHandler
    public void onRefillSignPlace(SignChangeEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(block.getState() instanceof Sign){
            Sign sign = (Sign) block.getState();

            if(player.hasPermission("arrow.refill")){
                if(event.getLine(0).equalsIgnoreCase("<refill>")){
                    event.setLine(0, StringUtil.color("&m----------"));
                    event.setLine(1, StringUtil.color("&9Free"));
                    event.setLine(2, StringUtil.color("&9Refill"));
                    event.setLine(3, StringUtil.color("&m----------"));
                    sign.update();
                }
            }
        }
    }

    @EventHandler
    public void onOpenRefillInventory(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && block.getState() instanceof Sign){
            Sign sign = (Sign) block.getState();

            if(sign.getLine(1).equalsIgnoreCase(StringUtil.color("&9Free")) && sign.getLine(2).equalsIgnoreCase(StringUtil.color("&9Refill"))){
                ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

                Inventory inventory = Bukkit.createInventory(player, 18, StringUtil.color("&6Healing Item Refill"));
                ItemStack soup = new ItemBuilder(Material.MUSHROOM_SOUP).setName(StringUtil.color("&6Soup")).build();
                ItemStack pots = new ItemBuilder(Material.POTION).setName(StringUtil.color("&6Potion")).setData((short) 16421).build();

                for(int i = 0; i < inventory.getSize(); i++){
                    inventory.addItem(profile.getHealingItem() == HealingItem.SOUP ? soup : pots);
                }

                player.openInventory(inventory);
            }
        }
    }

    /**
     * Remove item drops on death
     */
    @EventHandler
    public void onItemRemoval(PlayerDeathEvent event){
        Player player = event.getEntity();
        Location location = player.getLocation();

        event.getDrops().clear();
        event.setDroppedExp(0);

        if(ArrowUtil.isSpawnArea(location)) return;

        if(player.getKiller() != null){
            Player killer = player.getKiller();
            ArrowProfile profile = ProfileHandler.getInstance().getFrom(killer.getUniqueId());
            List<ItemStack> items = Lists.newArrayList();

            ItemStack soup = new ItemBuilder(Material.MUSHROOM_SOUP).setName(StringUtil.color("&6Soup")).build();
            ItemStack pots = new ItemBuilder(Material.POTION).setName(StringUtil.color("&6Potion")).setData((short) 16421).build();

            for(int i = 0; i < 8; i++){
                items.add(profile.getHealingItem() == HealingItem.SOUP ? soup : pots);
            }

            List<Item> toRemove = Lists.newArrayList();
            items.forEach(itemStack -> {
                Item item = location.getWorld().dropItemNaturally(location, itemStack);
                toRemove.add(item);
            });

            TaskUtil.runTaskLater(Arrow.getInstance(), new DropsRemovalTask(toRemove), 20L * 8, true);
        }
    }

    /**
     * Handle Bound Item Dropping
     */
    @EventHandler
    public void onBoundDrop(PlayerDropItemEvent event){
        ItemStack item = event.getItemDrop().getItemStack();

        if(item.hasItemMeta() && item.getItemMeta().hasLore()){
            if(item.getItemMeta().getLore().contains(ArrowUtil.BOUND_LORE)){
                event.setCancelled(true);
            }
        }

        if(item.getType() == Material.BOWL){
            event.getItemDrop().remove();
            return;
        }

        Item drop = event.getItemDrop();
        TaskUtil.runTaskLater(Arrow.getInstance(), new ItemRemovalTask(drop), 20L * 8, true);
    }

    /**
     * Sponge and other movement features
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMoveEvent(PlayerMoveEvent event) {
        if(!LocationUtil.isDifferentBlock(event.getFrom(), event.getTo())) return;
        Player player = event.getPlayer();
        Location baseLocation = event.getTo().clone().subtract(0, .95D, 0);

        if (baseLocation.getBlock().getType() == Material.SPONGE) {
            Vector send = new Vector(0, 0, 0);

            if (baseLocation.subtract(0, 1, 0).getBlock().getType() == Material.SPONGE) {
                for (SpongeFaces face : SpongeFaces.values()) {
                    Location curLocation = baseLocation.clone().add(face.getxOffset(), 0, face.getzOffset());
                    int tri = 0;

                    while (tri < 10 && curLocation.getBlock().getType() == Material.SPONGE) {
                        curLocation.add(face.getxOffset(), face.getyOffset(), face.getzOffset());
                        face.addToVector(send, .7);
                        tri++;
                    }
                }
                try {
                    player.setVelocity(send);
                }
                catch (Exception ex) {
                }
            }
        }
        else if(event.getTo().getBlock().getType() == Material.IRON_PLATE && ArrowUtil.isSpawnArea(player)) {
            player.setVelocity(player.getLocation().getDirection().multiply(3.5D).setY(1.3));
        }
    }

    @EventHandler
    public void onLandHaybale(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (event.getEntity().getLocation().subtract(0, 1, 0).getBlock().getType() == Material.HAY_BLOCK ||
                    event.getEntity().getLocation().subtract(0, 1, 0).getBlock().getType() == Material.SPONGE) {
                event.setCancelled(true);
            }
        }
    }

}
