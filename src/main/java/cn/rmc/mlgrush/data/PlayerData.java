package cn.rmc.mlgrush.data;

import cn.rmc.mlgrush.Language;
import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.enums.State;
import cn.rmc.mlgrush.enums.Team;
import cn.rmc.mlgrush.util.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerData {
    @Getter
    private Player player;
    @Getter
    @Setter
    private State state;
    @Getter
    @Setter
    private Game game;
    @Getter
    @Setter
    private Team team;
    @Getter
    @Setter
    private PlayerData opponent;
    @Getter
    @Setter
    private Game SpecingGame;

    public PlayerData(Player p){
        this.player = p;
        setTeam(Team.NON);
    }
    public void refresh(){
        setTeam(Team.NON);
        setGame(null);
        setState(State.SPAWN);
        setOpponent(null);
        setSpecingGame(null);
    }

    public void respawn(){
        if(game == null || team == Team.NON) return;
        if(team == Team.RED){
            player.teleport(game.getMap().redSpawn);
        }
        else if(team == Team.BLUE){
            player.teleport(game.getMap().blueSpawn);
        }
    }
    public void tpToLobby(){
        player.teleport(MLGRush.getInstance().getMapManager().getLobby());
    }
    public void giveLobbyKit(){
        Language lang = Language.get();
        player.getInventory().clear();
        player.getInventory().setItem(0,new ItemBuilder(Material.GOLD_SWORD).setName(lang.get().translate("Items.queue")).toItemStack());
        player.getInventory().setItem(4,new ItemBuilder(Material.BOOK).setName(lang.translate("Items.editor")).toItemStack());
        player.getInventory().setItem(8,new ItemBuilder(Material.ENDER_PEARL).setName(lang.translate("Items.spec")).toItemStack());
    }
}
