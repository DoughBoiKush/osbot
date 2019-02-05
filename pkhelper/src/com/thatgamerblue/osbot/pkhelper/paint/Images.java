package com.thatgamerblue.osbot.pkhelper.paint;

import com.thatgamerblue.osbot.pkhelper.PKHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;

public class Images {

    private static final HashMap<String, BufferedImage> images = new HashMap<>();
    private static ArrayList<BufferedImage> all = new ArrayList<>();

    public static BufferedImage getImage(String string) {
        if (!images.containsKey(string)) {
            cacheImage(string);
        }
        return images.getOrDefault(string, null);
    }

    public static ArrayList<BufferedImage> getAll() {
        return all;
    }

    public static void putImage(String s, BufferedImage i) {
        images.put(s, i);
        all.add(i);
    }

    public static void cacheImage(String image) {
        try {
            File file = new File(PKHelper.dataFolder, image + ".png");
            if (file.exists()) {
                images.put(image, ImageIO.read(file));
                return;
            }
            InputStream is = new URL(PKHelper.getBaseUrl() + image + ".png").openStream();
            Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            is.close();
            putImage(image, ImageIO.read(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
