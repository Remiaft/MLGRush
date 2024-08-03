package cn.rmc.mlgrush.listener;

import cn.rmc.mlgrush.Language;
import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.data.Game;
import cn.rmc.mlgrush.data.PlayerData;
import cn.rmc.mlgrush.enums.Team;
import cn.rmc.mlgrush.util.MathUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        PlayerData pd = MLGRush.getInstance().getPlayerManager().get(p);
        Language lang = Language.get();
        String prefix = Language.get().translate("prefix");
        if(p.hasPermission("rushbed.admin") && p.getGameMode() == GameMode.CREATIVE) return;
        switch (pd.getState()){
            case FIGHTING:
                Game g = pd.getGame();
                if(e.getBlock().getType() == Material.BED_BLOCK){
                    if (MathUtils.getDistance(e.getBlock().getLocation(), g.getMap().blueBed) < MathUtils.getDistance(e.getBlock().getLocation(), g.getMap().redBed)) {
                        if (pd.getTeam() == Team.BLUE) {
                            e.getPlayer().sendMessage(prefix + lang.translate("Message.misc.bedself"));
                        }else{
                            pd.getGame().newRound(pd);
                        }
                    }
                    if (MathUtils.getDistance(e.getBlock().getLocation(), g.getMap().redBed) < MathUtils.getDistance(e.getBlock().getLocation(), g.getMap().blueBed)) {
                        if (pd.getTeam() == Team.RED) {
                            e.getPlayer().sendMessage(prefix + lang.translate("Message.misc.bedself"));
                        }else{
                            pd.getGame().newRound(pd);
                        }
                    }
                    e.setCancelled(true);
                }
                if(g.getBlock().contains(e.getBlock())){
                    e.getBlock().getDrops().clear();
                    g.removeBlock(e.getBlock());
                }else{
                    e.setCancelled(true);
                }
                break;
            default:
                e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        PlayerData pd = MLGRush.getInstance().getPlayerManager().get(p);
        if(p.hasPermission("rushbed.admin") && p.getGameMode() == GameMode.CREATIVE) return;
        switch (pd.getState()){
            case FIGHTING:
                Game g = pd.getGame();
                if(e.getBlock().getLocation().getY() > g.getMap().redSpawn.getY()){
                    e.setCancelled(true);
                    return;
                }
                g.addBlock(e.getBlock());

                break;
            default:
                e.setCancelled(true);
        }
    }
    @EventHandler
    public void onWeather(WeatherChangeEvent e){
        e.setCancelled(true);
    }
}
