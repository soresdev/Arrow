package me.sores.arrow.kit.wrapper;

import me.sores.arrow.kit.Kit;
import org.bukkit.event.entity.PotionSplashEvent;

/**
 * Created by sores on 4/20/2021.
 */
public interface IPotionSplashEvent extends WrapperItem {

    void onPotionSplash(Kit kit, PotionSplashEvent event);

}
