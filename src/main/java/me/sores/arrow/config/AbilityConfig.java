package me.sores.arrow.config;

import me.sores.arrow.Arrow;
import me.sores.impulse.util.configuration.ConfigFile;

/**
 * Created by sores on 4/20/2021.
 */
public class AbilityConfig {

    private static ConfigFile file;

    public AbilityConfig() {
        file = new ConfigFile("abilities.yml", Arrow.getInstance());
    }

    public static ConfigFile getFile() {
        return file;
    }

}
