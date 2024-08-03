package cn.rmc.mlgrush.manager;

import cn.rmc.mlgrush.Language;
import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.data.Game;
import cn.rmc.mlgrush.data.PlayerData;
import cn.rmc.mlgrush.enums.Mode;
import cn.rmc.mlgrush.enums.State;
import cn.rmc.mlgrush.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class QueueManager {
    MLGRush plugin = MLGRush.getInstance();
    ArrayList<PlayerData> data = new ArrayList<>();


    public QueueManager(){
        new BukkitRunnable(){
            @Override
            public void run() {
                detect();
            }
        }.runTaskTimerAsynchronously(MLGRush.getInstance(),0L,40L);

    }

    public Boolean add(Player p){
        PlayerData pd = plugin.getPlayerManager().get(p);
        Language lang = Language.get();
        String prefix = Language.get().translate("prefix");
        if(data.contains(pd)){
            return false;
        }else{
            p.sendMessage(prefix+ lang.translate("Message.queue.add"));
            giveQueueKit(pd);
            pd.setState(State.QUEUE);
            data.add(pd);
            return true;
        }
    }
    public Boolean remove(Player p){

        PlayerData pd = plugin.getPlayerManager().get(p);
        if(data.contains(pd)){
            Language lang = Language.get();
            String prefix = Language.get().translate("prefix");
            pd.setState(State.SPAWN);
            data.remove(pd);
            p.sendMessage(prefix+lang.translate("Message.queue.remove"));
            pd.giveLobbyKit();
            return true;
        }else{
            return false;
        }

    }
    public void detect(){
        if(data.size() >= 2){
            PlayerData p1 = data.get(0);
            data.remove(0);
            PlayerData p2 = data.get(0);
            data.remove(0);
            if(p1 == p2){
                System.out.println("Error!!!!!!!");
                return;
            }
            new Game(p1,p2,plugin.getMapManager().getMap(), Mode.UNRANKED);
        }
    }
    public static void giveQueueKit(PlayerData pd){
        Player p = pd.getPlayer();
        Language lang = Language.get();
        p.getInventory().clear();
        p.getInventory().setItem(8,new ItemBuilder(Material.INK_SACK,1,(byte) 1).setName(lang.translate("Items.queueleave")).toItemStack());
    }

}
