package com.thatgamerblue.osbot.pkhelper;

import com.thatgamerblue.osbot.framework.BaseScript;
import com.thatgamerblue.osbot.paint.GraphicsUtil;
import com.thatgamerblue.osbot.pkhelper.listeners.KeyboardListener;
import com.thatgamerblue.osbot.pkhelper.listeners.MouseListener;
import com.thatgamerblue.osbot.pkhelper.listeners.TickListener;
import com.thatgamerblue.osbot.pkhelper.paint.Images;
import com.thatgamerblue.osbot.pkhelper.paint.PaintAPI;
import com.thatgamerblue.osbot.pkhelper.paint.PaintModule;
import com.thatgamerblue.osbot.pkhelper.paint.impl.Players;
import com.thatgamerblue.osbot.pkhelper.tickers.Ticker;
import com.thatgamerblue.osbot.pkhelper.tickers.impl.SpotanimTicker;
import com.thatgamerblue.osbot.pkhelper.tickers.impl.WealthCalculatorTicker;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ScriptManifest(info = "Freeze Timer Overlays & Clanning Tools", author = "ThatGamerBlue", version = 1.0, logo = "https://i.imgur.com/RBBeX0C.png", name = "Blue's PKing Helper")
public class PKHelper extends BaseScript {

    public static File dataFolder = new File(getOSBotDataDir(), "PKHelper");

    public static PKHelper inst;

    public PaintAPI paintAPI;

    private KeyboardListener keyListener;
    private MouseListener mouseListener;
    private TickListener tickListener;

    public static final Color CONFIG_BACKGROUND_COLOR = new Color(0x0d, 0x0d, 0x0d, 150);

    public static final HashMap<String, Ticker> tickers = new HashMap<>();
    public static final ArrayList<PaintModule> paintModules = new ArrayList<>();
    public static final ArrayList<PaintModule> configModules = new ArrayList<>();

    public static final Font font = new Font("Tahoma", 0, 12);

    private static boolean itemDataEnabled = true;
    private static boolean canEnableItemData = true;

    public static String getBaseUrl() {
        return BASE_URL + "pkhelper/";
    }

    public static boolean isItemDataEnabled() {
        return itemDataEnabled;
    }

    public static void toggleItemDataEnabled() {
        itemDataEnabled = !itemDataEnabled;
    }

    public static boolean canEnableItemData() {
        return canEnableItemData;
    }

    @Override
    public void start() {

        dataFolder.mkdirs();

        inst = this;

        //region Image Loading
        Images.cacheImage("ice_barrage");
        Images.cacheImage("ice_blitz");
        Images.cacheImage("ice_burst");
        Images.cacheImage("ice_rush");
        Images.cacheImage("entangle");
        Images.cacheImage("snare");
        Images.cacheImage("bind");
        Images.cacheImage("teleblock");
        Images.cacheImage("veng");
        //endregion

        //region Ticker Setup
        tickers.put("Spotanim", new SpotanimTicker());
        tickers.put("WealthCalculator", new WealthCalculatorTicker());
        //endregion

        paintAPI = new PaintAPI();
        paintAPI.exchangeContext(bot);

        //region Paint Setup
        paintModules.add(new Players());
        //endregion

        //region Config Setup
        //endregion

        keyListener = new KeyboardListener();
        mouseListener = new MouseListener();
        tickListener = new TickListener(this);
        getBot().addKeyListener(keyListener);
        getBot().addMouseListener(mouseListener);
        getBot().addGameTickListener(tickListener);
        debug("got to end of onstart");
    }

    public void tickTickers() {
        for (Map.Entry<String, Ticker> t : tickers.entrySet()) {
            t.getValue().tick(this);
        }
    }

    @Override
    public int loop() throws InterruptedException {
        tickTickers();
        return 150;
    }

    @Override
    public void exit() {
        if (keyListener != null)
            getBot().removeKeyListener(keyListener);
        if (mouseListener != null)
            getBot().removeMouseListener(mouseListener);
        if (tickListener != null)
            getBot().removeGameTickListener(tickListener);
    }

    @Override
    public void drawPaint(GraphicsUtil g) {
        if (paintAPI == null) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        g.g.setColor(Color.WHITE);
        g.g.setFont(font);
        // draw regular paint

        for (PaintModule pModule : paintModules) {
            pModule.doPaint(g.g, this, currentTime);
        }

        if (keyListener != null && keyListener.isKeyDown(KeyEvent.VK_ALT)) {
            // draw config overlay
            g.g.setColor(CONFIG_BACKGROUND_COLOR);
            g.g.fillRect(0, 0, bot.getCanvas().getWidth(), bot.getCanvas().getHeight());
            for (PaintModule pModule : configModules) {
                pModule.doPaint(g.g, this, currentTime);
            }
        }
    }

    public HashMap<String, Ticker> getTickers() {
        return tickers;
    }

}
