package com.thatgamerblue.osbot.pkhelper.events;

import java.util.EventObject;

public interface IEventHandler {

    void handleEvent(EventObject event, EventType type);

}
