package com.thatgamerblue.osbot.pkhelper.listeners;

import com.thatgamerblue.osbot.pkhelper.EventBus;
import com.thatgamerblue.osbot.pkhelper.events.EventType;
import org.osbot.rs07.input.mouse.BotMouseListener;
import org.osbot.rs07.script.Script;

import java.awt.event.MouseEvent;

public class MouseListener extends BotMouseListener {

    public MouseListener() {
        super();
    }

    @Override
    public void checkMouseEvent(MouseEvent mouseEvent) {
        switch (mouseEvent.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                EventBus.postEvent(mouseEvent, EventType.MOUSEDOWN);
                break;
            case MouseEvent.MOUSE_RELEASED:
                EventBus.postEvent(mouseEvent, EventType.MOUSEUP);
                break;
            default:
                break;
        }
    }

}
