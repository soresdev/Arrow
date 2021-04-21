package me.sores.arrow.config;

import me.sores.arrow.Arrow;
import me.sores.impulse.util.configuration.ConfigFile;

/**
 * Created by sores on 4/21/2021.
 */
public class RegionsConfig {

    private static ConfigFile file;

    public RegionsConfig() {
        file = new ConfigFile("regions.yml", Arrow.getInstance());
    }

    public static ConfigFile getFile() {
        return file;
    }

}
