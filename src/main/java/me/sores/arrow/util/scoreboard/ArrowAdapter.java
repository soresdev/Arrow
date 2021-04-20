package me.sores.arrow.util.scoreboard;

import com.google.common.collect.Lists;
import me.sores.arrow.config.ArrowConfig;
import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.scoreboard.AssembleAdapter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by sores on 4/20/2021.
 */
public class ArrowAdapter implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        return ArrowConfig.SCOREBOARD_TITLE;
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = Lists.newArrayList();

        lines.add(ArrowUtil.SCOREBOARD_SPACER);

        //profile stuff

        lines.add(ArrowUtil.SCOREBOARD_SPACER);

        return StringUtil.color(lines);
    }

}
