package me.sores.arrow.util.scoreboard;

import me.sores.arrow.Arrow;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.handler.Handler;
import me.sores.impulse.util.scoreboard.Assemble;
import me.sores.impulse.util.scoreboard.AssembleBoard;
import me.sores.impulse.util.scoreboard.AssembleStyle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/20/2021.
 */
public class BoardHandler extends Handler {

    private static BoardHandler instance;

    public BoardHandler() {
        instance = this;
    }

    @Override
    public void init() {
        Assemble assemble = new Assemble(Arrow.getInstance(), new ArrowAdapter());
        assemble.setAssembleStyle(AssembleStyle.MODERN);
        assemble.setTicks(2);

        if(Bukkit.getOnlinePlayers().size() != 0){
            cleanBoards();
        }
    }

    @Override
    public void unload() {

    }

    public void addBoard(Player player){
        Assemble.getInstance().getBoards().put(player.getUniqueId(), new AssembleBoard(player));
    }

    public void removeBoard(Player player){
        Assemble.getInstance().getBoards().remove(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void cleanBoards(){
        Bukkit.getOnlinePlayers().forEach(player -> {
            ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

            removeBoard(player);
            addBoard(player);

            if(!profile.isScoreboard()) profile.hideScoreboard();
        });
    }

    public static BoardHandler getInstance() {
        return instance;
    }
}
