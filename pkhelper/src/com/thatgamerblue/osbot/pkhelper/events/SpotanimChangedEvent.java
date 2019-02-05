package com.thatgamerblue.osbot.pkhelper.events;

import org.osbot.rs07.api.model.Player;

import java.util.EventObject;

public class SpotanimChangedEvent extends EventObject {

    private final int oldAnim, newAnim;
    private final Player player;

    public SpotanimChangedEvent(Player player, int oldAnim, int newAnim) {
        super(player);
        this.player = player;
        this.oldAnim = oldAnim;
        this.newAnim = newAnim;
    }

    public int getOldAnim() {
        return oldAnim;
    }

    public int getNewAnim() {
        return newAnim;
    }

    public Player getPlayer() {
        return player;
    }

    public String toString() {
        return "SpotanimChangedEvent[oldAnim=" + oldAnim + ",newAnim=" + newAnim + ",player=" + player + "]";
    }
}
