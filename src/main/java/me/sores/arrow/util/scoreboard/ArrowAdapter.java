package me.sores.arrow.util.scoreboard;

import com.google.common.collect.Lists;
import me.sores.arrow.config.ArrowConfig;
import me.sores.arrow.kit.AbilityHandler;
import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.scoreboard.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by sores on 4/20/2021.
 */
public class ArrowAdapter implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        return profile.getSelectedTheme().getPrimary() + ArrowConfig.SCOREBOARD_TITLE;
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = Lists.newArrayList();
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());
        String sec = profile.getSelectedTheme().getSecondary(), val = profile.getSelectedTheme().getValue();

        lines.add(ArrowUtil.SCOREBOARD_SPACER);

        lines.add(sec + "Coins: " + val + profile.getCoins());
        lines.add(sec + "Kills: " + val + profile.getKills());
        lines.add(sec + "Deaths: " + val + profile.getDeaths());
        lines.add(sec + "Ratio: " + val + profile.calculateRatio());
        if(profile.hasStreak()) lines.add(sec + "Streak: " + val + profile.getStreak());

        lines.add(ArrowUtil.SCOREBOARD_SPACER);

        if(profile.inCombat()) lines.add(sec + "Combat: " + val + (profile.getCombatTimer() > System.currentTimeMillis() ? ArrowUtil.combatFormat.format(
                (profile.getCombatTimer() - System.currentTimeMillis()) / 1000.0) + "s" : ""));

        if(profile.hasPearlCooldown()) lines.add(sec + "Pearl: " + val + (profile.getLastPearlThrow() > System.currentTimeMillis() ? ArrowUtil.combatFormat.format((
                profile.getLastPearlThrow() - System.currentTimeMillis()) / 1000.0) + "s" : ""));

       if(AbilityHandler.getInstance().isOnCooldown(player)) lines.add(sec + profile.getSelectedKit().getRegisteredAbility().getType().getDisplay() + ": "
       + val + (AbilityHandler.getInstance().getCooldownTime(player) > System.currentTimeMillis() ? ArrowUtil.combatFormat.format((AbilityHandler.getInstance()
       .getCooldownTime(player) - System.currentTimeMillis()) / 1000.0) + "s" : ""));

        return StringUtil.color(lines);
    }

}
