package cn.rmc.mlgrush.runnable;

import cn.rmc.mlgrush.Language;
import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.data.PlayerData;
import cn.rmc.mlgrush.util.ReplaceUtils;
import cn.rmc.mlgrush.util.ScoreBoardUtils;
import cn.rmc.mlgrush.util.game.LevelUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreBoardRunnable implements Runnable {
    @Override
    public void run() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd");
        String format = simpleDateFormat.format(new Date());
        for(Player p : Bukkit.getOnlinePlayers()){
            PlayerData pd = MLGRush.getInstance().getPlayerManager().get(p);
            LevelUtils level = LevelUtils.get(p);
            Language lang = Language.get();
            switch (pd.getState()){
                case SPAWN:
                    new ScoreBoardUtils().SidebarDisplay(p,ReplaceUtils.replacelobby(p,lang.translateList("SBLobby").toArray(new String[0])));
                    break;
                case FIGHTING:
                    new ScoreBoardUtils().SidebarDisplay(p,ReplaceUtils.replaceFight(pd,lang.translateList("SBGame").toArray(new String[0])));
                    break;
                case SPECTATING:
                    new ScoreBoardUtils().SidebarDisplay(p,ReplaceUtils.replaceSpecFight(pd,lang.translateList("SBSpec").toArray(new String[0])));
                    break;
            }


        }
    }
}
