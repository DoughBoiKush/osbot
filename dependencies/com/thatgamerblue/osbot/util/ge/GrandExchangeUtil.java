package com.thatgamerblue.osbot.util.ge;

import com.thatgamerblue.osbot.framework.BaseScript;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;

public class GrandExchangeUtil extends org.osbot.rs07.api.GrandExchange {
	
	private static final int baseWidget = 465;
	private BaseScript s;
	
	public GrandExchangeUtil(BaseScript script) {
		super();
		s = script;
	}
	
	public boolean add5Percent() {
		RS2Widget w = s.getWidgetWithAction(baseWidget, "+5%");
		if (w == null || !w.isVisible()) {
			return false;
		}
		return w.interact("+5%");
	}
	
	public boolean remove5Percent() {
		RS2Widget w = s.getWidgetWithAction(baseWidget, "-5%");
		if (w == null || !w.isVisible()) {
			return false;
		}
		return w.interact("-5%");
	}
	
	public boolean add1() {
		/// base 24 3
		RS2Widget w = s.getWidgets().getWidgetContainingText(baseWidget, "+1");
		if (w == null || !w.isVisible()) {
			return false;
		}
		return w.interact();
	}
	
	public boolean add10() {
		/// base 24 4
		RS2Widget w = s.getWidgets().getWidgetContainingText(baseWidget, "+10");
		if (w == null || !w.isVisible()) {
			return false;
		}
		return w.interact();
	}
	
	public boolean add100() {
		/// base 24 5
		RS2Widget w = s.getWidgets().getWidgetContainingText(baseWidget, "+100");
		if (w == null || !w.isVisible()) {
			return false;
		}
		return w.interact();
	}
	
	public boolean add1000() {
		/// base 24 6
		RS2Widget w = s.getWidgets().getWidgetContainingText(baseWidget, "+1K");
		if (w == null || !w.isVisible()) {
			return false;
		}
		return w.interact();
	}
	
	public boolean resetPrice() {
		/// base 24 11
		RS2Widget w = s.getWidgetWithAction(baseWidget, "Guide price");
		if (w == null || !w.isVisible()) {
			return false;
		}
		return w.interact();
	}
	
	public boolean open() {
		RS2Object geBooth = getObjects().closest(new Filter<RS2Object>() {
			@Override
			public boolean match(RS2Object arg0) {
				return arg0.getName().contains("Grand Exchange booth") && arg0.hasAction("Exchange");
			}
		});
		if (geBooth == null) {
			return false;
		}
		return geBooth.interact("Exchange");
	}
	
}
