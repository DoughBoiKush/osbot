package com.thatgamerblue.osbot.util;

import org.osbot.rs07.Bot;
import org.osbot.rs07.script.Script;

import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

public class TextUtil {

    public static final String formatTime(final long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60;
        m %= 60;
        h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
                h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
                        String.format("%02d:%02d", m, s);
    }

    private static final DecimalFormat UNIT_FORMAT = new DecimalFormat(".0#");

    public static String formatValue(final long value) {
        String suffix;
        double convertedValue;

        if (value >= 1_000_000_000) {
            convertedValue = ((double) value / 1_000_000_000);
            suffix = "B";
        } else if (value >= 1_000_000) {
            convertedValue = ((double) value / 1_000_000);
            suffix = "M";
        } else if (value >= 1000) {
            convertedValue = ((double) value / 1000);
            suffix = "K";
        } else {
            return String.valueOf(value);
        }

        convertedValue = Math.floor(convertedValue * 100) / 100;

        return UNIT_FORMAT.format(convertedValue) + suffix;
    }

    public static long unformatValue(String s) {
        String lower = s.toLowerCase();
        long l = Long.parseLong(s.replaceAll("[^0-9]", ""));
        if (s.contains(".")) {
            l = Long.parseLong(s.replaceAll("[^0-9]", ""));
            l /= 10;
        }
        if (lower.contains("k")) {
            l *= 1000;
        } else if (lower.contains("m")) {
            l *= 1_000_000;
        } else if (lower.contains("b")) {
            l *= 1_000_000_000;
        }
        return l;
    }

    public static void typeStringInstant(Bot bot, String output, boolean pressEnter) {
        for (int i = 0; i < output.length(); i++) {
            char c = output.charAt(i);
            int code = KeyEvent.getExtendedKeyCodeForChar(c);
            // Type the character
            bot.getKeyEventHandler().generateBotKeyEvent(400, System.currentTimeMillis(), 0, code, c);
        }

        if (pressEnter) {
            // Press enter
            bot.getKeyEventHandler().generateBotKeyEvent(401, System.currentTimeMillis(), 0, 10, '\u0000');
            // Release enter
            bot.getKeyEventHandler().generateBotKeyEvent(402, System.currentTimeMillis(), 0, 10, '\u0000');
        }
    }
}