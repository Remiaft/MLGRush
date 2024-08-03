package cn.rmc.mlgrush.util.game;

import cn.rmc.mlgrush.MLGRush;
import org.bukkit.entity.Player;

public class SlotUtils {
    public static int getSlots(Player p, String s, Integer def){
        if(MLGRush.getPlayer().getString("players."+p.getName()+".inventory.stick") != null){
            return MLGRush.getPlayer().getInt("players."+p.getName()+".inventory."+s);
        }else{
            return def;
        }
    }
}
