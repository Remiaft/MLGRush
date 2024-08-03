package cn.rmc.mlgrush.manager;

import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.runnable.ScoreBoardRunnable;
import org.bukkit.Bukkit;

public class ScoreBoardManager {
    public ScoreBoardManager(){
        Bukkit.getScheduler().runTaskTimer(MLGRush.getInstance(),new ScoreBoardRunnable(),0L,5L);
    }
}
