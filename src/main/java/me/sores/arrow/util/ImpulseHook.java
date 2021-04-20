package me.sores.arrow.util;

import me.sores.arrow.Arrow;
import me.sores.impulse.util.StringUtil;

/**
 * Created by sores on 4/16/2021.
 */
public class ImpulseHook {

    public static void hook(Arrow arrow){
        if(isImpulseHooked()){
            arrow.getServer().getConsoleSender().sendMessage(StringUtil.color("&a[Arrow] Impulse depend found."));
        }else{
            arrow.getServer().getConsoleSender().sendMessage(StringUtil.color("&c[Arrow] Disabling..."));
            arrow.getServer().getConsoleSender().sendMessage(StringUtil.color("&c[Arrow] Arrow cannot be enabled while missing Impulse depend."));
            arrow.getServer().getPluginManager().disablePlugin(arrow);
        }
    }

    private static boolean isImpulseHooked(){
        return Arrow.getInstance().getServer().getPluginManager().getPlugin("Impulse") != null;
    }

}
