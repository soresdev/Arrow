package me.sores.arrow.util.killstreaks;

import me.sores.arrow.util.profile.ArrowProfile;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/22/2021.
 */
public abstract class Killstreak {

    private final KillstreakType type;

    public Killstreak(KillstreakType type) {
        this.type = type;
    }

    public abstract void execute(ArrowProfile profile, Player player);

    public KillstreakType getType() {
        return type;
    }

}
