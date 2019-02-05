package com.thatgamerblue.osbot.pkhelper.enums;

import com.thatgamerblue.osbot.pkhelper.timers.TimerType;

public enum Spotanim {

    ICE_BARRAGE(369, TimerType.FREEZE, 20, "ice_barrage", false),
    ICE_BLITZ(367, TimerType.FREEZE, 15, "ice_blitz", false),
    ICE_BURST(363, TimerType.FREEZE, 10, "ice_burst", false),
    ICE_RUSH(361, TimerType.FREEZE, 5, "ice_rush", false),
    ENTANGLE(179, TimerType.FREEZE, 15, "entangle", true),
    SNARE(180, TimerType.FREEZE, 10, "snare", true),
    BIND(181, TimerType.FREEZE, 5, "bind", true),
    TELEBLOCK(345, TimerType.TELEBLOCK, 300, "teleblock", true),
    VENGEANCE(726, TimerType.VENGEANCE, 30, "veng", false),
    VENGEANCE_OTHER(725, TimerType.VENGEANCE, 30, "veng", false);

    int id;
    int type;
    int seconds;
    boolean halfOnPrayMage;
    String imageName;

    Spotanim(int id, int type, int seconds, String imageName, boolean halfOnPrayMage) {
        this.id = id;
        this.type = type;
        this.seconds = seconds;
        this.halfOnPrayMage = halfOnPrayMage;
        this.imageName = imageName;
    }

    public static Spotanim getById(int id) {
        for (Spotanim anim : values()) {
            if (anim.id == id) {
                return anim;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public int getTime() {
        return seconds;
    }

    public boolean halfOnPrayMage() {
        return halfOnPrayMage;
    }

    public String getImageName() {
        return imageName;
    }
}
