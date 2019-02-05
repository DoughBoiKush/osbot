package com.thatgamerblue.osbot.pkhelper;

import com.thatgamerblue.osbot.pkhelper.events.EventType;
import com.thatgamerblue.osbot.pkhelper.events.IEventHandler;

import java.util.ArrayList;
import java.util.EventObject;

public class EventBus {

    private static ArrayList<IEventHandler> handlers = new ArrayList<>();

    public static void postEvent(EventObject event, EventType type) {
        PKHelper.inst.debug("Received event: " + event + " of type " + type);
        for (IEventHandler handler : handlers) {
            handler.handleEvent(event, type);
        }
    }

    public static void registerHandler(IEventHandler handler) {
        handlers.add(handler);
    }

}
