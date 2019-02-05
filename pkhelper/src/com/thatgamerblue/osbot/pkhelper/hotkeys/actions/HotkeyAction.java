package com.thatgamerblue.osbot.pkhelper.hotkeys.actions;

import com.thatgamerblue.osbot.pkhelper.PKHelper;
import org.osbot.rs07.script.MethodProvider;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class HotkeyAction {

    public HotkeyAction() {

    }

    public abstract void execute() throws InterruptedException;

    protected void setMousePosition(int x, int y) {
        PKHelper.inst.getBot().getMouseEventHandler().generateBotMouseEvent(MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false, MouseEvent.NOBUTTON, true);
    }

    protected void setMousePosition(Rectangle rect) {
        int minX = (int) rect.getMinX(), maxX = (int) rect.getMaxX(), minY = (int) rect.getMinY(), maxY = (int) rect.getMaxY();
        int x = MethodProvider.random(minX, maxX);
        int y = MethodProvider.random(minY, maxY);
        setMousePosition(x, y);
    }

    protected void mouseClick(boolean right) throws InterruptedException {
        PKHelper.inst.getMouse().click(right);
    }

    protected void keyDown(int keyCode) {
        PKHelper.inst.getKeyboard().pressKey(keyCode);
    }

    protected void keyUp(int keyCode) {
        PKHelper.inst.getKeyboard().releaseKey(keyCode);
    }

    protected void keyPress(int keycode) throws InterruptedException {
        keyDown(keycode);
        Thread.sleep(1);
        keyUp(keycode);
    }

}
