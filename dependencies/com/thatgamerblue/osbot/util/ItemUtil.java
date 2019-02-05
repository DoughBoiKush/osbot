package com.thatgamerblue.osbot.util;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.input.mouse.MouseDestination;
import org.osbot.rs07.script.API;

import java.awt.event.MouseEvent;
import java.util.HashMap;

public class ItemUtil extends API {
	
	@Override
	public void initializeModule() {
		
	}
	
	public MouseDestination getLastInInventory(String name, Inventory inv) {
		int slot = -1;
		int i = 0;
		for (Item item : inv.getItems()) {
			if (item != null && item.getName().equalsIgnoreCase(name)) {
				slot = i;
			}
			i++;
		}
		if (slot == -1) {
			return null;
		}
		return inv.getMouseDestination(slot);
	}
	
	public boolean inventoryMatchesPattern(Inventory inv, char[] pattern, HashMap<Character, String> mapping) {
		int k = 0;
		for (Item i : inv.getItems()) {
			//log(k);
			if (pattern.length - 1 < k) {
				//log("pattern small");
				return false;
			}
			if (pattern[k] == '*') {
				//log("********");
				k++;
				continue;
			}
			if (!mapping.containsKey(pattern[k])) {
				//log("mapping doesnt contain key");
				return false;
			}
			String check = mapping.get(pattern[k]);
			if (i == null || !check.equalsIgnoreCase(i.getName())) {
				//log("item null or name wrong");
				return false;
			}
			k++;
		}
		return true;
	}
	
	public void moveItemToSlot(Inventory inv, int slot, int destination) {
		getMouse().move(inv.getMouseDestination(slot));
		pressMouse();
		getMouse().move(inv.getMouseDestination(destination));
		releaseMouse();
	}
	
	
	private void pressMouse() {
		getBot().getMouseEventHandler().generateBotMouseEvent(MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, getMouse().getPosition().x, getMouse().getPosition().y, 1, false,
				MouseEvent.BUTTON1, true);
	}
	
	private void releaseMouse() {
		getBot().getMouseEventHandler().generateBotMouseEvent(MouseEvent.MOUSE_RELEASED,
				System.currentTimeMillis(), 0, getMouse().getPosition().x, getMouse().getPosition().y, 1, false,
				MouseEvent.BUTTON1, true);
	}
	
}
