package cn.rmc.mlgrush.util;

import cn.rmc.mlgrush.Language;
import cn.rmc.mlgrush.data.Game;
import cn.rmc.mlgrush.data.PlayerData;
import cn.rmc.mlgrush.enums.Team;
import cn.rmc.mlgrush.util.game.LevelUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReplaceUtils {
    public static String[] replaceFight(PlayerData p, String[] strings){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd");
        String format = simpleDateFormat.format(new Date());
        Game g = p.getGame();
        String bme = "";
        String rme = "";
        if(p.getTeam() == Team.RED){
            rme = Language.get().translate("You");
        }else if(p.getTeam() == Team.BLUE){
            bme = Language.get().translate("You");
        }
        ArrayList<String> result = new ArrayList<>();
        for(String s : strings){
            s = s.
                    replace("%date%",format).
                    replace("%blue%",String.valueOf(g.getBluescore())).
                    replace("%red%",String.valueOf(g.getRedscore())).
                    replace("%bme%",bme).
                    replace("%rme%",rme);
            s = ChatColor.translateAlternateColorCodes('&',s);
            s = PlaceholderAPI.setPlaceholders(p.getPlayer(),s);
            result.add(s);
        }
        return result.toArray(new String[0]);
    }
    public static String[] replaceSpecFight(PlayerData pd, String[] strings){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd");
        String format = simpleDateFormat.format(new Date());
        ArrayList<String> result = new ArrayList<>();
        for(String s : strings){
            s = s.
                    replace("%date%",format).
                    replace("%blue%",String.valueOf(pd.getSpecingGame().getBluescore())).
                    replace("%red%",String.valueOf(pd.getSpecingGame().getRedscore()));
            s = ChatColor.translateAlternateColorCodes('&',s);
            s = PlaceholderAPI.setPlaceholders(pd.getPlayer(),s);
            result.add(s);
        }
        return result.toArray(new String[0]);
    }
    public static String[] replacelobby(Player p, String[] strings){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd");
        String format = simpleDateFormat.format(new Date());
        ArrayList<String> result = new ArrayList<>();
        LevelUtils level = LevelUtils.get(p);
        for(String s : strings){
            s = s.
                    replace("%date%",format).
                    replace("%kills%",String.valueOf(level.getKills())).
                    replace("%wins%",String.valueOf(level.getWins())).
                    replace("%breaks%",String.valueOf(level.getBreaks())).
                    replace("%online%",String.valueOf(Bukkit.getOnlinePlayers().size()));
            s = ChatColor.translateAlternateColorCodes('&',s);
            s = PlaceholderAPI.setPlaceholders(p.getPlayer(),s);
            result.add(s);
        }
        return result.toArray(new String[0]);
    }
}
