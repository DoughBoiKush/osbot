package com.thatgamerblue.osbot.pkhelper.tickers.impl;

import com.thatgamerblue.osbot.framework.BaseScript;
import com.thatgamerblue.osbot.pkhelper.EventBus;
import com.thatgamerblue.osbot.pkhelper.events.EventType;
import com.thatgamerblue.osbot.pkhelper.events.SpotanimChangedEvent;
import com.thatgamerblue.osbot.pkhelper.tickers.Ticker;
import org.osbot.rs07.api.model.Player;

import java.util.HashMap;

public class SpotanimTicker extends Ticker {

    private HashMap<Player, Integer> oldAnims = new HashMap<>();
    boolean isFirstTick = true;

    @Override
    public boolean tick(BaseScript script) {
        if (isFirstTick) {
            isFirstTick = false;
            return true;
        }
        for (Player player : script.getPlayers().getAll()) {
            if (player == null) {
                continue;
            }
            int oldAnim = oldAnims.getOrDefault(player, -1);
            int curAnim = player.getAnimationSpot();
            if (curAnim != oldAnim) {
                oldAnims.put(player, curAnim);
                EventBus.postEvent(new SpotanimChangedEvent(player, oldAnim, curAnim), EventType.SPOTANIMCHANGED);
            }
        }
        return true;
    }

    public HashMap<Player, Integer> getAnimations() {
        return oldAnims;
    }
}
