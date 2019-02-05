package com.thatgamerblue.osbot.pkhelper.hotkeys.actions.impl;

import com.thatgamerblue.osbot.pkhelper.PKHelper;
import com.thatgamerblue.osbot.pkhelper.enums.TabStrings;
import com.thatgamerblue.osbot.pkhelper.hotkeys.actions.HotkeyAction;
import com.thatgamerblue.osbot.util.Sleep;
import com.thatgamerblue.osbot.util.TabUtils;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.MethodProvider;

import java.awt.*;
import java.awt.event.KeyEvent;

public class EquipItemAction extends HotkeyAction {

    private String itemName;

    public EquipItemAction(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public void execute() throws InterruptedException {
        PKHelper script = PKHelper.inst;
        if (!script.getTabs().isOpen(Tab.INVENTORY)) {
            int hotkey = script.tabUtil.getTabHotkey(Tab.INVENTORY);
            if (hotkey != -1) {
                if (hotkey == KeyEvent.VK_ESCAPE) {
                    script.keyboard.typeEscape();
                } else {
                    script.keyboard.typeFKey(hotkey);
                }
            } else {
                RS2Widget inventory = script.getWidgetWithAction(TabUtils.tabsWidgetRootId, TabStrings.INVENTORY);
                Rectangle targetRect = inventory.getBounds();
                int minX = (int) targetRect.getMinX(), maxX = (int) targetRect.getMaxX(), minY = (int) targetRect.getMinY(), maxY = (int) targetRect.getMaxY();
                int x = MethodProvider.random(minX, maxX);
                int y = MethodProvider.random(minY, maxY);
                setMousePosition(x, y);
                Thread.sleep(5);
                mouseClick(false);
            }
            if (!Sleep.sleepUntil(200, 4, () -> script.getTabs().isOpen(Tab.INVENTORY))) {
                script.error("Failed to open the inventory tab!");
                return;
            }
        }
        // inventory is open
        if (!script.inventory.contains(itemName)) {
            script.error("Inventory does not contain: " + itemName);
            return;
        }
        Item target = script.inventory.getItem(itemName);
        if (target == null) {
            script.error("Inventory does not contain: " + itemName);
            return;
        }
        int slot = script.inventory.getSlot(target);
        Rectangle clickRect = script.inventory.getSlotBoundingBox(slot);
        setMousePosition(clickRect);
        Thread.sleep(5);
        mouseClick(false);
    }
}
