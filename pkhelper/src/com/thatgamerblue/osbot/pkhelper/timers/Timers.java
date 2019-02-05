package com.thatgamerblue.osbot.pkhelper.timers;

import org.osbot.rs07.api.model.Player;

import java.util.HashMap;

public class Timers {

    private static HashMap<Player, HashMap<String, Object>> timers = new HashMap<>();

    public static boolean isFrozen(Player player, long currentTime) {
        if (!timers.containsKey(player)) {
            timers.put(player, new HashMap<>());
        }
        long i = (long) timers.get(player).getOrDefault("freeze", (long) 0);
        long j = (long) timers.get(player).getOrDefault("frozenAt", (long) 0);
        return j + i >= currentTime;
    }

    public static boolean isTeleblocked(Player player, long currentTime) {
        if (!timers.containsKey(player)) {
            timers.put(player, new HashMap<>());
        }
        long i = (long) timers.get(player).getOrDefault("teleblock", (long) 0);
        long j = (long) timers.get(player).getOrDefault("teleblockedAt", (long) 0);
        return j + i >= currentTime;
    }

    public static boolean isVenged(Player player, long currentTime) {
        if (!timers.containsKey(player)) {
            timers.put(player, new HashMap<>());
        }
        long i = (long) timers.get(player).getOrDefault("venged", (long) 0);
        long j = (long) timers.get(player).getOrDefault("vengedAt", (long) 0);
        return j + i >= currentTime;
    }

    public static void setFrozen(Player player, int seconds, long currentTime, String freezeImage) {
        if (!timers.containsKey(player)) {
            timers.put(player, new HashMap<>());
        }
        long millis = seconds * 1000;
        timers.get(player).put("freeze", millis);
        timers.get(player).put("frozenAt", currentTime);
        timers.get(player).put("freezeImage", freezeImage);
    }

    public static void setTeleblocked(Player player, int seconds, long currentTime) {
        if (!timers.containsKey(player)) {
            timers.put(player, new HashMap<>());
        }
        long millis = seconds * 1000;
        timers.get(player).put("teleblock", millis);
        timers.get(player).put("teleblockedAt", currentTime);
    }

    public static void setVenged(Player player, int seconds, long currentTime) {
        if (!timers.containsKey(player)) {
            timers.put(player, new HashMap<>());
        }
        long millis = seconds * 1000;
        timers.get(player).put("venged", millis);
        timers.get(player).put("vengedAt", currentTime);
    }

    public static long getUnfreezeTime(Player player) {
        if (!timers.containsKey(player)) {
            timers.put(player, new HashMap<>());
        }
        return (long) timers.get(player).getOrDefault("freeze", (long) 0) +
                (long) timers.get(player).getOrDefault("frozenAt", (long) 0);
    }

    public static long getUntbTime(Player player) {
        if (!timers.containsKey(player)) {
            timers.put(player, new HashMap<>());
        }
        return (long) timers.get(player).getOrDefault("teleblock", (long) 0) +
                (long) timers.get(player).getOrDefault("teleblockedAt", (long) 0);
    }

    public static long getVengTime(Player player) {
        if (!timers.containsKey(player)) {
            timers.put(player, new HashMap<>());
        }
        return (long) timers.get(player).getOrDefault("venged", (long) 0) +
                (long) timers.get(player).getOrDefault("vengedAt", (long) 0);
    }

    public static String getFreezeImage(Player player) {
        if (!timers.containsKey(player)) {
            timers.put(player, new HashMap<>());
        }
        return (String) timers.get(player).getOrDefault("freezeImage", "");
    }

}
