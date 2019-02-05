package com.thatgamerblue.osbot.pkhelper.listeners;

import com.thatgamerblue.osbot.pkhelper.EventBus;
import com.thatgamerblue.osbot.pkhelper.events.EventType;
import com.thatgamerblue.osbot.pkhelper.paint.PaintModule;
import org.osbot.rs07.input.keyboard.BotKeyListener;
import org.osbot.rs07.script.Script;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class KeyboardListener extends BotKeyListener {

    private HashMap<Integer, Boolean> keysDown = new HashMap<>();

    public KeyboardListener() {
        super();
    }

    @Override
    public void checkKeyEvent(KeyEvent e) {
        switch (e.getID()) {
            case KeyEvent.KEY_PRESSED:
                //script.log("Pressed");
                keysDown.put(e.getKeyCode(), true);
                EventBus.postEvent(e, EventType.KEYDOWN);
                break;
            case KeyEvent.KEY_RELEASED:
                //script.log("Released");
                keysDown.put(e.getKeyCode(), false);
                EventBus.postEvent(e, EventType.KEYUP);
                break;
            case KeyEvent.KEY_TYPED:
                //script.log("Typed");
                keysDown.put(e.getKeyCode(), true);
                break;
            default:
                break;
        }

    }

    public boolean isKeyDown(int keyCode) {
        return keysDown.getOrDefault(keyCode, false);
    }

}
