package me.sores.arrow.kit;

import com.google.common.collect.Lists;
import me.sores.arrow.Arrow;
import me.sores.arrow.kit.excep.AbilityPerformException;
import me.sores.arrow.kit.wrapper.WrapperItem;
import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.IChange;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.arrow.util.region.Region;
import me.sores.arrow.util.region.RegionHandler;
import me.sores.arrow.util.region.RegionType;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.serialization.interf.JsonSerializable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sores on 4/20/2021.
 */
public abstract class Ability implements JsonSerializable, IChange {

    public Ability(AbilityType type) {
        this.type = type;
    }

    public static String ABILITY_EXCEPTION_MESSAGE = ChatColor.RED + "You cannot use that ability here.";

    private AbilityType type;
    private long cooldown = -1;

    public abstract String[] getInfo();

    public void register(){
        AbilityHandler.getInstance().registerAbility(this);
    }

    public static boolean canPerform(Player player, Ability ability) throws AbilityPerformException { //base this off player state instead todo
        if(RegionHandler.getInstance().getRegion(player.getLocation()) != null){
            Region region = RegionHandler.getInstance().getRegion(player.getLocation());

            if(region.getType() == RegionType.SPAWN || !region.isPvp() || !region.isAbilities())
                throw new AbilityPerformException(player, ProfileHandler.getInstance().getFrom(player.getUniqueId()), ability, ABILITY_EXCEPTION_MESSAGE);
        }

        return true;
    }

    public static boolean canAttack(Player player, Player target){
        return !ArrowUtil.isSpawnArea(player) && !ArrowUtil.isSpawnArea(target);
    }

    public void perform(Player player, Ability ability){
        long time = System.currentTimeMillis() + ability.getCooldown();
        if(time <= 0) return;

        AbilityCooldown abilityCooldown = new AbilityCooldown(player, ability, time);
        AbilityHandler.getInstance().addCooldown(player, abilityCooldown);

        abilityCooldown.runTaskTimerAsynchronously(Arrow.getInstance(), 0L, 2L);
    }

    public void sendCooldownMessage(Player player, Ability ability, long expire){
        long exp = TimeUnit.MILLISECONDS.toSeconds(expire - System.currentTimeMillis()) + 1;
        if(exp < 0) return;

        MessageUtil.message(player, ChatColor.RED + "Your " + ability.getType().getDisplay() + " Ability is on cooldown for " + exp + "s.");
    }

    public void destroy(){}

    public void destroy(Player player){
        if(!AbilityHandler.getInstance().getCooldowns().isEmpty() && AbilityHandler.getInstance().getCooldowns().containsKey(player.getUniqueId())){
            AbilityCooldown cooldown = AbilityHandler.getInstance().getCooldowns().get(player.getUniqueId());

            if(cooldown != null){
                if(!cooldown.isCancelled()) cooldown.cancel();
            }

            AbilityHandler.getInstance().getCooldowns().remove(player.getUniqueId());
        }
    }

    public List<Class<? extends WrapperItem>> fetchWrapperItems(){
        if(getClass().getInterfaces().length > 0){
            List<Class<? extends WrapperItem>> items = Lists.newArrayList();

            for(Class<?> wrapper : getClass().getInterfaces()){
                for(Class<?> a : wrapper.getInterfaces()){
                    if(a == WrapperItem.class) items.add((Class<? extends WrapperItem>) wrapper);
                }
            }

            return items;
        }

        return null;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public AbilityType getType() {
        return type;
    }

}
