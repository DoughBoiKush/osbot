package com.thatgamerblue.osbot.util.ge;

import org.osbot.rs07.api.model.GroundItem;
import java.util.Comparator;

public class PriceSorter implements Comparator<GroundItem> {
	
	@Override
	public int compare(GroundItem o1, GroundItem o2) {
		if(Prices.getItemPriceJagex(o1.getId(), null) > Prices.getItemPriceJagex(o2.getId(), null)) {
			return 1;
		}
		if(Prices.getItemPriceJagex(o1.getId(), null) == Prices.getItemPriceJagex(o2.getId(), null)) {
			return 0;
		}
		return -1;
	}
	
}
