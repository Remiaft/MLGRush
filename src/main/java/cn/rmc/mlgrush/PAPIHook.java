package cn.rmc.mlgrush;

import cn.rmc.mlgrush.data.PlayerData;
import cn.rmc.mlgrush.util.game.LevelUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PAPIHook extends PlaceholderExpansion {
    @Override
    public String onPlaceholderRequest(Player player, String name) {
        PlayerData u = MLGRush.getInstance().getPlayerManager().get(player);
        if (u == null) return "";
        LevelUtils level = LevelUtils.get(player);
        if(name.equalsIgnoreCase("kills")){
            return String.valueOf(level.getKills());
        }
        if(name.equalsIgnoreCase("breaks")){
            return String.valueOf(level.getBreaks());
        }
        if(name.equalsIgnoreCase("wins")){
            return String.valueOf(level.getWins());
        }
        if(name.equals("lvnow")){
            return String.valueOf(level.getLevelInt());
        }
        if (name.equals("lv")) {
            return level.getLevel();
        }

        return "";
    }
    public String getIdentifier() {
        return "rush";
    }


    public String getAuthor() {
        return "yeoc";
    }


    public String getVersion() {
        return "1.0";
    }
}
