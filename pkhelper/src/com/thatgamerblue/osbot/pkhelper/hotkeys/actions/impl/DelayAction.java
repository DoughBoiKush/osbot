package com.thatgamerblue.osbot.pkhelper.hotkeys.actions.impl;

import com.thatgamerblue.osbot.pkhelper.hotkeys.actions.HotkeyAction;

public class DelayAction extends HotkeyAction {

    long delayMs;

    public DelayAction(long millis) {
        delayMs = millis;
    }

    @Override
    public void execute() {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException ignored) {
        }
    }

}
