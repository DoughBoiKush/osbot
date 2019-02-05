package com.thatgamerblue.osbot.pkhelper.listeners;

import com.thatgamerblue.osbot.pkhelper.PKHelper;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.listener.GameTickListener;
import org.osbot.rs07.script.MethodProvider;

import java.util.HashMap;

public class TickListener implements GameTickListener {

    private static HashMap<Player, HashMap<String, Integer>> lastTick = new HashMap<>();
    private static HashMap<Player, HashMap<String, Integer>> newTick = new HashMap<>();
    private MethodProvider main;

    public TickListener(MethodProvider main) {
        this.main = main;
        PKHelper.inst.debug("constructed gameticklistener");
    }

    @Override
    public void onGameTick() {
        PKHelper.inst.debug("game tick");
        lastTick.clear();
        lastTick.putAll(newTick);
        newTick.clear();
        for (Player p : main.getPlayers().getAll()) {
            newTick.get(p).put("PrayerIcon", p.getPrayerIcon());
            newTick.get(p).put("SpotAnim", p.getAnimationSpot());
        }
        //PKHelper.inst.tickTickers();
    }

    public static int getPrayerIconLastTick(Player p) {
        return lastTick.getOrDefault(p, new HashMap<>()).getOrDefault("PrayerIcon", -1337);
    }

}
