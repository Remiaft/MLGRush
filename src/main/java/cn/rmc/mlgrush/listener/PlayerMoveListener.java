package cn.rmc.mlgrush.listener;

import cn.rmc.mlgrush.Language;
import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.data.PlayerData;
import cn.rmc.mlgrush.util.game.LevelUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    MLGRush plugin = MLGRush.getInstance();
    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        PlayerData pd = MLGRush.getInstance().getPlayerManager().get(p);
        Location location = e.getTo();
        switch (pd.getState()){
            case SPAWN:
                if (location.getY() <= plugin.getMapManager().getLobby().getY() - 30.0D) {
                    pd.tpToLobby();
                }
                break;
            case WAITING:
                pd.respawn();
                break;
            case FIGHTING:
                if(location.getY() <= pd.getGame().getMap().pos1.getY()){
                    if (e.getPlayer().getLastDamageCause() != null) {
                        LevelUtils.get(pd.getOpponent().getPlayer()).addKill();
                        LevelUtils.get(p).addDeath();
                    }else{
                        LevelUtils.get(p).addDeath();
                    }
                    pd.getGame().initplayer(pd);
                    e.getPlayer().setLastDamageCause(null);

                }
                if (Math.abs(e.getTo().getX() - pd.getGame().getMap().spec.getX()) > 30.0D || Math.abs(e.getTo().getZ() - pd.getGame().getMap().spec.getZ()) > 30.0D) {
                    Language lang = Language.get();
                    String prefix = Language.get().translate("prefix");
                    e.getPlayer().sendMessage(prefix + lang.translate("Message.misc.out"));
                    pd.respawn();
                }

                break;

        }
    }
}
