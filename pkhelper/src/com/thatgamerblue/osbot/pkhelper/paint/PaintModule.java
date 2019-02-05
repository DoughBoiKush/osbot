package com.thatgamerblue.osbot.pkhelper.paint;

import com.thatgamerblue.osbot.pkhelper.EventBus;
import com.thatgamerblue.osbot.pkhelper.events.IEventHandler;
import org.osbot.rs07.script.Script;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class PaintModule implements IEventHandler {

    public PaintModule() {
        EventBus.registerHandler(this);
    }

    public abstract void doPaint(Graphics2D g, Script script, long currentTime);

}
