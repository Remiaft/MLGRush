package cn.rmc.mlgrush.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BukkitReflection {
   private static final String CRAFT_BUKKIT_PACKAGE;
   private static final String NET_MINECRAFT_SERVER_PACKAGE;
   private static final Class CRAFT_SERVER_CLASS;
   private static final Method CRAFT_SERVER_GET_HANDLE_METHOD;
   private static final Class PLAYER_LIST_CLASS;
   private static final Field PLAYER_LIST_MAX_PLAYERS_FIELD;
   private static final Class CRAFT_PLAYER_CLASS;
   private static final Method CRAFT_PLAYER_GET_HANDLE_METHOD;
   private static final Class ENTITY_PLAYER_CLASS;
   private static final Field ENTITY_PLAYER_PING_FIELD;
   private static final Class CRAFT_ITEM_STACK_CLASS;
   private static final Method CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD;

   public static void sendLightning(Player p, Location l) {
      Class light = getNMSClass("EntityLightning");

      try {
         Constructor<?> constu = light.getConstructor(getNMSClass("World"), Double.TYPE, Double.TYPE, Double.TYPE, Boolean.TYPE, Boolean.TYPE);
         Object wh = p.getWorld().getClass().getMethod("getHandle").invoke(p.getWorld());
         Object lighobj = constu.newInstance(wh, l.getX(), l.getY(), l.getZ(), true, true);
         Object obj = getNMSClass("PacketPlayOutSpawnEntityWeather").getConstructor(getNMSClass("Entity")).newInstance(lighobj);
         sendPacket(p, obj);
         p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 100.0F, 1.0F);
      } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException var7) {
         var7.printStackTrace();
      }

   }

   public static Class<?> getNMSClass(String name) {
      String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

      try {
         return Class.forName("net.minecraft.server." + version + "." + name);
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static void sendPacket(Player player, Object packet) {
      try {
         Object handle = player.getClass().getMethod("getHandle").invoke(player);
         Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
         playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static int getPing(Player player) {
      try {
         int ping = ENTITY_PLAYER_PING_FIELD.getInt(CRAFT_PLAYER_GET_HANDLE_METHOD.invoke(player));
         return ping > 0 ? ping : 0;
      } catch (Exception var2) {
         return 1;
      }
   }

   public static void setMaxPlayers(Server server, int slots) {
      try {
         PLAYER_LIST_MAX_PLAYERS_FIELD.set(CRAFT_SERVER_GET_HANDLE_METHOD.invoke(server), slots);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static String getItemStackName(ItemStack itemStack) {
      try {
         return (String)CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD.invoke(itemStack, itemStack);
      } catch (Exception var2) {
         var2.printStackTrace();
         return "";
      }
   }

   static {
      try {
         String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
         CRAFT_BUKKIT_PACKAGE = "org.bukkit.craftbukkit." + version + ".";
         NET_MINECRAFT_SERVER_PACKAGE = "net.minecraft.server." + version + ".";
         CRAFT_SERVER_CLASS = Class.forName(CRAFT_BUKKIT_PACKAGE + "CraftServer");
         CRAFT_SERVER_GET_HANDLE_METHOD = CRAFT_SERVER_CLASS.getDeclaredMethod("getHandle");
         CRAFT_SERVER_GET_HANDLE_METHOD.setAccessible(true);
         PLAYER_LIST_CLASS = Class.forName(NET_MINECRAFT_SERVER_PACKAGE + "PlayerList");
         PLAYER_LIST_MAX_PLAYERS_FIELD = PLAYER_LIST_CLASS.getDeclaredField("maxPlayers");
         PLAYER_LIST_MAX_PLAYERS_FIELD.setAccessible(true);
         CRAFT_PLAYER_CLASS = Class.forName(CRAFT_BUKKIT_PACKAGE + "entity.CraftPlayer");
         CRAFT_PLAYER_GET_HANDLE_METHOD = CRAFT_PLAYER_CLASS.getDeclaredMethod("getHandle");
         CRAFT_PLAYER_GET_HANDLE_METHOD.setAccessible(true);
         ENTITY_PLAYER_CLASS = Class.forName(NET_MINECRAFT_SERVER_PACKAGE + "EntityPlayer");
         ENTITY_PLAYER_PING_FIELD = ENTITY_PLAYER_CLASS.getDeclaredField("ping");
         ENTITY_PLAYER_PING_FIELD.setAccessible(true);
         CRAFT_ITEM_STACK_CLASS = Class.forName(CRAFT_BUKKIT_PACKAGE + "inventory.CraftItemStack");
         CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD = CRAFT_ITEM_STACK_CLASS.getDeclaredMethod("asNMSCopy", ItemStack.class);
         CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD.setAccessible(true);
      } catch (Exception var1) {
         var1.printStackTrace();
         throw new RuntimeException("Failed to initialize Bukkit/NMS Reflection");
      }
   }
}
