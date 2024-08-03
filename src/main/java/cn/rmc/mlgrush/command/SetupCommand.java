package cn.rmc.mlgrush.command;

import cn.rmc.mlgrush.MLGRush;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SetupCommand extends Command {
    public SetupCommand(){
        super("setup");
    }
    @SneakyThrows
    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.hasPermission("rush.setup")) {
            sender.sendMessage(ChatColor.RED + "你没有权限!");
            return false;
        }
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if(args[0].equalsIgnoreCase("save")){
                MLGRush.getMapdata().save(new File(MLGRush.getInstance().getDataFolder(), "data.yml"));
            }
            if (args.length == 1 && args[0].equals("setlobby")) {
                MLGRush.getMapdata().set("lobby", p.getLocation());
                sendMessege("设置大厅成功",sender);
                save();
            } else if (args.length == 2 && args[0].equals("create")) {
                MLGRush.getMapdata().createSection("games." + args[1]);
                sendMessege("创建成功",sender);
                save();
            } else if (args.length == 2 && args[0].equals("setspec")) {
                ConfigurationSection section = MLGRush.getMapdata().getConfigurationSection("games." + args[1]);
                if (section == null) {
                    sendMessege("请先创建游戏!", sender);
                    return true;
                }
                section.set("spec", p.getLocation());
                sendMessege("设置成功!", sender);
                save();
            } else if (args.length == 2 && args[0].equals("setpos1")) {
                ConfigurationSection section = MLGRush.getMapdata().getConfigurationSection("games." + args[1]);
                if (section == null) {
                    sendMessege("请先创建游戏!", sender);
                    return true;
                }
                section.set("pos1", p.getLocation());
                sendMessege("设置成功!", sender);
                save();
            } else if (args.length == 2 && args[0].equals("setpos2")) {
                ConfigurationSection section = MLGRush.getMapdata().getConfigurationSection("games." + args[1]);
                if (section == null) {
                    sendMessege("请先创建游戏!", sender);
                    return true;
                }
                section.set("pos2", p.getLocation());
                sendMessege("设置成功!", sender);
                save();
            } else if (args.length == 2 && args[0].equals("redspawn")) {
                ConfigurationSection section = MLGRush.getMapdata().getConfigurationSection("games." + args[1]);
                if (section == null) {
                    sendMessege("请先创建游戏!", sender);
                    return true;
                }
                section.set("redspawn", p.getLocation());
                sendMessege("设置成功!", sender);
                save();
            } else if (args.length == 2 && args[0].equals("bluespawn")) {
                ConfigurationSection section = MLGRush.getMapdata().getConfigurationSection("games." + args[1]);
                if (section == null) {
                    sendMessege("请先创建游戏!", sender);
                    return true;
                }
                section.set("bluespawn", p.getLocation());
                sendMessege("设置成功!", sender);
                save();
            } else if (args.length == 2 && args[0].equals("bluebed")) {
                ConfigurationSection section = MLGRush.getMapdata().getConfigurationSection("games." + args[1]);
                if (section == null) {
                    sendMessege("请先创建游戏!", sender);
                    return true;
                }
                section.set("bluebed", p.getLocation());
                sendMessege("设置成功!", sender);
                save();
            } else if (args.length == 2 && args[0].equals("redbed")) {
                ConfigurationSection section = MLGRush.getMapdata().getConfigurationSection("games." + args[1]);
                if (section == null) {
                    sendMessege("请先创建游戏!", sender);
                    return true;
                }
                section.set("redbed", p.getLocation());
                sendMessege("设置成功!", sender);
                save();
            } else if (args.length == 1 && args[0].equals("list")) {
                sendMessege("地图列表", sender);
                for (String name : MLGRush.getMapdata().getConfigurationSection("games").getKeys(false)) {
                    sendMessege(" =>  " + name, sender);
                }
            } else if (args.length == 2 && args[0].equals("tp")) {
                for (String name : MLGRush.getMapdata().getConfigurationSection("games").getKeys(false)) {
                    if (name.equals(args[1])) {
                        p.teleport((Location)MLGRush.getMapdata().get("games." + name + ".spec"));
                        return true;
                    }
                }
                p.sendMessage(ChatColor.RED + "没有找到此地图!");
            } else if (args.length == 2 && args[0].equals("remove")) {
                MLGRush.getMapdata().set("games." + args[0] + ".a", Boolean.valueOf(true));
                sender.sendMessage(ChatColor.GOLD + "删除成功!");
                save();
            } else {
                sendMessege("RushBed Setup使用帮助", sender);
                sendMessege("1、/setup setlobby 设置大厅", sender);
                sendMessege("3、/setup create 游戏名 创建游戏", sender);
                sendMessege("4、/setup list 地图列表", sender);
                sendMessege("5、/setup setspec 游戏名 设置游戏等待点", sender);
                sendMessege("6、/setup setpos1 游戏名 设置游戏最低点（玩家如果低于此点将被判定为死亡）", sender);
                sendMessege("7、/setup setpos2 游戏名 设置游戏的顶点", sender);
                sendMessege("8、/setup redspawn 游戏名 设置红队出生点", sender);
                sendMessege("9、/setup bluespawn 游戏名 设置蓝队出生点", sender);
                sendMessege("10、/setup redbed 游戏名 设置红队床（床的尾部的方块位置，一定要按照位置来）", sender);
                sendMessege("11、/setup bluebed 游戏名 设置蓝队床（床的尾部的方块位置，一定要按照位置来）", sender);
                sendMessege("12、/setup tp 游戏名 传送到游戏位置", sender);
            }
        } else {
            sendMessege("此指令只能由玩家调用!", sender);
        }
        return true;
    }

    private void sendMessege(String s, CommandSender sender) {
        sender.sendMessage(s);
    }
    private void save() throws IOException {
        //MLGRush.getMapdata().save(new File(MLGRush.getInstance().getDataFolder(),"data.yml"));
    }
}
