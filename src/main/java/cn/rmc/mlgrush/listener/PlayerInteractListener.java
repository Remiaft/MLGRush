package cn.rmc.mlgrush.listener;

import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.data.PlayerData;
import cn.rmc.mlgrush.inventory.KitEditorMenu;
import cn.rmc.mlgrush.inventory.SpecMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onInt(PlayerInteractEvent e){
        Player p = e.getPlayer();
        PlayerData pd = MLGRush.getInstance().getPlayerManager().get(p);
        try {
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                switch (pd.getState()) {
                    case SPAWN:
                        e.setCancelled(true);
                        switch (e.getItem().getType()) {
                            case GOLD_SWORD:
                                MLGRush.getInstance().getQueueManager().add(p);
                                break;
                            case BOOK:
                                KitEditorMenu.open(p);
                                break;
                            case ENDER_PEARL:
                                SpecMenu.open(p);
                        }
                    case QUEUE:
                        e.setCancelled(true);
                        switch (e.getItem().getType()) {
                            case INK_SACK:
                                MLGRush.getInstance().getQueueManager().remove(p);
                                break;
                        }
                    case SPECTATING:
                        e.setCancelled(true);
                        switch (e.getItem().getType()) {
                            case NETHER_STAR:
                                pd.getSpecingGame().removewatcher(pd);
                        }
                }

            }
        }catch (NullPointerException exception){}
    }
}
