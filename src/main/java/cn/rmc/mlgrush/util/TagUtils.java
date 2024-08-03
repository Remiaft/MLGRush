package cn.rmc.mlgrush.util;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TagUtils {
    public static void setTag(Player p,Player target, String prefix, String suffix) {
        if (prefix.length() > 16) {
            prefix = prefix.substring(0, 16);
        }
        if (suffix.length() > 16) {
            suffix = suffix.substring(0, 16);
        }
            Scoreboard board = target.getScoreboard();
            Team t = board.getTeam(p.getName());
            if (t == null) {
                t = board.registerNewTeam(p.getName());
                t.setPrefix(prefix);
                t.setSuffix(suffix);
                t.addPlayer(p);
                return;
            }
            t.setPrefix(prefix);
            t.setSuffix(suffix);
            if (!t.hasPlayer(p)) {
                t.addPlayer(p);
            }
    }

    public static void unregisterTag(Player p,Player target) {
            Scoreboard board = p.getScoreboard();
            if (board != null && board.getPlayerTeam(target) != null)
                board.getPlayerTeam(target).unregister();
    }
}
