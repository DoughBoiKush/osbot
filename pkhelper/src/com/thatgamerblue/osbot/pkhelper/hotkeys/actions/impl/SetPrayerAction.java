package com.thatgamerblue.osbot.pkhelper.hotkeys.actions.impl;

import com.thatgamerblue.osbot.pkhelper.PKHelper;
import com.thatgamerblue.osbot.pkhelper.enums.TabStrings;
import com.thatgamerblue.osbot.pkhelper.hotkeys.actions.HotkeyAction;
import com.thatgamerblue.osbot.util.Sleep;
import com.thatgamerblue.osbot.util.TabUtils;
import org.osbot.rs07.api.ui.PrayerButton;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.MethodProvider;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SetPrayerAction extends HotkeyAction {

    PrayerButton target;
    private boolean toggle;

    public SetPrayerAction(PrayerButton targ, boolean toggle) {
        target = targ;
        this.toggle = toggle;
    }

    @Override
    public void execute() throws InterruptedException {
        PKHelper script = PKHelper.inst;
        if (!script.getTabs().isOpen(Tab.PRAYER)) {
            int hotkey = script.tabUtil.getTabHotkey(Tab.PRAYER);
            if (hotkey != -1) {
                if (hotkey == KeyEvent.VK_ESCAPE) {
                    script.keyboard.typeEscape();
                } else {
                    script.keyboard.typeFKey(hotkey);
                }
            } else {
                RS2Widget prayerWidget = script.getWidgetWithAction(TabUtils.tabsWidgetRootId, TabStrings.PRAYER);
                Rectangle targetRect = prayerWidget.getBounds();
                setMousePosition(targetRect);
                Thread.sleep(5);
                mouseClick(false);
            }
            if (!Sleep.sleepUntil(200, 4, () -> script.getTabs().isOpen(Tab.PRAYER))) {
                script.error("Failed to open the prayers tab!");
                return;
            }
        }
        // prayer tab is open
        if (script.prayer.isActivated(target) && !toggle) {
            script.info(target + " is already enabled, skipping.");
            return;
        }
        RS2Widget[] prayers = script.getWidgets().getWidgets(541);
        RS2Widget validPrayer = null;
        for (RS2Widget prayer : prayers) {
            if (prayer.isSecondLevel()) {
                RS2Widget thirdLevel = prayer.getChildWidget(1);
                if (thirdLevel == null) {
                    continue;
                }
                if (thirdLevel.getSpriteIndex1() == target.getSpriteId()) {
                    validPrayer = thirdLevel;
                    break;
                }
            }
        }
        if (validPrayer == null) {
            script.error("Failed to find prayer: " + target);
            return;
        }
        Rectangle prayerRect = validPrayer.getBounds();
        setMousePosition(prayerRect);
        Thread.sleep(5);
        mouseClick(false);
    }
}
