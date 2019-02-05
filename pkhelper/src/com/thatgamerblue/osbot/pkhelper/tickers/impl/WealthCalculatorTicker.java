package com.thatgamerblue.osbot.pkhelper.tickers.impl;

import com.thatgamerblue.osbot.framework.BaseScript;
import com.thatgamerblue.osbot.pkhelper.PKHelper;
import com.thatgamerblue.osbot.pkhelper.tickers.Ticker;
import com.thatgamerblue.osbot.pkhelper.utils.ItemRemapper;
import com.thatgamerblue.osbot.util.ge.Prices;
import org.osbot.rs07.api.model.Player;

import java.util.HashMap;

public class WealthCalculatorTicker extends Ticker {

    private final HashMap<Player, Integer> wealthMap = new HashMap<>();

    @Override
    public boolean tick(BaseScript script) {
        if (!PKHelper.isItemDataEnabled()) {
            return false;
        }
        for (Player p : script.getPlayers().getAll()) {
            int price = 0;
            if (p.getDefinition() == null) {
                continue;
            }
            for (int modelId : p.getDefinition().getAppearance()) {
                int itemId = modelId - 512;
                if (itemId < 0) {
                    continue;
                }
                itemId = ItemRemapper.remapId(itemId);
                int toAdd = Prices.getItemPriceOsbuddy(itemId, script);
                if (toAdd == 0) {
                    // toAdd = ItemDefinition.forId(itemId);
                    // TODO: figure out high alch prices
                }
                price += toAdd;
            }
            wealthMap.put(p, price);
        }
        return true;
    }

    public int getPlayerWealth(Player player) {
        return wealthMap.getOrDefault(player, 1000);
    }

}
