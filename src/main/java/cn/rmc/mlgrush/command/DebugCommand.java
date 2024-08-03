package cn.rmc.mlgrush.command;

import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.data.PlayerData;
import cn.rmc.mlgrush.inventory.KitEditorMenu;
import cn.rmc.mlgrush.inventory.SpecMenu;
import cn.rmc.mlgrush.util.game.LevelUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand extends Command {
    public DebugCommand(){
        super("debug");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if(!p.hasPermission("rushbed.admin")) return true;
        PlayerData pd = MLGRush.getInstance().getPlayerManager().get(p);
        if(args[0].equalsIgnoreCase("queue")){
            if(args[1].equalsIgnoreCase("add")){
                System.out.println("executed ! "+ p.getName());
                MLGRush.getInstance().getQueueManager().add(p);
            }
            if(args[1].equalsIgnoreCase("remove")){
                MLGRush.getInstance().getQueueManager().remove(p);
            }
        }
        if(args[0].equalsIgnoreCase("stats")){
            sender.sendMessage(new String[]{
                    pd.getState().toString(),
                    pd.getGame()+"",
                    pd.getOpponent()+"",
                    pd.getTeam().toString(),
                    LevelUtils.get(p).getKills()+"",
                    "maps:"+MLGRush.getInstance().getMapManager().maps.size(),
                    pd.getGame() == null ? "null":pd.getGame().getWatcher().size()+"",
                    pd.getSpecingGame() == null? "null":pd.getSpecingGame().getWatcher().size()+""
            });
        }
        if(args[0].equalsIgnoreCase("kills")){
            LevelUtils.get(p).addKill();
        }
        if(args[0].equalsIgnoreCase("spec")){
            SpecMenu.open(p);
        }
        if(args[0].equalsIgnoreCase("inv")){
            sender.sendMessage(MLGRush.getInstance().getSpecMenu().inventories.size()+"aaa");
        }
        if(args[0].equalsIgnoreCase("editor")){
            KitEditorMenu.open(p);
        }
        return false;
    }
}
