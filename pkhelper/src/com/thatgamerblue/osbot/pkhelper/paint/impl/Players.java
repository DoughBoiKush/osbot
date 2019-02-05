package com.thatgamerblue.osbot.pkhelper.paint.impl;

import com.thatgamerblue.osbot.pkhelper.PKHelper;
import com.thatgamerblue.osbot.pkhelper.enums.PrayerIcons;
import com.thatgamerblue.osbot.pkhelper.enums.Spotanim;
import com.thatgamerblue.osbot.pkhelper.hotkeys.actions.impl.EquipItemAction;
import com.thatgamerblue.osbot.pkhelper.hotkeys.actions.impl.SetPrayerAction;
import com.thatgamerblue.osbot.pkhelper.listeners.TickListener;
import com.thatgamerblue.osbot.pkhelper.tickers.impl.WealthCalculatorTicker;
import com.thatgamerblue.osbot.pkhelper.timers.Timers;
import com.thatgamerblue.osbot.pkhelper.events.EventType;
import com.thatgamerblue.osbot.pkhelper.events.SpotanimChangedEvent;
import com.thatgamerblue.osbot.pkhelper.paint.Images;
import com.thatgamerblue.osbot.pkhelper.paint.PaintModule;
import com.thatgamerblue.osbot.util.TextUtil;
import org.osbot.rs07.api.def.ItemDefinition;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.ui.PrayerButton;
import org.osbot.rs07.script.Script;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.EventObject;

import static com.thatgamerblue.osbot.pkhelper.timers.TimerType.*;

public class Players extends PaintModule {

    @Override
    public void doPaint(Graphics2D g, Script script, long currentTime) {
        g.drawString("Players: " + script.getPlayers().getAll().size(), 10, 50);
        int i = 0;
        g.setBackground(new Color(0, 0, 0, 100));
        for (Player player : script.getPlayers().getAll()) {
            if (script.widgets.getActiveWidgetRoots().size() != 18) {
                //continue;
            }
            i = 0;
            if (Timers.isFrozen(player, currentTime)) {
                String time = TextUtil.formatTime(Timers.getUnfreezeTime(player) - currentTime);
                BufferedImage image1 = Images.getImage(Timers.getFreezeImage(player));
                PKHelper.inst.paintAPI.drawImageAndString(g, time, image1, player, i++);
            }
            if (Timers.isTeleblocked(player, currentTime)) {
                String time = TextUtil.formatTime(Timers.getUntbTime(player) - currentTime);
                BufferedImage image1 = Images.getImage("teleblock");
                PKHelper.inst.paintAPI.drawImageAndString(g, time, image1, player, i++);
            }
            if (Timers.isVenged(player, currentTime)) {
                String time = TextUtil.formatTime(Timers.getVengTime(player) - currentTime);
                BufferedImage image1 = Images.getImage("veng");
                PKHelper.inst.paintAPI.drawImageAndString(g, time, image1, player, i++);
            }
            if (PKHelper.isItemDataEnabled()) {
                if (PKHelper.inst.getTickers().get("WealthCalculator") != null) {
                    int price = ((WealthCalculatorTicker) PKHelper.inst.getTickers().get("WealthCalculator")).getPlayerWealth(player);
                    PKHelper.inst.paintAPI.drawImageAndString(g, "Wealth: " + TextUtil.formatValue(price), null, player, i++);
                    /*for(int j : player.getDefinition().getAppearance()) {
                        //PKHelper.inst.paintAPI.drawImageAndString(g, "Model: " + j, null, player, i++);
                        ItemDefinition def = ItemDefinition.forId(j-512);
                        if(def != null) {
                            PKHelper.inst.paintAPI.drawImageAndString(g, def.getName(), null, player, i++);
                        }
                    }*/
                }
            }
        }
    }

    @Override
    public void handleEvent(EventObject event, EventType type) {
        long currentTime = System.currentTimeMillis();
        switch (type) {
            case KEYDOWN:
                switch (((KeyEvent) event).getKeyCode()) {
                    case KeyEvent.VK_F12:
                        PKHelper.toggleItemDataEnabled();
                        break;
                    case KeyEvent.VK_F11:
                        TextUtil.typeStringInstant(PKHelper.inst.bot, "0123456789", false);
                        break;
                    default:
                        break;
                }
                break;
        }
        if (event instanceof SpotanimChangedEvent) {
            SpotanimChangedEvent evnt = (SpotanimChangedEvent) event;
            Player player = evnt.getPlayer();
            Spotanim newAnim = Spotanim.getById(evnt.getNewAnim());
            if (newAnim != null) {
                int time = newAnim.getTime();
                int prayIcon = TickListener.getPrayerIconLastTick(player);
                if(prayIcon == -1337) {
                    prayIcon = player.getPrayerIcon();
                }
                if (newAnim.halfOnPrayMage() && prayIcon == PrayerIcons.PROTECT_FROM_MAGE.getId()) {
                    time /= 2;
                }
                switch (newAnim.getType()) {
                    case FREEZE:
                        if (Timers.isFrozen(player, currentTime)) {
                            break;
                        }
                        Timers.setFrozen(player, time, currentTime, newAnim.getImageName());
                        break;
                    case VENGEANCE:
                        if (Timers.isVenged(player, currentTime)) {
                            break;
                        }
                        Timers.setVenged(player, time, currentTime);
                        break;
                    case TELEBLOCK:
                        if (Timers.isTeleblocked(player, currentTime)) {
                            break;
                        }
                        Timers.setTeleblocked(player, time, currentTime);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
