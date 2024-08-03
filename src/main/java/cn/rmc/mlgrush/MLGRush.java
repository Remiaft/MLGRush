package cn.rmc.mlgrush;

import cn.rmc.mlgrush.command.DebugCommand;
import cn.rmc.mlgrush.command.DuelCommand;
import cn.rmc.mlgrush.command.SetupCommand;
import cn.rmc.mlgrush.command.SpectateCommand;
import cn.rmc.mlgrush.data.Game;
import cn.rmc.mlgrush.enums.State;
import cn.rmc.mlgrush.inventory.KitEditorMenu;
import cn.rmc.mlgrush.inventory.SpecMenu;
import cn.rmc.mlgrush.listener.PlayerInteractListener;
import cn.rmc.mlgrush.listener.PlayerListener;
import cn.rmc.mlgrush.listener.PlayerMoveListener;
import cn.rmc.mlgrush.listener.WorldListener;
import cn.rmc.mlgrush.manager.*;
import cn.rmc.mlgrush.util.Config;
import cn.rmc.mlgrush.util.inventory.UIListener;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class MLGRush extends JavaPlugin {
    @Getter
    private static MLGRush instance;
    @Getter
    private static Config lang;
    @Getter
    private static Config mapdata;
    @Getter
    private static Config player;
    @Getter
    private MapManager mapManager;
    @Getter
    private PlayerManager playerManager;
    @Getter
    private GameManager gameManager;
    @Getter
    private QueueManager queueManager;
    @Getter
    private SpecMenu specMenu;
    public static HashMap<UUID,Long> Timer = new HashMap<>();
    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        loadconfig();
        registerclass();
        registerCommand();
        registerEvent();
        for(Player p : Bukkit.getOnlinePlayers()){
            getPlayerManager().Create(p);
           getPlayerManager().get(p).tpToLobby();
           getPlayerManager().get(p).setState(State.SPAWN);
           getPlayerManager().get(p).giveLobbyKit();
            p.setHealth(20);
            p.setFoodLevel(20);
        }
        Bukkit.getWorld("world").setTime(6000);
        Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle","false");
        Bukkit.getWorld("world").setGameRuleValue("doMobSpawning","false");

    }

    @Override
    public void onDisable() {
        saveconfig();
        for(Game g : gameManager.getData()){
            g.clearBlock();
        }
    }
    void loadconfig() throws Throwable{
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        File langs = new File(this.getDataFolder(),"lang.yml");
        File maps = new File(this.getDataFolder(),"data.yml");
        File players = new File(this.getDataFolder(),"players.yml");
        if(!langs.exists()){
            saveResource("lang.yml",true);
        }
        if(!maps.exists()){
            maps.createNewFile();
        }
        if(!players.exists()){
            players.createNewFile();
        }
        lang = new Config(langs);
        mapdata = new Config(maps);
        player = new Config(players);
    }

    void registerclass() {

        new PAPIHook().register();
        new ScoreBoardManager();
        mapManager = new MapManager();
        playerManager = new PlayerManager();
        gameManager = new GameManager();
        queueManager = new QueueManager();
        specMenu = new SpecMenu();

    }
    void registerEvent(){
        Arrays.asList(
                new PlayerListener(),
                new PlayerMoveListener(),
                new PlayerInteractListener(),
                new UIListener(),
                new WorldListener(),
                new KitEditorMenu())
                .forEach(listener -> Bukkit.getPluginManager().registerEvents(listener,this));
    }

    void registerCommand(){
        Arrays.asList(
                new DebugCommand(),
                new DuelCommand(),
                new SetupCommand(),
                new SpectateCommand()).forEach(this::registerCommand);
    }


    private void registerCommand(final Command cmd) {
        MinecraftServer.getServer().server.getCommandMap().register(cmd.getName(), this.getName(), cmd);
    }
    void saveconfig(){
        try {
            mapdata.save(new File(this.getDataFolder(), "data.yml"));
            player.save(new File(this.getDataFolder(),"players.yml"));
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

}
