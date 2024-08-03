package cn.rmc.mlgrush;

import cn.rmc.mlgrush.util.Config;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Language {


    private Language() {
    }

    public String translate(String s) {
        String result = MLGRush.getLang().getString(s);
        if(result == null){
            return "MESSGAE_NOT_FOUND";
        }else{
            return MLGRush.getLang().getString(s).replace("&", "§");
        }



    }
    public List<String> translateList(String s) {
        List<String> result = MLGRush.getLang().getStringList(s);
        if(result == null){
            return Arrays.asList("null");
        }else{
            return MLGRush.getLang().getStringList(s);
        }

    }
    public static Language get(){
        return new Language();
    }
}
