package com.thatgamerblue.osbot.util;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.util.ItemContainer;
import org.osbot.rs07.input.mouse.MouseDestination;

public class BankCache extends ItemContainer {

    private Item[] items = new Item[0];

    public void updateBankCache(ItemContainer bank) {
        setItems(bank.getItems());
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    @Override
    public int getInterfaceChildId() {
        return 0;
    }

    @Override
    public MouseDestination getMouseDestination(int i) {
        return null;
    }

    @Override
    public int getInterfaceId() {
        return 0;
    }

    @Override
    public Item[] getItems() {
        return items;
    }

    @Override
    public void initializeModule() {

    }
}
