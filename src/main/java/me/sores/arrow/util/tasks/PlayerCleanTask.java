package me.sores.arrow.util.tasks;

import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by sores on 4/23/2021.
 */
public class PlayerCleanTask extends BukkitRunnable {

    private final Player player;
    private final ArrowProfile profile;

    public PlayerCleanTask(Player player, ArrowProfile profile) {
        this.player = player;
        this.profile = profile;
    }

    @Override
    public void run() {
        if(profile.hasKit()) profile.clearKit(player);
        if(!profile.isScoreboard()) profile.hideScoreboard();

        PlayerUtil.gotoSpawn(player);
        ArrowUtil.clean(player);
    }

}
