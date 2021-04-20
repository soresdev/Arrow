package me.sores.arrow.config;

import me.sores.arrow.Arrow;
import me.sores.impulse.util.configuration.ConfigFile;

/**
 * Created by sores on 4/20/2021.
 */
public class KitsConfig {

    private static ConfigFile file;

    public KitsConfig() {
        file = new ConfigFile("kits.yml", Arrow.getInstance());
    }

    public static ConfigFile getFile() {
        return file;
    }

}
