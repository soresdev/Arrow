package me.sores.arrow.kit.menu;

import com.google.common.collect.Lists;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.kit.Loadout;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.impulse.util.ItemBuilder;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.menu.Button;
import me.sores.impulse.util.menu.Menu;
import me.sores.impulse.util.menu.buttons.BackButton;
import me.sores.impulse.util.menu.buttons.DisplayButton;
import me.sores.impulse.util.serialization.PotionEffectSerialization;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * Created by sores on 4/20/2021.
 */
public class KitPreview extends Menu {

    private Player player;
    private ArrowProfile profile;
    private Kit kit;

    public KitPreview(Player player, ArrowProfile profile, Kit kit) {
        this.player = player;
        this.profile = profile;
        this.kit = kit;

        setAutoUpdate(true);
        setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return "&aPreviewing " + kit.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Loadout loadout = kit.getLoadout();

        for(int i = 0; i < 36; i++){
            buttons.put(i, createPreviewItem(loadout.getContents()[i]));
        }

        buttons.put(41, createPreviewItem(loadout.getArmor()[3]));
        buttons.put(42, createPreviewItem(loadout.getArmor()[2]));
        buttons.put(43, createPreviewItem(loadout.getArmor()[1]));
        buttons.put(44, createPreviewItem(loadout.getArmor()[0]));

        buttons.put(45, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER).setName("&6Ability Information").setLore(Arrays.asList(
                        StringUtil.color("&eRegistered Ability: &r" + (kit.getRegisteredAbility() != null ? kit.getRegisteredAbility().getType().getDisplay() : "None")),
                        StringUtil.color("&eAbility Cooldown: &r" + (kit.getRegisteredAbility() != null ? kit.getRegisteredAbility().getCooldown() : "None")))).build();
            }
        });

        try{
            Collection<PotionEffect> effects = PotionEffectSerialization.getPotionEffects(kit.getPotions());
            List<String> lore = Lists.newArrayList();

            for(PotionEffect effect : effects){
                lore.add(StringUtil.color("&7- &r" + effect.getType().getName() + " " + (effect.getAmplifier() + 1)));
            }

            ItemStack item = new ItemBuilder(effects.isEmpty() ? Material.GLASS_BOTTLE : Material.POTION).setName("&6Effects: ").setLore(lore).build();

            buttons.put(46, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return item;
                }
            });

        }catch (Exception ex){

        }

        buttons.put(53, new BackButton(new KitSelectorMenu(player, profile)));

        return buttons;
    }

    private DisplayButton createPreviewItem(ItemStack item){
        return new DisplayButton(item, true);
    }

}
