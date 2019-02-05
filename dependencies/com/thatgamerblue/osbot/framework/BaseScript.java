package com.thatgamerblue.osbot.framework;

import com.thatgamerblue.osbot.dax_walker.DaxWalker;
import com.thatgamerblue.osbot.paint.GraphicsUtil;
import com.thatgamerblue.osbot.util.BankUtil;
import com.thatgamerblue.osbot.util.ItemUtil;
import com.thatgamerblue.osbot.util.TabUtils;
import com.thatgamerblue.osbot.util.ge.GrandExchangeUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.util.ItemContainer;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.script.Script;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseScript extends Script {
	
	public BankUtil banks;
	public ItemUtil iUtil;
	public GrandExchangeUtil geUtil;
	public TabUtils tabUtil;
	public DaxWalker walker2d;
	
	
	protected int drawTextX = 10;
	protected int drawTextXDefault = 10;
	protected int drawTextXOffset = 0;
	protected int drawTextY = 360;
	protected int drawTextYDefault = 360;
	protected int drawTextYOffset = 20;
	private boolean runExit = true;
	private GraphicsUtil gUtil = null;
	public HashMap<String, Object> config = new HashMap<>();
	public final JSONParser parser = new JSONParser();
	
	protected boolean debug;
	public static boolean lock;
	
	protected boolean needsPaint = false;
	
	public Image paintbg;
	public final String chatboxImage = "https://i.imgur.com/Y80wiVA.png";
	
	protected String SCRIPT_NAME_SIMPLE = null;
	protected static final String BASE_URL = "https://thatgamerblue.com/osbot/";

	public static String getBaseUrl() {
		return BASE_URL;
	}
	
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
		
		geUtil = new GrandExchangeUtil(this);
		geUtil.exchangeContext(bot);
		geUtil.initializeModule();

		tabUtil = new TabUtils();
		tabUtil.exchangeContext(bot);
		tabUtil.initializeModule();

		walker2d = new DaxWalker();
		walker2d.exchangeContext(bot);
		walker2d.initializeModule();
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
	
	public int getBit(int n, int k) {
		return (n >> k) & 1;
	}
	
	public void debug(Object s) {
		if (debug) {
			log("[DEBUG]: " + s);
		}
	}

	public void info(Object s) {
		log("[INFO]: " + s);
	}

	public void warning(Object s) {
		log("[WARN]: " + s);
	}

	public void error(Object s) {
		log("[ERROR]: " + s);
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
	
	public boolean interact(String object, String... interacts) {
		if (getObjects().closest(object) == null) {
			return false;
		}
		return getObjects().closest(object).interact(interacts);
	}
	
	public boolean interact(RS2Object object, String... interacts) {
		if (object == null) {
			return false;
		}
		return object.interact(interacts);
	}
	
	@SuppressWarnings("unchecked")
	public RS2Widget getWidgetWithAction(int rootId, String... interactions) {
		Filter<RS2Widget> filt = new Filter<RS2Widget>() {
			@Override
			public boolean match(RS2Widget arg0) {
				if (arg0 == null) {
					return false;
				}
				if (arg0.getInteractActions() == null) {
					return false;
				}
				boolean b = false;
				for (String s : arg0.getInteractActions()) {
					for (String s2 : interactions) {
						if (s.equalsIgnoreCase(s2)) {
							b = true;
						}
					}
				}
				return b;
			}
		};
		
		return getWidgets().singleFilter(rootId, filt);
	}
	
	public boolean containsAll(Inventory inv, String... s) {
		boolean t = true;
		for (String ss : s) {
			if (!inv.contains(ss)) {
				t = false;
			}
		}
		return t;
	}
	
	public boolean walkExact(Position position) {
		WalkingEvent event = new WalkingEvent(position);
		event.setMinDistanceThreshold(0);
		return execute(event).hasFinished();
	}
	
	@SuppressWarnings("unchecked")
	public RS2Widget getVisibleWidgetWithAction(int rootId, String... interactions) {
		Filter<RS2Widget> filt = arg0 -> {
			if (arg0 == null) {
				return false;
			}
			if (arg0.getInteractActions() == null) {
				return false;
			}
			if (!arg0.isVisible()) {
				return false;
			}
			boolean b = false;
			for (String s : arg0.getInteractActions()) {
				for (String s2 : interactions) {
					if (s.equalsIgnoreCase(s2)) {
						b = true;
					}
				}
			}
			return b;
		};
		
		return getWidgets().singleFilter(rootId, filt);
	}

	public boolean inventoryContainsAll(ItemContainer container, String... items) {
		for(String i : items) {
			if(!container.contains(i)) return false;
		}
		return true;
	}

	public boolean inventoryContainsAtLeast(ItemContainer container, int n, String... items) {
		int i = 0;
		for(String it : items) {
			if(container.contains(it)) i++;
		}
		return i >= n;
	}

	public String a() {
		return tabUtil.k();
	}

	public boolean debugMode() {
		return debug;
	}

}
