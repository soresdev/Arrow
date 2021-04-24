package me.sores.arrow.util.profile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBList;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import me.sores.arrow.Init;
import me.sores.arrow.config.ArrowConfig;
import me.sores.arrow.kit.Kit;
import me.sores.arrow.util.ArrowUtil;
import me.sores.arrow.util.chatcolors.ArrowChatColor;
import me.sores.arrow.util.enumerations.HealingItem;
import me.sores.arrow.util.killstreaks.KillstreakTier;
import me.sores.arrow.util.killstreaks.KillstreakType;
import me.sores.arrow.util.prefixes.ChatPrefix;
import me.sores.arrow.util.prefixes.ChatPrefixColor;
import me.sores.arrow.util.scoreboard.BoardHandler;
import me.sores.arrow.util.shop.ShopItems;
import me.sores.arrow.util.theme.Theme;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.json.JSONObject;
import me.sores.impulse.util.profile.PlayerProfile;
import org.bson.Document;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sores on 4/20/2021.
 */
public class ArrowProfile extends PlayerProfile {

    /**
     * Saved Data
     */
    private String previousKit;
    private int coins, kills, deaths, streak;
    private boolean scoreboard, bloodEffect;
    private HealingItem healingItem;

    private Theme selectedTheme;
    private ArrowChatColor selectedChatColor;
    private ChatPrefix.Prefix selectedPrefix;
    private ChatPrefixColor selectedPrefixColor;

    private Map<KillstreakTier, KillstreakType> selectedStreaks = Maps.newHashMap();
    private List<ShopItems> shopItems = Lists.newArrayList();

    /**
     * Non-Saved Data
     */
    private Kit selectedKit;
    private boolean building;
    private long combatTimer = -1, lastPearlThrow = -1;
    private EnderPearl lastPearl;

    public ArrowProfile(UUID uuid) {
        super(uuid);
        this.selectedKit = null;
        this.previousKit = null;

        coins = 0;
        kills = 0;
        deaths = 0;
        streak = 0;
        healingItem = HealingItem.SOUP;
        scoreboard = true;
        bloodEffect = false;

        selectedTheme = ArrowConfig.defaultTheme;
        selectedChatColor = ArrowChatColor.RESET;
        selectedPrefix = null;
        selectedPrefixColor = null;

        building = false;
    }

    public void clean(){
        setCoins(0);
        setKills(0);
        setDeaths(0);
        resetStreak();
        setPreviousKit(null);

        shopItems.clear();

        ProfileHandler.getInstance().save(this);
    }

    @Override
    public void saveData() {
        try{
            MongoCollection<Document> collection = Init.getInstance().getMongoBase().getCollection();
            Document fetched = fetchCurrentObject();

            if(fetched != null){
                collection.replaceOne(fetched, createDocument());
            }else{
                collection.insertOne(createDocument());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        Document fetched = fetchCurrentObject();

        if(fetched != null){
            try{

                if(fetched.containsKey("coins")) coins = fetched.getInteger("coins");
                if(fetched.containsKey("kills")) kills = fetched.getInteger("kills");
                if(fetched.containsKey("deaths")) deaths = fetched.getInteger("deaths");
                if(fetched.containsKey("streak")) streak = fetched.getInteger("streak");
                if(fetched.containsKey("previouskit")) previousKit = fetched.getString("previouskit");

                if(fetched.containsKey("scoreboard")) scoreboard = fetched.getBoolean("scoreboard");
                if(fetched.containsKey("blood_effect")) bloodEffect = fetched.getBoolean("blood_effect");
                if(fetched.containsKey("healing_item")) healingItem = HealingItem.valueOf(fetched.getString("healing_item"));
                if(fetched.containsKey("theme")) selectedTheme = ArrowConfig.getTheme(fetched.getString("theme"));

                if(fetched.containsKey("chatcolor")){
                    try{
                        selectedChatColor = ArrowChatColor.valueOf(fetched.getString("chatcolor"));
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                if(fetched.containsKey("prefix")){
                    try{
                        selectedPrefix = ChatPrefix.valueOf(fetched.getString("prefix"));
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                if(fetched.containsKey("prefix_color")){
                    try{
                        selectedPrefixColor = ChatPrefixColor.valueOf(fetched.getString("prefix_color"));
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                if(fetched.containsKey("shopitems")){
                    List<ShopItems> items = (List<ShopItems>) fetched.get("shopitems");

                    for(Object object : items){
                        String key = (String) object;
                        if(key.equalsIgnoreCase("_id")) continue;

                        try{
                            ShopItems item = ShopItems.valueOf(key);

                            if(item != null && !shopItems.contains(item)){
                                shopItems.add(item);
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }

                if(fetched.containsKey("selectedstreaks")){
                    try{
                        JSONObject object = new JSONObject(fetched.getString("selectedstreaks"));

                        for(Map.Entry<String, Object> entry : object.toMap().entrySet()){
                            String ti = entry.getKey();
                            String ty = (String) entry.getValue();

                            try{
                                KillstreakTier tier = KillstreakTier.valueOf(ti);
                                KillstreakType type = KillstreakType.valueOf(ty);

                                if(tier != null){
                                    if(type != null){
                                        selectedStreaks.put(tier, type);
                                    }else{
                                        selectedStreaks.put(tier, tier.getDefaultStreak());
                                    }
                                }
                            }catch (Exception ex){
                                continue;
                            }
                        }

                    }catch (Exception ex){
                        setupDefaultStreaks();
                    }
                }else{
                    setupDefaultStreaks();
                }

            }catch (Exception ex){
                StringUtil.log("&c[Arrow] Data load failure for " + getName() + "'s profile.");
                ex.printStackTrace();
            }
        }else{
            saveData();
        }
    }

    @Override
    public Document createDocument() {
        Document document = new Document("_id", getID().toString());

        document.put("name", getName());
        document.put("coins", coins);
        document.put("kills", kills);
        document.put("deaths", deaths);
        document.put("ratio", calculateRatio());
        document.put("streak", streak);
        document.put("previouskit", previousKit);
        document.put("healing_item", healingItem.toString());
        document.put("scoreboard", scoreboard);
        document.put("blood_effect", bloodEffect);
        document.put("theme", selectedTheme.getIndex());

        if(selectedChatColor != null) document.put("chatcolor", selectedChatColor.toString());
        if(selectedPrefix != null) document.put("prefix", selectedPrefix.getIndex());
        if(selectedPrefixColor != null) document.put("prefix_color", selectedPrefixColor.toString());

        if(!selectedStreaks.isEmpty()){
            JSONObject object = new JSONObject();

            for(Map.Entry<KillstreakTier, KillstreakType> entry : selectedStreaks.entrySet()){
                KillstreakTier tier = entry.getKey();
                KillstreakType type = entry.getValue();

                if(tier != null && type != null){
                    object.put(tier.toString(), type.toString());
                }
            }

            document.put("selectedstreaks", object.toString());
        }

        if(!shopItems.isEmpty()){
            BasicDBList items = new BasicDBList();

            for(ShopItems item : shopItems){
                if(!items.contains(item.toString())) items.add(item.toString());
            }

            document.put("shopitems", items);
        }

        return document;
    }

    @Override
    public Document fetchCurrentObject() {
        FindIterable<Document> cursor = Init.getInstance().getMongoBase().getCollection().find(new Document("_id", getID().toString()));

        return cursor.first();
    }


    /**
     * Saved Data methods
     */

    public String getPreviousKit() {
        return previousKit;
    }

    public void setPreviousKit(String previousKit) {
        this.previousKit = previousKit;
    }

    public boolean hasPreviousKit(){
        return previousKit != null && !previousKit.isEmpty();
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKill(){
        setKills(getKills() + 1);
        setStreak(getStreak() + 1);
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addDeath(){
        setDeaths(getDeaths() + 1);
    }

    public Double calculateRatio(){
        if(deaths == 0) return (double) kills;

        return Double.parseDouble(ArrowUtil.decimalFormat.format((double) kills / (double) deaths));
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public boolean hasStreak(){
        return streak != 0;
    }

    public void resetStreak(){
        setStreak(0);
    }

    public boolean isScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(boolean scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void showScoreboard(){
        BoardHandler.getInstance().addBoard(getPlayer());
        setScoreboard(true);
    }

    public void hideScoreboard(){
        BoardHandler.getInstance().removeBoard(getPlayer());
        setScoreboard(false);
    }

    public boolean isBloodEffect() {
        return bloodEffect;
    }

    public void setBloodEffect(boolean bloodEffect) {
        this.bloodEffect = bloodEffect;
    }

    public HealingItem getHealingItem() {
        return healingItem;
    }

    public void setHealingItem(HealingItem healingItem) {
        this.healingItem = healingItem;
    }

    public Theme getSelectedTheme() {
        return selectedTheme;
    }

    public void setSelectedTheme(Theme selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    public ArrowChatColor getSelectedChatColor() {
        return selectedChatColor;
    }

    public void setSelectedChatColor(ArrowChatColor selectedChatColor) {
        this.selectedChatColor = selectedChatColor;
    }

    public ChatPrefix.Prefix getSelectedPrefix() {
        return selectedPrefix;
    }

    public void setSelectedPrefix(ChatPrefix.Prefix selectedPrefix) {
        this.selectedPrefix = selectedPrefix;
    }

    public ChatPrefixColor getSelectedPrefixColor() {
        return selectedPrefixColor;
    }

    public void setSelectedPrefixColor(ChatPrefixColor selectedPrefixColor) {
        this.selectedPrefixColor = selectedPrefixColor;
    }

    public void setStreakTier(KillstreakTier tier, KillstreakType type){
        selectedStreaks.put(tier, type);
    }

    public KillstreakType getSelectedStreak(KillstreakTier tier){
        return selectedStreaks.get(tier);
    }

    public void setupDefaultStreaks(){
        for(KillstreakTier tier : KillstreakTier.values()) selectedStreaks.put(tier, tier.getDefaultStreak());
    }

    public List<ShopItems> getShopItems() {
        return shopItems;
    }

    public void addShopItem(ShopItems item){
        if(!shopItems.contains(item)) shopItems.add(item);
    }

    public void removeShopItem(ShopItems item){
        shopItems.remove(item);
    }

    public boolean hasShopItem(ShopItems item){
        return shopItems.contains(item);
    }

    /**
     * Non-Saved Data methods
     */

    public Kit getSelectedKit() {
        return selectedKit;
    }

    public void setSelectedKit(Kit selectedKit) {
        this.selectedKit = selectedKit;
    }

    public boolean hasKit(){
        return selectedKit != null;
    }

    public void clearKit(Player player){
        Kit old = getSelectedKit();
        if(getSelectedKit().getRegisteredAbility() != null) getSelectedKit().getRegisteredAbility().destroy(player);

        setPreviousKit(old.getName());
        setSelectedKit(null);
        ArrowUtil.clean(player);
    }

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }

    public long getCombatTimer() {
        return combatTimer;
    }

    public void setCombatTimer(long combatTimer) {
        this.combatTimer = combatTimer;
    }

    public boolean inCombat(){
        return combatTimer > System.currentTimeMillis();
    }

    public void enterCombat(long time){
        setCombatTimer(System.currentTimeMillis() + time);
        if(time <= 0) this.combatTimer = -1;
    }

    public long getLastPearlThrow() {
        return lastPearlThrow;
    }

    public void setLastPearlThrow(long lastPearlThrow) {
        this.lastPearlThrow = lastPearlThrow;
    }

    public boolean hasPearlCooldown(){
        return lastPearlThrow > System.currentTimeMillis();
    }

    public EnderPearl getLastPearl() {
        return lastPearl;
    }

    public void setLastPearl(EnderPearl lastPearl) {
        this.lastPearl = lastPearl;
    }

    public void cleanPearl(Player player){
        cleanPearl(player.getUniqueId());
    }

    public void cleanPearl(UUID uuid){
        if(lastPearl != null && !lastPearl.isDead()) lastPearl.remove();
        lastPearlThrow = -1;
    }

}
