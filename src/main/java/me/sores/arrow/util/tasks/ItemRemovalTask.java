package me.sores.arrow.util.tasks;

import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by sores on 4/23/2021.
 */
public class ItemRemovalTask extends BukkitRunnable {

    private final Item item;

    public ItemRemovalTask(Item item) {
        this.item = item;
    }

    @Override
    public void run() {
        if(item == null) return;

        item.remove();
    }

}
