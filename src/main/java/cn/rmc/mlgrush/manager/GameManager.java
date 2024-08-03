package cn.rmc.mlgrush.manager;

import cn.rmc.mlgrush.data.Game;
import lombok.Getter;

import java.util.ArrayList;

public class GameManager {
    @Getter
    private ArrayList<Game> data = new ArrayList<>();
    public void addGame(Game g){
        data.add(g);
    }
    public void removeGame(Game g){
        data.remove(g);
    }

}
