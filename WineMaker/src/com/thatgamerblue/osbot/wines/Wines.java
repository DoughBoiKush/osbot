package com.thatgamerblue.osbot.wines;

import com.thatgamerblue.osbot.framework.BaseScript;
import com.thatgamerblue.osbot.paint.GraphicsUtil;
import com.thatgamerblue.osbot.util.ItemUtil;
import com.thatgamerblue.osbot.util.Sleep;
import com.thatgamerblue.osbot.util.TextUtil;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.input.mouse.BotMouseListener;
import org.osbot.rs07.input.mouse.PointDestination;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.Condition;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;

@ScriptManifest(author = "ThatGamerBlue", info = "Makes Jugs of Wine for Mad Cooking XP", logo = "https://i.imgur.com/fLsc09D.png", name = "Blue's Wine Maker", version = 1.7)
public class Wines extends BaseScript {
	
	private long startedAt, runtime;
	private String state = "Starting";
	private static boolean drawPaint = true;
	private ItemUtil iUtil;
	private int att = 0;
	private Rectangle clickBox = null;
	private MyMouseListener mL;
	
	
	private final Area[] BANKS = { // not the most efficient way but it works
			(Banks.DRAYNOR),
			(Banks.AL_KHARID),
			(Banks.LUMBRIDGE_UPPER),
			(Banks.FALADOR_EAST),
			(Banks.FALADOR_WEST),
			(Banks.VARROCK_EAST),
			(Banks.VARROCK_WEST),
			(Banks.GRAND_EXCHANGE),
			(Banks.CAMELOT),
			(Banks.CATHERBY),
			(Banks.EDGEVILLE),
			(Banks.YANILLE),
			(Banks.GNOME_STRONGHOLD),
			(Banks.ARDOUGNE_NORTH),
			(Banks.ARDOUGNE_SOUTH),
			(Banks.CASTLE_WARS),
			(Banks.DUEL_ARENA),
			(Banks.PEST_CONTROL),
			(Banks.CANIFIS),
			(Banks.TZHAAR),
			(Banks.PISCARILIUS_HOUSE),
			(Banks.ARCEUUS_HOUSE),
			(Banks.HOSIDIUS_HOUSE),
			(Banks.SHAYZIEN_HOUSE),
			(Banks.LOVAKENGJ_HOUSE),
			(Banks.LOVAKITE_MINE)};
	
	@SuppressWarnings("deprecation")
	public void start() {
		bot.addMouseListener(mL = (new MyMouseListener()));
		needsPaint = true;
		
		startedAt = System.currentTimeMillis();
		runtime = 1;
		
		iUtil = new ItemUtil();
		iUtil.exchangeContext(bot);
		
		getExperienceTracker().start(Skill.COOKING);
		
		if (getSkills().getStatic(Skill.COOKING) < 35) {
			log("Can't make wines at this cooking level - stopping");
			stop(true);
		}
		
		boolean atBank = false;
		for (NPC n : getNpcs().getAll()) {
			if (n.hasAction("Bank")) {
				atBank = true;
				break;
			}
		}
		if(!atBank) {
			for (RS2Object obj : getObjects().getAll()) {
				if (obj.hasAction("Bank")) {
					atBank = true;
					break;
				}
			}
		}
		if (!atBank) {
			getWalking().webWalk(BANKS);
		}
		
		if ((new File(getDirectoryData(), "winemakerdebug")).exists()) {
			debug = true;
		}
		
	}
	
	public int loop() throws InterruptedException {
		if (!client.isLoggedIn()) {
			debug("Not logged in - waiting...");
			return 1000;
		}
		
		debug(getState());
		switch (getState()) {
			case BANK:
				state = "Banking";
				if (!getBank().isOpen()) {
					getBank().open();
				}
				Sleep.sleepUntil(() -> getBank().isOpen(), 3000);
				getBank().depositAll();
				Sleep.sleepUntil(() -> getInventory().isEmpty(), 3000);
				if (!getBank().contains("Grapes") || !getBank().contains("Jug of Water")) {
					att++;
					log("Out of water / grapes, attempts: " + att);
					if (att == 5) {
						stop(true);
					}
					return 450;
				}
				getBank().withdraw("Jug of Water", 14);
				sleep(50);
				getBank().withdraw("Grapes", 14);
				Sleep.sleepUntil(() -> getInventory().contains("Jug of Water") &&
						getInventory().contains("Grapes"), 3000);
				if (getInventory().contains("Unfermented Wine", "Jug of Wine", "Jug of bad wine")) {
					getBank().depositAll("Unfermented Wine", "Jug of Wine", "Jug of bad wine");
					if (!getInventory().contains("Jug of Water")) {
						getBank().withdraw("Jug of Water", 14);
					}
					if (!getInventory().contains("Grapes")) {
						getBank().withdraw("Grapes", 14);
					}
				}
				getBank().close();
				att = 0;
				return 150;
			case MAKEWINE:
				
				state = "Making wines";
				if (getTabs().getOpen() != Tab.INVENTORY) {
					getTabs().open(Tab.INVENTORY);
				}
				Sleep.sleepUntil(() -> getTabs().getOpen() == Tab.INVENTORY, 3000);
				
				getMouse().move(iUtil.getLastInInventory("Jug of Water", getInventory()) == null ?
						new PointDestination(bot, 30, 30) : iUtil.getLastInInventory("Jug of Water", getInventory()));
				if (getMouse().getPosition().x > 25 && getMouse().getPosition().x < 35) {
					if (getMouse().getPosition().y > 25 && getMouse().getPosition().y < 35) {
						log("Failed to find jugs of water");
						stop(false);
					}
				}
				getMouse().click(false);
				
				if (getInventory().getItem("Grapes") != null) {
					getInventory().interact("Use", "Grapes");
				}
				
				getKeyboard().typeContinualKey((char) KeyEvent.VK_SPACE, 0, new Condition() {
					@Override
					public boolean evaluate() {
						RS2Widget actionWidget = getWidgetWithAction(270, "Make");
						if(actionWidget == null) {
							return false;
						}
						for(RS2Widget widget : actionWidget.getChildWidgets()) {
							if(widget != null && widget.isVisible() && widget.isThirdLevel() && widget.getItemId() == 5610) {
								return true;
							}
						}
						return myPlayer().isAnimating();
					}
				});
				
				getKeyboard().releaseKey(KeyEvent.VK_SPACE);
				Sleep.sleepUntil(() -> myPlayer().isAnimating(), 3200);
				getMouse().moveOutsideScreen();
				Sleep.sleepUntil(() -> !myPlayer().isAnimating(), 2*60*1000);
				
				if (random(0, 4) == 4 && getSkills().getStatic(Skill.COOKING) < 67) {
					long curXp = skills.getExperience(Skill.COOKING);
					Sleep.sleepUntil(() -> curXp != skills.getExperience(Skill.COOKING), 15000);
				}
				
				return 150;
			case WAIT:
				state = "Waiting";
				return 500;
			default:
				return 50;
		}
	}
	
	public void drawPaint(GraphicsUtil g) {
		runtime = System.currentTimeMillis() - startedAt;
		if(clickBox == null) {
			clickBox = new Rectangle(0, 337, paintbg.getWidth(null), paintbg.getWidth(null));
		}
		
		if(drawPaint) {
			g.g.drawImage(paintbg, 0, 337, null);
			g.g.setColor(Color.BLACK);
			drawText("State: " + state, g);
			drawText("Runtime: " + TextUtil.formatTime(runtime), g);
			drawText("XP (hr): " + getGainedXp()
					+ " (" + getXpHour() + ")", g);
			drawText("Level: " + getSkills().getStatic(Skill.COOKING)
					+ " (" + getExperienceTracker().getGainedLevels(Skill.COOKING)
					+ ")", g);
			if (getSkills().getStatic(Skill.COOKING) != 99) {
				drawText("TTL: " + TextUtil.formatTime(getTTL()), g);
			}
			drawTextX += 125;
			drawTextY -= drawTextYOffset;
			drawText("Version: " + getVersion(), g);
		}
		
		g.paintMouse(this, false);
		
	}
	
	private int getGainedXp() {
		return getExperienceTracker().getGainedXP(Skill.COOKING);
	}
	
	private long getTTL() {
		return getExperienceTracker().getTimeToLevel(Skill.COOKING);
	}
	
	private int getXpHour() {
		return getExperienceTracker().getGainedXPPerHour(Skill.COOKING);
	}
	
	private State getState() {
		if (myPlayer().isAnimating()) {
			return State.WAIT;
		}
		
		if (getInventory().contains("Grapes") && getInventory().contains("Jug of Water") &&
				!getInventory().contains("Unfermented Wine", "Jug of Wine", "Jug of bad wine")) {
			return State.MAKEWINE;
		}
		
		return State.BANK;
	}
	
	enum State {
		BANK,
		MAKEWINE,
		WAIT
	}
	
	@Override
	public void exit() {
		bot.removeMouseListener(mL);
		if (skills != null) {
			long curXp = skills.getExperience(Skill.COOKING);
			Sleep.sleepUntil(() -> curXp != skills.getExperience(Skill.COOKING), 10000);
		}
	}
	
	class MyMouseListener extends BotMouseListener {
		@Override
		public void checkMouseEvent(MouseEvent mouseEvent) {
			switch(mouseEvent.getID()) {
				case MouseEvent.MOUSE_CLICKED:
					if(clickBox.contains(mouseEvent.getLocationOnScreen()) && mouseEvent.getButton() == MouseEvent.BUTTON2) {
						drawPaint = !drawPaint;
						mouseEvent.consume();
					}
					break;
				default:
					break;
			}
		}
	}
	
}