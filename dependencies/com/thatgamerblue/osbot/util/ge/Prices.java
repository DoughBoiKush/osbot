package com.thatgamerblue.osbot.util.ge;

import com.thatgamerblue.osbot.util.TextUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osbot.rs07.script.Script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Prices {

    private static final HashMap<Integer, CachedItem> cache = new HashMap<>();
    private static final String BASE_URL_JAGEX =
            "http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=";
    private static final String OSBUDDY_API_URL =
            "https://storage.googleapis.com/osbuddy-exchange/summary.json";
    private static JSONObject osbuddy;

    public static int getItemPriceJagex(int id, Script s) {
        if (id == 995) {
            return 1;
        }
        if (cache.containsKey(id)) {
            CachedItem item = cache.get(id);
            if (item.getTime() < new Date().getTime()) {
                if (!cacheItemJagex(id, s)) {
                    return 0;
                }
            }
            return item.getPrice();
        } else {
            if (!cacheItemJagex(id, s)) {
                return 0;
            }
            CachedItem item = cache.get(id);
            return item.getPrice();
        }
    }

    public static int getItemPriceOsbuddy(int id, Script s) {
        if (id == 995) {
            return 1;
        }
        if(osbuddy == null) {
            try {
                String result = getUrlContents(new URL(OSBUDDY_API_URL));
                if (result == null) {
                    return 0;
                }
                osbuddy = (JSONObject) new JSONParser().parse(result);
            } catch (ParseException | IOException ex) {
                return 0;
            }
        }
        JSONObject itemObject = (JSONObject) osbuddy.get(id + "");
        if(itemObject == null) {
            return 0;
        }
        return Math.toIntExact((long)itemObject.get("overall_average"));
    }

    private static boolean cacheItemJagex(int id, Script s) {
        try {
            CachedItem item = null;
            if (cache.containsKey(id)) {
                item = cache.get(id);
            }
            if (s != null) {
                //s.log("Caching price for item id: " + id);
            }
            URL url = new URL(BASE_URL_JAGEX + id);
            String result = getUrlContents(url);
            if (result == null) {
                cache.put(id, new CachedItem("", 0, Long.MAX_VALUE));
                return false;
            }
            JSONParser parser = new JSONParser();
            JSONObject obj1 = (JSONObject) parser.parse(result);
            JSONObject obj2 = (JSONObject) obj1.get("item");
            JSONObject obj3 = (JSONObject) obj2.get("current");
            String name = (String) obj2.get("name");
            int price = (int) TextUtil.unformatValue((String) obj3.get("price"));
            if (s != null) {
                s.log(name);
            }
            if (s != null) {
                s.log(price);
            }
            if (item == null) {
                item = new CachedItem(name, price, new Date().getTime() + 86400000);
            } else {
                item.setPrice(price);
                item.setTime(new Date().getTime());
            }
            if (s != null) {
                //s.log("Cached price for item id: " + id + ", price: " + price);
            }
            cache.put(id, item);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String getUrlContents(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
        conn.setRequestMethod("GET");
        conn.connect();
        if (conn.getResponseCode() != 200) {
            return null;
        }
        String result = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
        return result;
    }


}
