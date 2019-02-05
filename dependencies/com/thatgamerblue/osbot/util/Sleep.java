package com.thatgamerblue.osbot.util;

import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;
import org.osbot.rs07.utility.ConditionalSleep2;

import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;

public class Sleep extends ConditionalSleep {
	
	private final BooleanSupplier condition;
	
	public Sleep(final BooleanSupplier condition, final int timeout) {
		super(timeout);
		this.condition = condition;
	}
	
	@Override
	public final boolean condition() throws InterruptedException {
		return condition.getAsBoolean();
	}
	
	public static boolean sleepUntil(final BooleanSupplier condition, final int timeout) {
		return new Sleep(condition, timeout).sleep();
	}

	public static final boolean sleepUntil(int timeout, int delay, Callable<Boolean> condition) {
		try {
			long var3 = System.currentTimeMillis();

			while(!Thread.interrupted()) {
				if (condition.call()) {
					return true;
				}

				Thread.sleep(delay == -1 ? (long) MethodProvider.random(25, 60) : (long)delay);
				if (System.currentTimeMillis() > var3 + (long)timeout) {
					return false;
				}
			}
		} catch (InterruptedException var5) {
			return false;
		} catch (Exception var6) {
			var6.printStackTrace();
		}

		return false;
	}

}
