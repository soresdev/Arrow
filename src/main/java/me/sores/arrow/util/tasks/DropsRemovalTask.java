package me.sores.arrow.util.tasks;

import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Created by sores on 4/23/2021.
 */
public class DropsRemovalTask extends BukkitRunnable {

    private final List<Item> removal;

    public DropsRemovalTask(List<Item> removal) {
        this.removal = removal;
    }

    @Override
    public void run() {
        if(removal.isEmpty()) return;

        removal.forEach(Item::remove);
    }

}
