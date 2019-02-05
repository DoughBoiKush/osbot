package com.thatgamerblue.osbot.util.gui;

public class ComboItemGeneric {
	
	private String key;
	private Object value;
	
	public ComboItemGeneric(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return key;
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getValue() {
		return value;
	}
	
}
