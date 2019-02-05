package com.thatgamerblue.osbot.dax_walker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.API;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DaxWalker extends API {

    private static final String API_URL = "https://thatgamerblue.com/osbot/walker.php?x1=%d&y1=%d&z1=%d&x2=%d&y2=%d&z2=%d";
    private final JSONParser parser = new JSONParser();
    private final HashMap<PathLocs, Position[]> cachedPaths = new HashMap<>();

    @Override
    public void initializeModule() {

    }

    private String makeRequestString(Position start, Position end) {
        return String.format(API_URL, start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ());
    }

    private boolean isPathCached(Position start, Position end) {
        return isPathCached(new PathLocs(start, end));
    }

    private boolean isPathCached(PathLocs location) {
        for(Map.Entry<PathLocs, Position[]> kV : cachedPaths.entrySet()) {
            if(kV.getKey().equals(location))
                return true;
        }
        return false;
    }

    private Position[] getCachedPath(Position start, Position end) {
        return getCachedPath(new PathLocs(start, end));
    }

    private Position[] getCachedPath(PathLocs location) {
        for(Map.Entry<PathLocs, Position[]> kV : cachedPaths.entrySet()) {
            if(kV.getKey().equals(location))
                return kV.getValue();
        }
        return null;
    }

    private void cachePath(Position start, Position end, Position[] path) {
        cachePath(new PathLocs(start, end), path);
    }

    private void cachePath(PathLocs location, Position[] path) {
        for(Map.Entry<PathLocs, Position[]> kV : cachedPaths.entrySet()) {
            if(kV.getKey().equals(location)) {
                kV.setValue(path);
                return;
            }
        }
        cachedPaths.put(location, path);
    }

    private Position[] parsePath(String json) throws ParseException {
        JSONObject root = (JSONObject) parser.parse(json);
        String status = (String) root.get("pathStatus");
        if(!status.equals("SUCCESS")) {
            return null;
        }
        JSONArray positions = (JSONArray) root.get("path");
        Position[] path = new Position[positions.size()];
        int idx = 0;
        for(Object obj : positions) {
            JSONObject pathPoint = (JSONObject) obj;
            int x, y, z;
            x = Math.toIntExact((long)pathPoint.get("x"));
            y = Math.toIntExact((long)pathPoint.get("y"));
            z = Math.toIntExact((long)pathPoint.get("z"));
            path[idx] = new Position(x, y, z);
            idx++;
        }
        return path;
    }

    public Position[] getPath(Position start, Position end) {
        try {
            if(start.getZ() != end.getZ()) {
                log("[ERROR] Dax Pathing only supports 2d planes");
                return null;
            }
            if(isPathCached(start, end)) {
                return getCachedPath(start, end);
            }
            return doPost(start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Position[] doPost(Position start, Position end) throws IOException, ParseException {
        URL api = new URL(makeRequestString(start, end));
        HttpsURLConnection conn = (HttpsURLConnection)api.openConnection();
        conn.setRequestMethod("GET");
        //conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.39 (KHTML, like Gecko) Chrome/42.0.2321.157 Safari/537.36 Edge/12.247");
        //conn.setRequestProperty("Referer", "OSBOT");
        int responseCode = conn.getResponseCode();
        InputStream in;
        if(responseCode < 400) {
            in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            //log(result.toString());
            Position[] path = parsePath(result.toString());
            cachePath(start, end, path);
            return path;
        } else {
            in = conn.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            log(result.toString());
        }
        return null;
    }

}
