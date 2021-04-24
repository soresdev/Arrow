package me.sores.arrow.listener;

import me.sores.arrow.Arrow;
import me.sores.arrow.config.ArrowConfig;
import me.sores.arrow.kit.Ability;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.wrapper.*;
import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.enumerations.KillWorth;
import me.sores.arrow.util.enumerations.SpawnItems;
import me.sores.arrow.util.killstreaks.KillstreakHandler;
import me.sores.arrow.util.killstreaks.KillstreakTier;
import me.sores.arrow.util.killstreaks.KillstreakType;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.arrow.util.region.Region;
import me.sores.arrow.util.region.RegionHandler;
import me.sores.arrow.util.region.RegionType;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.ParticleEffect;
import me.sores.impulse.util.entity.CopyOfFishingHook;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.TimeUnit;

/**
 * Created by sores on 4/20/2021.
 */
public class Listener_kitlistener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        event.setDeathMessage(null);

        profile.addDeath();
        profile.enterCombat(-1);

        if(profile.getLastPearl() != null){
            profile.cleanPearl(player);
        }

        if(!profile.getShopItems().isEmpty()){
            profile.getShopItems().clear();
        }

        if(player.getKiller() != null){
            Player killer = player.getKiller();
            if(killer == player) return;
            ArrowProfile killerProfile = ProfileHandler.getInstance().getFrom(killer.getUniqueId());

            killerProfile.addKill();

            if(killerProfile.hasKit()){
                KillstreakHandler streakHandler = KillstreakHandler.getInstance();
                int mod = (mod = killerProfile.getStreak() % 25) == 0 ? mod = 25 : mod;
                KillstreakTier tier = streakHandler.getTiers().get(mod);

                if(tier != null){
                    KillstreakType type = killerProfile.getSelectedStreak(tier);

                    if(type != null){
                        streakHandler.getKillstreaks().get(type).execute(killerProfile, killer);

                        streakHandler.announceStreakItem(killer, type);
                    }
                }

                if(streakHandler.hasSubstantialStreak(profile)) streakHandler.announceStreakEnd(killer, player);
            }

            ParticleEffect.FLAME.display(0, 0, 0, 1, 15, player.getLocation(), 3);
            ParticleEffect.LAVA.display(0, 0, 0, 1, 15, player.getLocation(), 3);
            killer.playSound(killer.getLocation(), Sound.NOTE_PLING, 1f, 1f);
            killer.playSound(player.getLocation(), Sound.FIRE_IGNITE, 1f, 1f);

            int worth = KillWorth.calculateKillWorth(profile);
            killerProfile.setCoins(killerProfile.getCoins() + worth);

            MessageUtil.message(killer, "&7You have received &a" + worth + " &7coins for killing &a" + player.getName() + "&7.");

            if(killerProfile.hasKit()){
                Kit kit = killerProfile.getSelectedKit();

                if(kit.getRegisteredAbility() != null){
                    Ability ability = kit.getRegisteredAbility();

                    if(ability.fetchWrapperItems().contains(IKillGained.class)){
                        ((IKillGained) ability).onKillGained(kit, event, player, killer);
                    }
                }
            }
        }

        profile.resetStreak();

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IDeath.class)){
                    ((IDeath) ability).onPlayerDeath(kit, event);
                }
            }
        }

        if(player.getVehicle() != null){
            player.getVehicle().remove();
            player.eject();
        }

        if(profile.hasKit()) profile.clearKit(player);

        player.setMaxHealth(20D);
        ArrowUtil.skipDeathScreen(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        ArrowUtil.resetPlayer(event.getPlayer());
        SpawnItems.apply(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());
        ItemStack item = event.getItem();

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(item != null){

                switch (item.getType()){
                    case CHEST:{
                        if(item.isSimilar(SpawnItems.KIT_SELECTOR.getItem())){
                            SpawnItems.KIT_SELECTOR.interact(player, profile);
                        }

                        break;
                    }
                    case EYE_OF_ENDER:{
                        if(item.isSimilar(SpawnItems.SETTINGS.getItem())){
                            SpawnItems.SETTINGS.interact(player, profile);
                        }

                        break;
                    }
                    case WATCH:{
                        if(item.isSimilar(SpawnItems.clone(profile, SpawnItems.PREVIOUS_KIT.getItem()))){
                            SpawnItems.PREVIOUS_KIT.interact(player, profile);
                        }

                        break;
                    }
                    case ENDER_PEARL:{
                        if(RegionHandler.getInstance().getRegion(player.getLocation()) != null){
                            Region region = RegionHandler.getInstance().getRegion(player.getLocation());

                            if(region.getType() == RegionType.SPAWN){
                                event.setCancelled(true);
                                player.updateInventory();

                                MessageUtil.message(player, ChatColor.RED + "You cannot throw pearls in spawn.");
                                return;
                            }
                        }

                        long expiry = profile.getLastPearlThrow();
                        if(expiry > System.currentTimeMillis()){
                            long e = TimeUnit.MILLISECONDS.toSeconds(expiry - System.currentTimeMillis()) + 1;

                            if(e < 0) return;

                            MessageUtil.message(player, ChatColor.RED + "Your pearl cooldown will end in " + e + "s.");
                            event.setCancelled(true);
                            player.updateInventory();
                        }

                        break;
                    }
                }
            }
        }

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IInteract.class)){
                    ((IInteract) ability).onPlayerInteract(kit, event);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof CopyOfFishingHook){
            event.setCancelled(true);
            return;
        }

        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

            if(profile.getSelectedKit() != null && !event.isCancelled()){
                Kit kit = profile.getSelectedKit();

                if(kit.getRegisteredAbility() != null){
                    Ability ability = kit.getRegisteredAbility();

                    if(ability.fetchWrapperItems().contains(IPlayerDamageByEntity.class)){
                        ((IPlayerDamageByEntity) ability).onPlayerDamageByEntity(kit, event, player, event.getDamager());
                    }
                }
            }
        }

        if(event.getDamager() instanceof Player){
            Player damager = (Player) event.getDamager();
            ArrowProfile damagerProfile = ProfileHandler.getInstance().getFrom(damager.getUniqueId());

            if(event.getEntity() instanceof Player){
                Player damaged = (Player) event.getEntity();
                ArrowProfile damagedProfile = ProfileHandler.getInstance().getFrom(damaged.getUniqueId());

                if(ArrowUtil.isSpawnArea(damaged) || ArrowUtil.isSpawnArea(damager)){
                    event.setCancelled(true);
                    return;
                }

                if(!event.isCancelled()){
                    damagerProfile.enterCombat(ArrowConfig.COMBAT_TIMER * 1000);
                    damagedProfile.enterCombat(ArrowConfig.COMBAT_TIMER * 1000);

                    if(damagerProfile.isBloodEffect()) damaged.getLocation().getWorld().playEffect(damaged.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
                }

                if(damagedProfile.getSelectedKit() != null){
                    Kit kit = damagedProfile.getSelectedKit();

                    if(kit.getRegisteredAbility() != null){
                        Ability ability = kit.getRegisteredAbility();

                        if(ability.fetchWrapperItems().contains(IEntityDamageByPlayer.class)){
                            ((IEntityDamageByPlayer) ability).onEntityDamageByPlayer(kit, event, damaged, damager);
                        }
                    }
                }
            }
        }else if(event.getDamager() instanceof Projectile){
            if(((Projectile) event.getDamager()).getShooter() != null && ((Projectile) event.getDamager()).getShooter() instanceof Player){
                Player shooter = (Player) ((Projectile) event.getDamager()).getShooter();
                ArrowProfile profile = ProfileHandler.getInstance().getFrom(shooter.getUniqueId());

                if(event.getEntity() instanceof Player){
                    Player damaged = (Player) event.getEntity();
                    ArrowProfile damagedProfile = ProfileHandler.getInstance().getFrom(damaged.getUniqueId());

                    if(ArrowUtil.isSpawnArea(damaged)){
                        event.setCancelled(true);
                        return;
                    }

                    if(!event.isCancelled()){
                        profile.enterCombat(ArrowConfig.COMBAT_TIMER * 1000);
                        damagedProfile.enterCombat(ArrowConfig.COMBAT_TIMER * 1000);
                    }
                }

                if(profile.getSelectedKit() != null){
                    Kit kit = profile.getSelectedKit();

                    if(kit.getRegisteredAbility() != null){
                        Ability ability = kit.getRegisteredAbility();

                        if(ability.fetchWrapperItems().contains(IPlayerHitEntityWithProjectile.class)){
                            ((IPlayerHitEntityWithProjectile) ability).onPlayerHitEntityWithProjectile(kit, event, (Projectile) event.getDamager(), shooter, event.getEntity());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){
        if(event.getEntity() != null && event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof Player){
            Player shooter = (Player) event.getEntity().getShooter();
            ArrowProfile profile = ProfileHandler.getInstance().getFrom(shooter.getUniqueId());

            if(profile.getSelectedKit() != null){
                Kit kit = profile.getSelectedKit();

                if(kit.getRegisteredAbility() != null){
                    Ability ability = kit.getRegisteredAbility();

                    if(ability.fetchWrapperItems().contains(IPlayerProjectileHitEvent.class)){
                        ((IPlayerProjectileHitEvent) ability).onPlayerProjectileHitEvent(kit, event);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event){
        Player player = event.getPlayer();
        if(player.isDead()) return;
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IToggleFlight.class)){
                    ((IToggleFlight) ability).onPlayerToggleFlight(kit, event);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerHealthRegen(EntityRegainHealthEvent event){
        if(event.getEntity().isDead() || !(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IPlayerHealthRegain.class)){
                    ((IPlayerHealthRegain) ability).onPlayerHealthRegen(kit, event);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerUnmount(VehicleExitEvent event){
        if(event.getExited() instanceof Player){
            Player player = (Player) event.getExited();
            ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

            if(profile.getSelectedKit() != null){
                Kit kit = profile.getSelectedKit();

                if(kit.getRegisteredAbility() != null){
                    Ability ability = kit.getRegisteredAbility();

                    if(ability.fetchWrapperItems().contains(IPlayerUnmount.class)){
                        ((IPlayerUnmount) ability).onPlayerUnmount(kit, event);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IPlayerInteractEntity.class)){
                    ((IPlayerInteractEntity) ability).onPlayerInteractEntity(kit, event);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event){
        Player player = event.getPlayer();
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IFish.class)){
                    ((IFish) ability).onPlayerFish(kit, event);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent event){
        Player player = event.getPlayer();
        if(player.isDead()) return;
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IPickupItem.class)){
                    ((IPickupItem) ability).onPlayerPickupItem(kit, event);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();

        // handle drops & clearing here

        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IItemDrop.class)){
                    ((IItemDrop) ability).onItemDrop(kit, event);
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event){
        if(event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()){
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();

            if(meta.hasLore()){
                if(meta.getLore().contains(ArrowUtil.BOUND_LORE)){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event){
        if(event.isCancelled() || !(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IPlayerCraft.class)){
                    ((IPlayerCraft) ability).onPlayerCraft(kit, event);
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event){
        if(event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof Player){
            Player shooter = (Player) event.getEntity().getShooter();
            ArrowProfile profile = ProfileHandler.getInstance().getFrom(shooter.getUniqueId());

            if(event.getEntity() instanceof EnderPearl){
                EnderPearl pearl = (EnderPearl) event.getEntity();

                profile.setLastPearl(pearl);
                profile.setLastPearlThrow(System.currentTimeMillis() + (1000 * ArrowConfig.PEARL_TIMER));
            }

            if(profile.getSelectedKit() != null){
                Kit kit = profile.getSelectedKit();

                if(kit.getRegisteredAbility() != null){
                    Ability ability = kit.getRegisteredAbility();

                    if(ability.fetchWrapperItems().contains(IProjectileLaunch.class)){
                        ((IProjectileLaunch) ability).onProjectileLaunch(kit, event, shooter);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

            if(ArrowUtil.isSpawnArea(player)){
                event.setCancelled(true);
                return;
            }

            if(profile.getSelectedKit() != null){
                Kit kit = profile.getSelectedKit();

                if(kit.getRegisteredAbility() != null){
                    Ability ability = kit.getRegisteredAbility();

                    if(ability.fetchWrapperItems().contains(IPlayerDamage.class)){
                        ((IPlayerDamage) ability).onPlayerDamage(kit, event, player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemHeldEvent(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IPlayerHeldItemEvent.class)){
                    ((IPlayerHeldItemEvent) ability).onPlayerHeldItem(kit, event, player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleportWrap(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL){
            if(RegionHandler.getInstance().getRegion(event.getTo()) != null){
                Region region = RegionHandler.getInstance().getRegion(event.getTo());

                if(region.getType() == RegionType.SPAWN){
                    event.setCancelled(true);
                    player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                    player.updateInventory();
                }
            }
        }

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IPlayerTeleport.class)){
                    ((IPlayerTeleport) ability).onPlayerTeleport(kit, event);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        if(player.isDead()) return;
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(profile.getSelectedKit() != null){
            Kit kit = profile.getSelectedKit();

            if(kit.getRegisteredAbility() != null){
                Ability ability = kit.getRegisteredAbility();

                if(ability.fetchWrapperItems().contains(IToggleSneak.class)){
                    ((IToggleSneak) ability).onToggleSneak(kit, event);
                }
            }
        }
    }

}
