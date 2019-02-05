package com.thatgamerblue.osbot.pkhelper.hotkeys.actions;

import java.util.ArrayList;

public class HotkeyActionChain extends ArrayList<HotkeyAction> {

    public void executeAll() throws InterruptedException {
        for (HotkeyAction action : this) {
            action.execute();
        }
    }

}
