package com.thatgamerblue.osbot.pkhelper.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Base64;

public class ItemRemapper {

    private static final String remappingData = "H4sIAAAAAAAA/03Uy2rDMBAF0H/RuguNXqPJzxijliQkbcB9QCn998oXq7qbcGSH6xl5rB+3NHEnt1626+u7e3Kp5uJOqRYPK7mSbboEuMCRnMiZjBwNsE2rJwsZOVrhTC5kHV5aL8c9X9btcUM7FbE1wUIO5EhGrKG1quRKtmlDjqE1i+REzuQ9xzxaM/u3eU+W4aX1SHf+/Lisbw63Em5VOJMLWacFsZJgIQdyJCMneFjJlWzTATmhwJGcyHl4af2yu63b9Y5uAlIjmg42HT1ZyEiN6CxmciHrdEJOQmdJyIEcycjJ6CwpuZJteGm9BPf1sq0N3WAireJfmMjhSrZpjKBV7BFGcDiRMxk5hp3ACB42TxYycgw7gREcLmQdXlq/7D4e23pGNxnVZTw5R3IiZzKqK3gyzofDxZOFjJyCKnA+DBeyTuN8MMV7wvkwHMhxeGm6fzj3x/bduxHdv4/+m+A6vX8fhwUv8Fj4uNctXsWw2rujVZkrQcVY/f4BTJgXWE8FAAA=";
    private static JSONObject remapObject = null;

    public static int remapId(long id) {
        if (remapObject == null) {
            try {
                // https://stackoverflow.com/a/12531837
                java.io.ByteArrayInputStream bytein = new java.io.ByteArrayInputStream(Base64.getDecoder().decode(remappingData));
                java.util.zip.GZIPInputStream gzin = new java.util.zip.GZIPInputStream(bytein);
                java.io.ByteArrayOutputStream byteout = new java.io.ByteArrayOutputStream();

                int res = 0;
                byte buf[] = new byte[1024];
                while (res >= 0) {
                    res = gzin.read(buf, 0, buf.length);
                    if (res > 0) {
                        byteout.write(buf, 0, res);
                    }
                }
                byte uncompressed[] = byteout.toByteArray();
                remapObject = (JSONObject) new JSONParser().parse(new String(uncompressed));
            } catch (Exception e) {
                e.printStackTrace();
                return Math.toIntExact(id);
            }
        }
        return Math.toIntExact((Long) remapObject.getOrDefault(id + "", id));
    }

    public static int remapId(int id) {
        return remapId((long) id);
    }

}
