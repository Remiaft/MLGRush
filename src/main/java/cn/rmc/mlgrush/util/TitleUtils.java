package cn.rmc.mlgrush.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;

public class TitleUtils {
    /**
     * 使用方法例示
     * TitleAPI.sendFullTitle(player,100,100,100,"显示在最上面的大Title","显示在大Title下面的小Title");
     **/
    public static void sendTitle(Player player, Integer fadeIn, Integer stay,
                                 Integer fadeOut, String message) {
        sendTitle(player, fadeIn, stay, fadeOut, message, null);
    }

    public static void sendSubtitle(Player player, Integer fadeIn, Integer stay,
                                    Integer fadeOut, String message) {
        sendTitle(player, fadeIn, stay, fadeOut, null, message);
    }

    public static void sendFullTitle(Player player, Integer fadeIn, Integer stay,
                                     Integer fadeOut, String title, String subtitle) {
        sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0])
                    .invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection")
                    .get(handle);
            playerConnection.getClass()
                    .getMethod("sendPacket", new Class[]{getNMSClass("Packet")})
                    .invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName()
                .split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        try {
            if (title != null) {
                title = ChatColor.translateAlternateColorCodes('&', title);
                title = title.replaceAll("%player%", player.getDisplayName());
                Object e = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0]
                        .getField("TIMES").get(null);
                Object chatTitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent"))
                        .getDeclaredClasses()[0]
                        .getMethod("a", new Class[]{String.class}).invoke(null,
                                "{\"text\":\"" + title + "\"}");
                Constructor subtitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle"))
                        .getConstructor(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0],
                                getNMSClass("IChatBaseComponent"), Integer.TYPE,
                                Integer.TYPE, Integer.TYPE);
                Object titlePacket = subtitleConstructor.newInstance(
                        e, chatTitle, fadeIn, stay, fadeOut);
                sendPacket(player, titlePacket);
                e = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0]
                        .getField("TITLE").get(null);
                chatTitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0]
                        .getMethod("a", new Class[]{String.class})
                        .invoke(null, "{\"text\":\"" + title + "\"}");
                subtitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle"))
                        .getConstructor(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0],
                                getNMSClass("IChatBaseComponent"));
                titlePacket = subtitleConstructor
                        .newInstance(e, chatTitle);
                sendPacket(player, titlePacket);
            }
            if (subtitle != null) {
                subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
                subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
                Object e = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0]
                        .getField("TIMES").get(null);
                Object chatSubtitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent"))
                        .getDeclaredClasses()[0]
                        .getMethod("a", new Class[]{String.class}).invoke(null,
                                "{\"text\":\"" + title + "\"}");
                Constructor subtitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle"))
                        .getConstructor(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0],
                                getNMSClass("IChatBaseComponent"), Integer.TYPE,
                                Integer.TYPE, Integer.TYPE);
                Object subtitlePacket = subtitleConstructor.newInstance(
                        e, chatSubtitle, fadeIn, stay, fadeOut);
                sendPacket(player, subtitlePacket);
                e = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0]
                        .getField("SUBTITLE").get(null);
                chatSubtitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0]
                        .getMethod("a", new Class[]{String.class})
                        .invoke(null, "{\"text\":\"" + subtitle + "\"}");
                subtitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle"))
                        .getConstructor(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0],
                                getNMSClass("IChatBaseComponent"), Integer.TYPE,
                                Integer.TYPE, Integer.TYPE);
                subtitlePacket = subtitleConstructor.newInstance(
                        e, chatSubtitle, fadeIn, stay, fadeOut);
                sendPacket(player, subtitlePacket);
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }
    }

    public static void clearTitle(Player player) {
        sendTitle(player, 0, 0, 0, "",
                "");
    }

    public static void sendTabTitle(Player player, String header, String footer) {
        if (header == null) {
            header = "";
        }
        header = ChatColor.translateAlternateColorCodes('&', header);
        if (footer == null) {
            footer = "";
        }
        footer = ChatColor.translateAlternateColorCodes('&', footer);
        header = header.replaceAll("%player%", player.getDisplayName());
        footer = footer.replaceAll("%player%", player.getDisplayName());
        try {
            Object tabHeader = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0]
                    .getMethod("a", new Class[]{String.class})
                    .invoke(null, "{\"text\":\"" + header + "\"}");
            Object tabFooter = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0]
                    .getMethod("a", new Class[]{String.class})
                    .invoke(null, "{\"text\":\"" + footer + "\"}");
            Constructor<?> titleConstructor = Objects.requireNonNull(getNMSClass(
                    "PacketPlayOutPlayerListHeaderFooter")).getConstructor();
            Object packet = titleConstructor.newInstance();
            Field aField = packet.getClass().getDeclaredField("a");
            aField.setAccessible(true);
            aField.set(packet, tabHeader);
            Field bField = packet.getClass().getDeclaredField("b");
            bField.setAccessible(true);
            bField.set(packet, tabFooter);
            sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
