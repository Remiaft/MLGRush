package cn.rmc.mlgrush.util;

import org.bukkit.Location;

public class MathUtils {
    public static double getDistance(Location x, Location y) {
        return (Math.abs(x.getBlockX() - y.getBlockX()) + Math.abs(x.getBlockZ() - y.getBlockZ()));
    }
}
