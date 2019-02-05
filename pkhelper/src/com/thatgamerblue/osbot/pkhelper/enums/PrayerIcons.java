package com.thatgamerblue.osbot.pkhelper.enums;

public enum PrayerIcons {

    PROTECT_FROM_MAGE(2),
    PROTECT_FROM_MELEE(0),
    PROTECT_FROM_RANGED(1),
    SMITE(4),
    REDEMPTION(5),
    RETRIBUTION(3);

    int id;

    PrayerIcons(int id) {
        this.id = id;
    }

    public static PrayerIcons getById(int id) {
        for (PrayerIcons icon : values()) {
            if (icon.id == id) {
                return icon;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

}
