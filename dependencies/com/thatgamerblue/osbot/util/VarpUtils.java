package com.thatgamerblue.osbot.util;

import org.osbot.rs07.script.API;

public class VarpUtils extends API {
	
	@Override
	public void initializeModule() {
	}
	
	public static boolean isFlagSet(long flags, long flag) {
		return (flags & flag) == flag;
	}
	
	public boolean isBitSet(int config, int bit) {
		return (getBit(configs.get(config), bit) & 0b1) == 1;
	}
	
	private int getBit(int n, int k) {
		return (n >> k) & 1;
	}
	
}
