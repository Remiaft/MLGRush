package cn.rmc.mlgrush.command;

import cn.rmc.mlgrush.Language;
import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.data.PlayerData;
import cn.rmc.mlgrush.enums.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SpectateCommand extends Command {
    public SpectateCommand(){
        super("spectate");
        setAliases(Arrays.asList("spec"));
    }

    @Override
    public boolean execute(CommandSender Sender, String s, String[] args) {
        if(!(Sender instanceof Player)){
            Sender.sendMessage("你不是一名玩家!");
            return true;
        }
        Player p = (Player) Sender;
        Language lang = Language.get();
        String prefix = lang.translate("prefix");
        PlayerData pd = MLGRush.getInstance().getPlayerManager().get(p);
        if(args.length != 1) {
            Sender.sendMessage(ChatColor.RED+"Usage: /spectate <Player>");
            return true;
        }
        if(Bukkit.getPlayer(args[0]) == null){
            Sender.sendMessage(prefix+lang.translate("Message.spec.non"));
            return true;
        }
        if(Bukkit.getPlayer(args[0]) == p){
            Sender.sendMessage(prefix+lang.translate("Message.spec.self"));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        PlayerData targetd = MLGRush.getInstance().getPlayerManager().get(target);
        if(targetd.getTeam() == Team.NON){
            Sender.sendMessage(prefix+lang.translate("Message.spec.opponent"));
            return true;
        }
        targetd.getGame().addwatcher(pd);
        return false;
    }

}
