package me.sores.arrow.kit;

import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.profile.ArrowProfile;
import me.sores.arrow.util.profile.ProfileHandler;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.serialization.InventorySerialization;
import me.sores.impulse.util.serialization.PotionEffectSerialization;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/20/2021.
 */
public class Kit {

    private String name, displayName, potions;
    private Material icon;
    private Loadout loadout;
    private boolean enabled, pub;
    private Ability registeredAbility;

    public Kit(String name) {
        this.name = name;
        enabled = true;
        pub = false;

        loadout = new Loadout();
    }

    public void apply(Player player){
        ArrowProfile profile = ProfileHandler.getInstance().getFrom(player.getUniqueId());

        if(profile.hasKit()){
            MessageUtil.message(player, ChatColor.RED + "You already have a kit selected.");
            player.closeInventory();
            return;
        }

        if(!isEnabled()){
            MessageUtil.message(player, ChatColor.RED + "This kit is currently disabled.");
            player.closeInventory();
            return;
        }

        ArrowUtil.resetPlayer(player);

        player.getInventory().setArmorContents(getLoadout().getArmor());
        player.getInventory().setContents(getLoadout().getContents());

        if(getPotions() != null && !getPotions().isEmpty()){
            PotionEffectSerialization.addPotionEffects(getPotions(), player);
        }

        for(int i = 0; i < player.getInventory().getSize(); i++){
            player.getInventory().addItem(profile.getHealingItem().getItem());
        }

        profile.setSelectedKit(this);
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        KitsHandler.getInstance().makeConfigChange("kits." + getName() + ".display", displayName);
    }

    public String getPotions() {
        return potions;
    }

    public void setPotions(String potions) {
        this.potions = potions;
        KitsHandler.getInstance().makeConfigChange("kits." + getName() + ".potions", potions);
    }

    public Material getIcon() {
        return icon;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
        KitsHandler.getInstance().makeConfigChange("kits." + getName() + ".icon", icon.toString());
    }

    public Loadout getLoadout() {
        return loadout;
    }

    public void setLoadout(Loadout loadout) {
        this.loadout = loadout;
        KitsHandler.getInstance().makeConfigChange("kits." + getName() + ".loadout.armor", InventorySerialization.serializeInventory(loadout.getArmor()));
        KitsHandler.getInstance().makeConfigChange("kits." + getName() + ".loadout.contents", InventorySerialization.serializeInventory(loadout.getContents()));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        KitsHandler.getInstance().makeConfigChange("kits." + getName() + ".enabled", enabled);
    }

    public boolean isPub() {
        return pub;
    }

    public void setPub(boolean pub) {
        this.pub = pub;
        KitsHandler.getInstance().makeConfigChange("kits." + getName() + ".public", pub);
    }

    public Ability getRegisteredAbility() {
        return registeredAbility;
    }

    public void setRegisteredAbility(Ability registeredAbility) {
        this.registeredAbility = registeredAbility;
        if(registeredAbility == null) KitsHandler.getInstance().makeConfigChange("kits." + getName() + ".ability", null);
        KitsHandler.getInstance().makeConfigChange("kits." + getName() + ".ability", registeredAbility.getType().toString());
    }

}
