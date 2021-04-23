package me.sores.arrow.util.killstreaks;

import com.google.common.collect.Maps;
import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.PlayerUtil;
import me.sores.impulse.util.handler.Handler;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by sores on 4/22/2021.
 */
public class KillstreakHandler extends Handler {
    
    private static KillstreakHandler instance;
    
    private Map<KillstreakType, Killstreak> killstreaks;
    private Map<Integer, KillstreakTier> tiers;

    public static String PREFIX = "&8(&6&lStreak&8) ";

    public KillstreakHandler() {
        instance = this;
    }

    @Override//todo add more streaks, and listeners
    public void init() {
        killstreaks = Maps.newHashMap();
        tiers = Maps.newHashMap();

        registerStreak(new Killstreak(KillstreakType.X1_GAPPLE) {
            @Override
            public void execute(ArrowProfile profile, Player player) {
                if(profile.getSelectedKit() == null || player.isDead()) return;

                ArrowUtil.giveItems(player, new ItemBuilder(Material.GOLDEN_APPLE).build());
            }
        });

        registerStreak(new Killstreak(KillstreakType.REFILL) {
            @Override
            public void execute(ArrowProfile profile, Player player) {
                if(profile.getSelectedKit() == null || player.isDead()) return;

                for(int i = 0; i < player.getInventory().getSize(); i++){
                    player.getInventory().addItem(profile.getHealingItem().getItem());
                }
            }
        });

        registerStreak(new Killstreak(KillstreakType.X3_GAPPLE) {
            @Override
            public void execute(ArrowProfile profile, Player player) {
                if(profile.getSelectedKit() == null || player.isDead()) return;

                ArrowUtil.giveItems(player, new ItemBuilder(Material.GOLDEN_APPLE).setAmount(3).build());
            }
        });

        registerStreak(new Killstreak(KillstreakType.ARMOR_REPAIR) {
            @Override
            public void execute(ArrowProfile profile, Player player) {
                if(profile.getSelectedKit() == null || player.isDead()) return;

                ArrowUtil.repairPlayerArmor(player);
            }
        });

        registerStreak(new Killstreak(KillstreakType.GOD_APPLE) {
            @Override
            public void execute(ArrowProfile profile, Player player) {
                if(profile.getSelectedKit() == null || player.isDead()) return;

                ArrowUtil.giveItems(player, new ItemBuilder(Material.GOLDEN_APPLE).setData((short) 1).build());
            }
        });

        registerStreak(new Killstreak(KillstreakType.EXTRA_HEARTS) {
            @Override
            public void execute(ArrowProfile profile, Player player) {
                if(profile.getSelectedKit() == null || player.isDead()) return;

                player.setMaxHealth(26.0D);
                player.setHealth(player.getMaxHealth());
            }
        });
        
        for(KillstreakTier tier : KillstreakTier.values()){
            tiers.put(tier.getKillsNeeded(), tier);
        }

        for(KillstreakType type : KillstreakType.values()){
            if(type.isDef()) type.getTier().setDefaultStreak(type);
        }
        
    }

    @Override
    public void unload() {
        killstreaks.clear();
        tiers.clear();
    }

    private void registerStreak(Killstreak killstreak){
        killstreaks.put(killstreak.getType(), killstreak);
    }

    public void announceStreakItem(Player player, KillstreakType type){
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        MessageUtil.message(PlayerUtil.getOnlinePlayers(), PREFIX + "&a" + player.getName() + " &7has received " + type.getDisplay() +
                " &7for gaining a &6" + profile.getStreak() + " &7killstreak!");
    }

    public void announceStreakEnd(Player killer, Player dead){
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(dead.getUniqueId());

        MessageUtil.message(PlayerUtil.getOnlinePlayers(), PREFIX + "&a" + killer.getName() + " &7has ended &a" + dead.getName()
        + "'s &6" + profile.getStreak() + " &7killstreak!");
    }

    public boolean hasSubstantialStreak(ArrowProfile profile){
        return profile.getStreak() >= 6;
    }
    
    public Map<KillstreakType, Killstreak> getKillstreaks() {
        return killstreaks;
    }

    public Map<Integer, KillstreakTier> getTiers() {
        return tiers;
    }

    public static KillstreakHandler getInstance() {
        return instance;
    }
}
