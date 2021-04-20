package me.sores.arrow.kit;

import me.sores.arrow.kit.abilities.Ability_fisherman;
import me.sores.arrow.kit.abilities.Ability_switcher;

/**
 * Created by sores on 4/20/2021.
 */
public enum AbilityType {

    FISHERMAN(Ability_fisherman.class, "Fisherman"),
    SWITCHER(Ability_switcher.class, "Switcher");

    private Class<? extends Ability> clazz;
    private String display;

    AbilityType(Class<? extends Ability> clazz, String display) {
        this.clazz = clazz;
        this.display = display;
    }

    public static String toPrettyList(){
        StringBuilder builder = new StringBuilder();

        for(AbilityType type : values()){
            if(builder.length() == 0){
                builder.append(type.toString());
            }else{
                builder.append(", " + type.toString());
            }
        }

        return builder.toString();
    }

    public String getDisplay() {
        return display;
    }
}
