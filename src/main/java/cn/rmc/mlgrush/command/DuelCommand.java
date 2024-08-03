package cn.rmc.mlgrush.command;

import cn.rmc.mlgrush.Language;
import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.enums.Team;
import cn.rmc.mlgrush.util.game.DuelUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand extends Command {

    public DuelCommand(){
        super("duel");
    }
    @Override
    public boolean execute(CommandSender Sender, String s, String[] args) {
        if (!(Sender instanceof Player)) {
            Sender.sendMessage("你不是一名玩家!");
            return true;
        }
        if (args.length == 1) {
            Player p = (Player) Sender;
            Language lang = Language.get();
            String prefix = lang.translate("prefix");
            if (Bukkit.getPlayer(args[0]) == null) {
                Sender.sendMessage(prefix+lang.translate("Message.duel.non"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == p) {
                Sender.sendMessage(prefix+lang.translate("Message.duel.self"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            DuelUtils.Request(p, target);
            return true;
        } else if (args.length == 2) {
            Player p = (Player) Sender;
            Player target = Bukkit.getPlayer(args[1]);
            if (args[0].equals("accept")) {
                DuelUtils.Accept(p, target);
                return true;
            } else {
                p.sendMessage(ChatColor.RED + "Usage: /duel accpet <Player>");
                return true;
            }
        }
        Sender.sendMessage(ChatColor.RED + "Usage: /duel <Player>");
        return true;
    }
}
