package me.sores.arrow.kit;

import me.sores.impulse.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by sores on 4/20/2021.
 */
public class AbilityCooldown extends BukkitRunnable {

    private Player player;
    private Ability ability;
    private Long time;
    private boolean cancelled = false;

    public AbilityCooldown(Player player, Ability ability, Long time) {
        this.player = player;
        this.ability = ability;
        this.time = time;
    }

    @Override
    public void run() {
        if(cancelled || ability == null || player == null || player.isDead() || System.currentTimeMillis() > time){
            cancel();
            MessageUtil.message(player, ChatColor.GREEN + "Your " + ability.getType().getDisplay() + " Ability is no longer on cooldown.");
        }
    }

    public void cancel(){
        super.cancel();
        this.cancelled = true;
    }

    public Player getPlayer() {
        return player;
    }

    public Ability getAbility() {
        return ability;
    }

    public Long getTime() {
        return time;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
