package com.thatgamerblue.osbot.framework;

import com.thatgamerblue.osbot.paint.GraphicsUtil;
import com.thatgamerblue.osbot.util.BankUtil;
import com.thatgamerblue.osbot.util.ItemUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osbot.rs07.script.Plugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BasePlugin extends Plugin {

    public BankUtil banks;
    public ItemUtil iUtil;

    protected int drawTextX = 10;
    protected int drawTextXDefault = 10;
    protected int drawTextXOffset = 0;
    protected int drawTextY = 360;
    protected int drawTextYDefault = 360;
    protected int drawTextYOffset = 20;
    private boolean runExit = true;
    private GraphicsUtil gUtil = null;
    public final JSONParser parser = new JSONParser();

    public HashMap<String, Object> config = new HashMap<>();
    protected boolean needsPaint = false;
    protected boolean debug;
    public Image paintbg;

    protected String SCRIPT_NAME_SIMPLE = null;
    protected static final String BASE_URL = "https://thatgamerblue.com/osbot/";

    public String getName() {
        return SCRIPT_NAME_SIMPLE == null ? super.getName() : SCRIPT_NAME_SIMPLE;
    }

    public static String getOSBotDataDir() {
        return System.getProperty("user.home") + File.separator + "OSBot" + File.separator + "Data" + File.separator;
    }

    public void onStart() throws InterruptedException {
        setupUtils();

        if ((new File(getDirectoryData(), "bluescriptsdebug")).exists()) {
            debug = true;
        }

        start();
    }

    public abstract void start() throws InterruptedException;

    public int onLoop() throws InterruptedException {
        return loop();
    }

    public abstract int loop() throws InterruptedException;

    public void onPaint(Graphics2D g) {
        if (gUtil == null) {
            gUtil = new GraphicsUtil(g);
        }
        try {
            drawTextX = drawTextXDefault;
            drawTextXOffset = 0;
            drawTextY = drawTextYDefault;
            drawTextYOffset = 20;

            if (needsPaint && paintbg == null) {
                paintbg =
                        ImageIO.read(BaseScript.class.getResourceAsStream("/resources/paint.png"));
            }

            drawPaint(gUtil);
        } catch (Exception e) {
            StringWriter sw = new StringWriter(1024 * 10);
            e.printStackTrace(new PrintWriter(sw));
            log(sw.toString());
        }
    }

    public abstract void drawPaint(GraphicsUtil g);

    public void onExit() {
        if(runExit)
            exit();
    }

    public abstract void exit();

    @SuppressWarnings("deprecation")
    public void setupUtils() {
        banks = new BankUtil();
        banks.exchangeContext(bot);
        banks.initializeModule();

        iUtil = new ItemUtil();
        iUtil.exchangeContext(bot);
        iUtil.initializeModule();
    }

    public void drawShadowText(String s, GraphicsUtil g, Color textColor, Color shadowColor) {
        g.drawShadowedString(s, drawTextX, drawTextY, textColor, shadowColor);
        drawTextX += drawTextXOffset;
        drawTextY += drawTextYOffset;
    }

    public void drawShadowText(String s, GraphicsUtil g) {
        drawShadowText(s, g, Color.CYAN, Color.BLACK);
    }

    public void drawText(String s, GraphicsUtil g) {
        drawText(s, Color.BLACK, g);
    }

    public void drawText(String s, Color color, GraphicsUtil g) {
        Color col = g.g.getColor();
        g.g.setColor(color);
        g.g.drawString(s, drawTextX, drawTextY);
        g.g.setColor(col);
        drawTextX += drawTextXOffset;
        drawTextY += drawTextYOffset;
    }

    public void quit(boolean runOnExit, boolean logOut) {
        runExit = runOnExit;
        stop(logOut);
    }

    public boolean loadConfig() {
        File f = new File(getOSBotDataDir(), getName() + ".json");
        JSONObject outer;
        try {
            outer = (JSONObject) parser.parse(new FileReader(f));
            for (Iterator<?> iterator = outer.keySet().iterator(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                config.put(key, outer.get(key));
            }
        } catch (IOException | ParseException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log(sw.toString());
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public void saveConfig() {
        File f = new File(getOSBotDataDir(), getName() + ".json");
        JSONObject outer = new JSONObject();
        Iterator<?> it = config.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<?, ?> pair = (Map.Entry<?, ?>) it.next();
            outer.put(pair.getKey(), pair.getValue());
        }
        try (FileWriter fw = new FileWriter(f)) {
            fw.write(outer.toJSONString());
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log(sw.toString());
        }
    }

    public String getConfigValueString(String key) {
        if (config.get(key) instanceof String) {
            return config.get(key).toString();
        }
        return null;
    }

    public int getConfigValueInt(String key) {
        return (int) config.get(key);
    }

    public boolean getConfigValueBoolean(String key) {
        return (boolean) config.get(key);
    }

}
