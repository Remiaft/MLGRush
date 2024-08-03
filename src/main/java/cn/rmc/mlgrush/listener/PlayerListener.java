package cn.rmc.mlgrush.listener;

import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.data.PlayerData;
import cn.rmc.mlgrush.enums.State;
import cn.rmc.mlgrush.manager.QueueManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        MLGRush.getInstance().getPlayerManager().Create(p);
        PlayerData pd = MLGRush.getInstance().getPlayerManager().get(p);
        pd.tpToLobby();
        pd.setState(State.SPAWN);
        pd.giveLobbyKit();
        p.setHealth(20);
        p.setFoodLevel(20);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage(null);
        Player p = e.getPlayer();
        PlayerData pd = MLGRush.getInstance().getPlayerManager().get(p);
        switch (pd.getState()){
            case QUEUE:
                MLGRush.getInstance().getQueueManager().remove(p);
                break;
            case WAITING:
                pd.getOpponent().getPlayer().sendMessage("对方退出游戏,停止倒计时");
                pd.getGame().getWaitTask().cancel();
                pd.getGame().close();
                break;
            case FIGHTING:
                pd.getGame().clearBlock();
                pd.getGame().close();
                break;
            case SPECTATING:
                pd.getSpecingGame().removewatcher(pd);
                break;
            case SPAWN:
                break;
        }
    }
    @EventHandler
    public void onHungry(FoodLevelChangeEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        e.setCancelled(true);
        p.setFoodLevel(20);
    }
    @EventHandler
    public void onDamagebyEntity(EntityDamageByEntityEvent e){
        if((!(e.getEntity() instanceof Player)) || (!(e.getDamager() instanceof Player))) return;
        Player p = (Player) e.getEntity();
        PlayerData pd = MLGRush.getInstance().getPlayerManager().get(p);
        Player causer = (Player) e.getDamager();
        PlayerData pdd = MLGRush.getInstance().getPlayerManager().get(causer);
        if(pdd.getState() != State.FIGHTING || pd.getState() != State.FIGHTING){
            e.setCancelled(true);
        }else{
            e.setDamage(0);
        }
    }
    @EventHandler
    public void onFall(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        if(e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) e.setCancelled(true);
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        e.setCancelled(true);
    }
    @EventHandler
    public void onClick(InventoryClickEvent e){
        try {
            if(e.getClickedInventory().getType() == InventoryType.PLAYER && MLGRush.getInstance().getPlayerManager().get((Player) e.getWhoClicked()).getState() != State.FIGHTING && e.getWhoClicked().getGameMode() != GameMode.CREATIVE)
                e.setCancelled(true);
        }catch (NullPointerException exception){}

    }
    /*
    @EventHandler
    public void onChangeLang(LanguageUpdateEvent e){
        PlayerData pd = MLGRush.getInstance().getPlayerManager().get(e.getPlayer());
        switch (e.getType()){
            case Before:
                break;
            case After:
                switch (pd.getState()){
                    case SPAWN:
                        pd.giveLobbyKit();
                        break;
                    case SPECTATING:
                        pd.getSpecingGame().giveWatcherKit(pd);
                        break;
                    case QUEUE:
                        QueueManager.giveQueueKit(pd);
                        break;
                }
                break;
        }



    }

     */

}
