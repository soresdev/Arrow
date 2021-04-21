package me.sores.arrow.util.region;

/**
 * Created by sores on 4/21/2021.
 */
public enum RegionType {

    SPAWN,
    OTHER;

    public static String toPrettyList(){
        StringBuilder builder = new StringBuilder();

        for(RegionType type : values()){
            if(builder.length() == 0){
                builder.append(type.toString());
            }else{
                builder.append(", " + type.toString());
            }
        }

        return builder.toString();
    }

}
