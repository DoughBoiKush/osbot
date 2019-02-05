package com.thatgamerblue.osbot.util.ge;

public class CachedItem {
	
	String name;
	int price;
	long time;
	
	public CachedItem(String name, int price, long time) {
		this.name = name;
		this.price = price;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
}
