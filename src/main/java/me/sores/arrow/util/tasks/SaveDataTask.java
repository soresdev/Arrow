package me.sores.arrow.util.tasks;

import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by sores on 4/22/2021.
 */
public class SaveDataTask extends BukkitRunnable {

    @Override
    public void run() {
        if(Bukkit.getOnlinePlayers().size() == 0) return;

        try{
            ProfileHandler.getInstance().getProfiles().forEach((uuid, arrowProfile) -> arrowProfile.saveData());
            StringUtil.log("&a[Arrow] Completed SaveDataTask saving " + ProfileHandler.getInstance().getProfiles().size() + " profiles.");
        }catch (Exception ex){
            StringUtil.log("&c[Arrow] Failed to complete SaveDataTask, see log.");
            ex.printStackTrace();
        }
    }

}
