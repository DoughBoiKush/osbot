package com.thatgamerblue.osbot.pkhelper.hotkeys;

import com.thatgamerblue.osbot.pkhelper.EventBus;
import com.thatgamerblue.osbot.pkhelper.PKHelper;
import com.thatgamerblue.osbot.pkhelper.events.EventType;
import com.thatgamerblue.osbot.pkhelper.events.IEventHandler;
import com.thatgamerblue.osbot.pkhelper.hotkeys.actions.HotkeyActionChain;
import com.thatgamerblue.osbot.util.ArrayUtils;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HotkeySystem extends Thread implements IEventHandler {

    private boolean shouldExit = false;
    private boolean enabled = true;

    private ArrayList<HotkeyActionChain> allSwitches = new ArrayList<>();
    private Queue<HotkeyActionChain> queuedSwitches = new ConcurrentLinkedQueue<>();

    public HotkeySystem() {
        EventBus.registerHandler(this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean b) {
        enabled = b;
    }

    public void exit() {
        shouldExit = true;
    }

    public void run() {
        while (!shouldExit) {
            if (!enabled) {
                continue;
            }
            HotkeyActionChain actionChain;
            while ((actionChain = queuedSwitches.poll()) != null) {
                try {
                    actionChain.executeAll();
                    sleep(1);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    @Override
    public void handleEvent(EventObject event, EventType type) {
        switch (type) {
            case KEYDOWN:
                KeyEvent keyEvent = (KeyEvent) event;
                if (keyEvent.getKeyCode() < 0x30 || keyEvent.getKeyCode() > 0x39) {
                    break;
                }
                int switchIdx = keyEvent.getKeyCode() - 0x30;
                PKHelper.inst.debug("num press: " + switchIdx);
                if (!ArrayUtils.isValidIndex(allSwitches, switchIdx)) {
                    break;
                }
                HotkeyActionChain chain = allSwitches.get(switchIdx);
                if (chain == null) {
                    break;
                }
                queuedSwitches.offer(chain);
        }
    }
}