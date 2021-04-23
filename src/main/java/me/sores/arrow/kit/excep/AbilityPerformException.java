package me.sores.arrow.kit.excep;

import me.sores.arrow.kit.Ability;
import me.sores.arrow.util.profile.ArrowProfile;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/22/2021.
 */
public class AbilityPerformException extends Exception {

    private final Player player;
    private final ArrowProfile profile;
    private final Ability ability;

    public AbilityPerformException(Player player, ArrowProfile profile, Ability ability, String message) {
        super(message);
        this.player = player;
        this.profile = profile;
        this.ability = ability;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrowProfile getProfile() {
        return profile;
    }

    public Ability getAbility() {
        return ability;
    }
}
