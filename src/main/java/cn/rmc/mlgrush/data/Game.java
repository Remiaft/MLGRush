package cn.rmc.mlgrush.data;

import cn.rmc.mlgrush.Language;
import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.enums.Mode;
import cn.rmc.mlgrush.enums.State;
import cn.rmc.mlgrush.enums.Team;
import cn.rmc.mlgrush.util.ActionBarUtils;
import cn.rmc.mlgrush.util.ItemBuilder;
import cn.rmc.mlgrush.util.TagUtils;
import cn.rmc.mlgrush.util.TitleUtils;
import cn.rmc.mlgrush.util.game.LevelUtils;
import cn.rmc.mlgrush.util.game.SlotUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    public static int lastid = 1;
    @Getter
    private int id;
    @Getter
    private PlayerData red;
    @Getter
    private int redscore = 0;
    @Getter
    private PlayerData blue;
    @Getter
    private int bluescore = 0;
    @Getter
    private List<PlayerData> fighter;
    @Getter
    private Mode mode;
    @Getter
    private Map map;
    @Getter
    private ArrayList<PlayerData> watcher = new ArrayList<>();
    @Getter
    private ArrayList<Block> block = new ArrayList<>();
    @Getter
    private BukkitTask waitTask;

    public Game(PlayerData p1, PlayerData p2, Map map, Mode mode){
        fighter = Arrays.asList(p1,p2);
        if(map == null){
            fighter.forEach(playerData -> playerData.getPlayer().sendMessage("§c没有可用地图, 请向管理员汇报"));
            return;
        }
        id = lastid;
        lastid++;
        Language lang = Language.get();
        String prefix = lang.translate("prefix");
        switch (p1.getState()){
            case SPECTATING:
                p1.getSpecingGame().removewatcher(p1);
                break;
            case FIGHTING:
                p2.getPlayer().sendMessage(prefix+lang.translate("Message.duel.acceptopponetfighting"));
                return;
            case WAITING:
                p2.getPlayer().sendMessage(prefix+lang.translate("Message.duel.acceptopponetfighting"));
                return;
        }
        switch (p2.getState()){
            case SPECTATING:
                p2.getSpecingGame().removewatcher(p2);
                break;
            case FIGHTING:
                p2.getPlayer().sendMessage(prefix+lang.translate("Message.duel.acceptfighting"));
                return;
            case WAITING:
                p2.getPlayer().sendMessage(prefix+lang.translate("Message.duel.acceptfighting"));
                return;
        }
        this.map = map;
        this.mode = mode;
        red = p1;
        blue = p2;
        red.setTeam(Team.RED);
        red.setOpponent(blue);
        blue.setTeam(Team.BLUE);
        blue.setOpponent(red);
        MLGRush.getInstance().getGameManager().addGame(this);
        MLGRush.getInstance().getMapManager().maps.remove(map);
        waittime();
    }

    public void waittime(){
        fighter.forEach(playerData -> {
            playerData.setState(State.WAITING);
            playerData.setGame(this);
            playerData.respawn();
            playerData.getPlayer().getInventory().clear();
        });

        waitTask = new BukkitRunnable(){
            int i = 5;
            @Override
            public void run() {
                if(i <= 5 && i > 0){
                    fighter.forEach(playerData -> {
                        playerData.getPlayer().sendMessage(String.valueOf(i));
                        playerData.getPlayer().playSound(playerData.getPlayer().getLocation(), Sound.NOTE_PLING,1,1);

                    });
                }
                if(i <= 0){
                    start();
                    cancel();
                }
                i--;
            }
        }.runTaskTimer(MLGRush.getInstance(),0L,20L);


    }
    public void start(){
        fighter.forEach(playerData -> {
            playerData.setGame(this);
            playerData.respawn();
            playerData.getPlayer().getInventory().clear();
            setTag(playerData);
            playerData.setState(State.FIGHTING);
            initplayer(playerData);
            TitleUtils.sendFullTitle(playerData.getPlayer(),0,20,10,Language.get().translate("Title.start"),"");
            playerData.getPlayer().playSound(playerData.getPlayer().getLocation(), Sound.LEVEL_UP,1,1);
        });
    }
    public void newRound(PlayerData winner){
        clearBlock();
        fighter.forEach(playerData -> {
            MLGRush.Timer.put(playerData.getPlayer().getUniqueId(),System.currentTimeMillis());
        });
        if(winner.getTeam() == Team.RED){
            redscore++;
            LevelUtils.get(red.getPlayer()).addBreak();
            LevelUtils.get(blue.getPlayer()).addBroken();
            fighter.forEach(playerData ->
                    ActionBarUtils.sendActionBar(playerData.getPlayer(),Language.get().translate("Actionbar.redwin")
                            .replace("%red%",String.valueOf(redscore)).replace("%blue%",String.valueOf(bluescore))));
        }
        if(winner.getTeam() == Team.BLUE){
            bluescore++;
            LevelUtils.get(blue.getPlayer()).addBreak();
            LevelUtils.get(red.getPlayer()).addBroken();
            fighter.forEach(playerData -> ActionBarUtils.sendActionBar(playerData.getPlayer(),Language.get().translate("Actionbar.bluewin")
                    .replace("%red%",String.valueOf(redscore)).replace("%blue%",String.valueOf(bluescore))));
        }
        if(bluescore == 10){
            end(blue);
            return;
        }
        if(redscore == 10){
            end(red);
            return;
        }
        fighter.forEach(this::initplayer);
    }
    public void initplayer(PlayerData playerData){
        Language lang = Language.get();
        Player p = playerData.getPlayer();
        playerData.respawn();
        ItemStack stick = new ItemBuilder(Material.STICK, 1).setName(lang.translate("Item.stick")).addEnchant(Enchantment.KNOCKBACK,1).toItemStack();
        ItemStack blocks = new ItemBuilder(Material.SANDSTONE, 64).toItemStack();
        ItemStack pixel = new ItemBuilder(Material.DIAMOND_PICKAXE, 1).toItemStack();
        p.getInventory().clear();
        int Stick_slot = SlotUtils.getSlots(p,"stick",0);
        int Block_slot = SlotUtils.getSlots(p,"block",1);
        int Pickaxe_slot = SlotUtils.getSlots(p,"pickaxe",2);
        p.getInventory().setItem(Stick_slot, stick);
        p.getInventory().setItem(Block_slot, blocks);
        p.getInventory().setItem(Pickaxe_slot, pixel);
    }
    public void end(PlayerData winner){
        Language win = Language.get();
        Language lose = Language.get();
        TitleUtils.sendFullTitle(winner.getPlayer(),10,20,10,win.translate("Title.won"),win.translate("Title.wonsub"));
        TitleUtils.sendFullTitle(winner.getOpponent().getPlayer(),10,20,10,lose.translate("Title.lost"),lose.translate("Title.lostsub"));
        LevelUtils.get(winner.getPlayer()).addWin();
        LevelUtils.get(winner.getOpponent().getPlayer()).addLose();
        LevelUtils.get(winner.getPlayer()).addPx(20);
        LevelUtils.get(winner.getOpponent().getPlayer()).addPx(10);
        close();
    }
    public void close(){
        MLGRush.getInstance().getMapManager().addMap(map);
        MLGRush.getInstance().getGameManager().removeGame(this);
        clearBlock();
        watcher.forEach(playerData -> {
            playerData.getPlayer().setAllowFlight(false);
            playerData.refresh();
            playerData.tpToLobby();
            playerData.giveLobbyKit();
            unsetTag(playerData);
        });
        fighter.forEach(playerData -> {
            playerData.getGame().watcher.forEach(playerData1 -> playerData.getPlayer().showPlayer(playerData1.getPlayer()));
            playerData.getPlayer().showPlayer(playerData.getOpponent().getPlayer());
            playerData.refresh();
            playerData.tpToLobby();
            playerData.giveLobbyKit();
            unsetTag(playerData);
        });
        watcher.clear();
    }

    public void setTag(PlayerData pd){
        Language lang = Language.get();
        TagUtils.setTag(blue.getPlayer(),pd.getPlayer(),lang.translate("tag.blue"),"");
        TagUtils.setTag(red.getPlayer(),pd.getPlayer(),lang.translate("tag.red"),"");
    }
    public void unsetTag(PlayerData pd){
        TagUtils.unregisterTag(pd.getPlayer(),blue.getPlayer());
        TagUtils.unregisterTag(pd.getPlayer(),red.getPlayer());
    }


    public void addBlock(Block block){
        this.block.add(block);
    }
    public void removeBlock(Block block){
        if(this.block.contains(block)){
            this.block.remove(block);
        }
    }
    public void clearBlock(){
        Bukkit.getScheduler().runTask(MLGRush.getInstance(), () -> {
            for(Block b:block){
                b.setType(Material.AIR);
            }
            block.clear();
        });
    }
    public void addwatcher(PlayerData pd){
        if(watcher.contains(pd)) return;
        Player p = pd.getPlayer();
        Language lang = Language.get();
        String prefix = lang.translate("prefix");
        switch (pd.getState()){
            case SPECTATING:
                pd.getSpecingGame().removewatcher(pd);
                break;
            case QUEUE:
                MLGRush.getInstance().getQueueManager().remove(pd.getPlayer());
                break;
            case FIGHTING:
                p.sendMessage(prefix+lang.translate("Message.spec.fighting"));
                return;
            case WAITING:
                p.sendMessage(prefix+lang.translate("Message.spec.fighting"));
                return;
        }
        getFighter().forEach(playerData -> {
            playerData.getPlayer().hidePlayer(pd.getPlayer());
        });
        pd.setState(State.SPECTATING);
        watcher.add(pd);
        pd.setSpecingGame(this);
        pd.getPlayer().teleport(getMap().spec.clone().add(0.0D, 5.0D, 0.0D));
        pd.getPlayer().setAllowFlight(true);
        setTag(pd);
        pd.getPlayer().sendMessage(prefix + lang.translate("Message.spec.add"));
        giveWatcherKit(pd);
        //设置飞行
        try {
            pd.getPlayer().getClass().getDeclaredMethod("getHandle").setAccessible(true);
            pd.getPlayer().getClass().getDeclaredMethod("getHandle").invoke(pd.getPlayer()).getClass().getSuperclass().getDeclaredField("abilities").setAccessible(true);
            pd.getPlayer().getClass().getDeclaredMethod("getHandle").invoke(pd.getPlayer()).getClass().getSuperclass().getDeclaredField("abilities").get(pd.getPlayer().getClass().getDeclaredMethod("getHandle").invoke(pd.getPlayer())).getClass().getDeclaredField("isFlying").setAccessible(true);
            pd.getPlayer().getClass().getDeclaredMethod("getHandle").invoke(pd.getPlayer()).getClass().getSuperclass().getDeclaredField("abilities").get(pd.getPlayer().getClass().getDeclaredMethod("getHandle").invoke(pd.getPlayer())).getClass().getDeclaredField("isFlying").set(pd.getPlayer().getClass().getDeclaredMethod("getHandle").invoke(pd.getPlayer()).getClass().getSuperclass().getDeclaredField("abilities").get(pd.getPlayer().getClass().getDeclaredMethod("getHandle").invoke(pd.getPlayer())),true);
            pd.getPlayer().getClass().getDeclaredMethod("getHandle").invoke(pd.getPlayer()).getClass().getSuperclass().getDeclaredMethod("updateAbilities").setAccessible(true);
            pd.getPlayer().getClass().getDeclaredMethod("getHandle").invoke(pd.getPlayer()).getClass().getSuperclass().getDeclaredMethod("updateAbilities").invoke(pd.getPlayer().getClass().getDeclaredMethod("getHandle").invoke(pd.getPlayer()));
        }catch (Exception exception){}

    }
    public void removewatcher(PlayerData pd){
        if(!watcher.contains(pd)) return;
        watcher.remove(pd);
        getFighter().forEach(playerData -> playerData.getPlayer().showPlayer(pd.getPlayer()));
        pd.getPlayer().setAllowFlight(false);
        pd.setState(State.SPAWN);
        Language lang = Language.get();
        String prefix = Language.get().translate("prefix");
        pd.getPlayer().sendMessage(prefix + lang.translate("Message.spec.remove"));
        pd.refresh();
        pd.tpToLobby();
        pd.giveLobbyKit();
        unsetTag(pd);
    }
    public void giveWatcherKit(PlayerData pd){
        Language lang = Language.get();
        pd.getPlayer().getInventory().clear();
        pd.getPlayer().getInventory().setItem(8,new ItemBuilder(Material.NETHER_STAR).setName(lang.translate("Items.specleave")).toItemStack());
    }





}
