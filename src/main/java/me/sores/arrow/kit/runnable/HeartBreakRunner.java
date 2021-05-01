package me.sores.arrow.kit.runnable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.sores.arrow.Arrow;
import me.sores.arrow.kit.Ability;
import me.sores.impulse.util.LocationUtil;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.ParticleEffect;
import me.sores.impulse.util.TaskUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sores on 5/1/2021.
 */
public class HeartBreakRunner extends BukkitRunnable {

    private final Player player;
    private int ticks, maxTicks, circleRadius;
    private final Map<UUID, Long> lastHitMessage = Maps.newHashMap();

    public HeartBreakRunner(Player player, int maxTicks, int circleRadius) {
        this.player = player;
        this.maxTicks = maxTicks;
        this.circleRadius = circleRadius;
    }

    @Override
    public void run() {
        if(ticks >= maxTicks){
            cancel();
            return;
        }else{
            ticks++;
        }

        List<Location> circle = LocationUtil.circle(player.getLocation(), circleRadius, 0, true, true, 0);
        List<UUID> hit = Lists.newArrayList();

        TaskUtil.runTask(Arrow.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                for(Entity entity : player.getNearbyEntities(circleRadius, circleRadius, circleRadius)){
                    if(entity == null || entity.isDead()) continue;

                    if(entity instanceof Player){
                        Player target = (Player) entity;

                        try{
                            if(!Ability.canAttack(player, target)) continue;
                        }catch (Exception ex){
                            continue;
                        }

                        if(target.getGameMode() == GameMode.CREATIVE || !Ability.canAttack(player, target)) continue;

                        if(!hit.contains(target.getUniqueId())){
                            hit.add(target.getUniqueId());
                            if(target.hasPotionEffect(PotionEffectType.WITHER)) target.removePotionEffect(PotionEffectType.WITHER);

                            target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 3, 1));

                            if(!lastHitMessage.containsKey(target.getUniqueId()) || (lastHitMessage.containsKey(target.getUniqueId()) &&
                                    (lastHitMessage.get(target.getUniqueId()) + 1500) <= System.currentTimeMillis())){
                                MessageUtil.message(target, ChatColor.RED + "You have been struck by " + ChatColor.RED.toString() + ChatColor.BOLD
                                        + player.getName() + ChatColor.RED + "'s Heart Break ability.");
                                lastHitMessage.put(target.getUniqueId(), System.currentTimeMillis());
                            }
                        }
                    }
                }

                circle.forEach(location -> ParticleEffect.HEART.display(0, 0, 0, 0, 1, location, 5));
            }
        }, false);
    }

}
