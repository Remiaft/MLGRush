package cn.rmc.mlgrush.manager;

import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.data.Map;
import cn.rmc.mlgrush.util.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

public class MapManager {
    public ArrayList<Map> maps = new ArrayList<>();
    @Getter
    private Location lobby;
    public MapManager(){
        reloadmaps();
    }
    public void reloadmaps(){
        Config config = MLGRush.getMapdata();
        if (config.get("lobby") == null) {
            lobby = (Bukkit.getWorlds().get(0)).getSpawnLocation();
            System.out.println("Lobby didnt be set");
        } else {
            lobby = (Location)config.get("lobby");
            System.out.println("LobbySet");
        }
        if(config.getConfigurationSection("games") == null || config.getConfigurationSection("games").getKeys(false) == null){
            System.out.println("没有检测到地图");
            return;
        }
        int cnt = 0;
        for (String mapName : config.getConfigurationSection("games").getKeys(false)) {
            Map map = new Map();
            if (config.getBoolean("games." + mapName + ".a")) continue;
            map.name = mapName;
            map.pos1 = (Location)config.get("games." + mapName + ".pos1");
            map.pos2 = (Location)config.get("games." + mapName + ".pos2");
            map.spec = (Location)config.get("games." + mapName + ".spec");
            map.blueSpawn = (Location)config.get("games." + mapName + ".bluespawn");
            map.redSpawn = (Location)config.get("games." + mapName + ".redspawn");
            map.redBed = (Location)config.get("games." + mapName + ".redbed");
            map.blueBed = (Location)config.get("games." + mapName + ".bluebed");
            maps.add(map);
            cnt++;
        }
        System.out.println("加载了"+cnt+"张地图");

    }
    public Map getMap(){
        if(maps.size() == 0){
            return null;
        }
        int random = (int)(Math.random() * maps.size());
        Map result = maps.get(random);
        return result;
    }
    public void addMap(Map map){
        maps.add(map);
    }
}
