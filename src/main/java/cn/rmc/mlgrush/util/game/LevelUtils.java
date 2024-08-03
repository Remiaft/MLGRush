package cn.rmc.mlgrush.util.game;

import cn.rmc.mlgrush.MLGRush;
import org.bukkit.entity.Player;

public class LevelUtils {
    private Player p;

    public LevelUtils(Player p){
        this.p = p;
    }
    public long getNextPx(int lv) {
        return (10 + lv * 20);
    }

    public void addPx(long px) {
        long x = MLGRush.getPlayer().getLong("players." + p.getName() + ".px");
        int lv = MLGRush.getPlayer().getInt("players." + p.getName() + ".lv");
        if (px + x >= getNextPx(lv)) {
            MLGRush.getPlayer().set("players." + p.getName() + ".lv", lv + 1);
            MLGRush.getPlayer().set("players." + p.getName() + ".px", px + x - getNextPx(lv));
        } else {
            MLGRush.getPlayer().set("players." + p.getName() + ".px", px + x);
        }
    }
    public void addWin(){
        int before = MLGRush.getPlayer().getInt("players."+p.getName()+".wins");
        MLGRush.getPlayer().set("players."+p.getName()+".wins",before+1);
    }
    public void addLose(){
        int before = MLGRush.getPlayer().getInt("players."+p.getName()+".loses");
        MLGRush.getPlayer().set("players."+p.getName()+".loses",before+1);
    }
    public void addBreak(){
        int before = MLGRush.getPlayer().getInt("players."+p.getName()+".breaks");
        MLGRush.getPlayer().set("players."+p.getName()+".breaks",before+1);
    }
    public void addBroken(){
        int before = MLGRush.getPlayer().getInt("players."+p.getName()+".brokens");
        MLGRush.getPlayer().set("players."+p.getName()+".brokens",before+1);
    }
    public void addKill(){
        int before = MLGRush.getPlayer().getInt("players."+p.getName()+".kills");
        MLGRush.getPlayer().set("players."+p.getName()+".kills",before+1);
    }
    public void addDeath(){
        int before = MLGRush.getPlayer().getInt("players."+p.getName()+".deaths");
        MLGRush.getPlayer().set("players."+p.getName()+".deaths",before+1);
    }
    public int getLevelInt(){
        return MLGRush.getPlayer().getString("players." + p.getName() + ".lv") == null ? 0:MLGRush.getPlayer().getInt("players." + p.getName() + ".lv");
    }
    public String getLevel(){
        long lv = MLGRush.getPlayer().getInt("players." + p.getName() + ".lv");
        String str = String.valueOf(lv);
        if (lv < 10L)
            return "§8[§8" + str + "§8✫]";
        if (lv < 100L)
            return "§7[" + str + "✫]";
        if (lv < 200L)
            return "§r[" + str + "✫]";
        if (lv < 300L)
            return "§6[" + str + "✫]";
        if (lv < 400L)
            return "§2[" + str + "✫]";
        if (lv < 500L)
            return "§e[" + str + "✫]";
        if (lv < 600L)
            return "§3[" + str + "✫]";
        if (lv < 700L)
            return "§4[" + str + "✫]";
        if (lv < 800L)
            return "§8[" + str + "✫]";
        if (lv < 900L) {
            return "§a[" + str + "✫]";
        }
        return "§c[" + str + "✫]";
    }
    public int getWins(){
        return MLGRush.getPlayer().getString("players."+p.getName()+".wins") == null ? 0:MLGRush.getPlayer().getInt("players."+p.getName()+".wins");
    }
    public int getLoses(){
        return MLGRush.getPlayer().getString("players."+p.getName()+".loses") == null ? 0:MLGRush.getPlayer().getInt("players."+p.getName()+".loses");
    }
    public int getBreaks(){
        return MLGRush.getPlayer().getString("players."+p.getName()+".breaks") == null ? 0:MLGRush.getPlayer().getInt("players."+p.getName()+".breaks");
    }
    public int getBrokens(){
        return MLGRush.getPlayer().getString("players."+p.getName()+".brokens") == null ? 0:MLGRush.getPlayer().getInt("players."+p.getName()+".brokens");
    }
    public int getKills(){
        return MLGRush.getPlayer().getString("players."+p.getName()+".kills") == null ? 0:MLGRush.getPlayer().getInt("players."+p.getName()+".kills");
    }
    public int getDeaths(){
        return MLGRush.getPlayer().getString("players."+p.getName()+".deaths") == null ? 0:MLGRush.getPlayer().getInt("players."+p.getName()+".deaths");
    }
    public static LevelUtils get(Player p){
        return new LevelUtils(p);
    }
}
