package cn.rmc.mlgrush.manager;

import cn.rmc.mlgrush.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private HashMap<UUID, PlayerData> data = new HashMap<>();

    public PlayerData get(Player p){
        return data.get(p.getUniqueId());
    }
    public void Create(Player p){
        data.put(p.getUniqueId(),new PlayerData(p));
    }
}
