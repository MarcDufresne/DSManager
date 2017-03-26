package net.imatruck.dsmanager.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marc on 2017-03-25.
 */

public class RequestDSTaskCreate {
    private static final String api = "SYNO.DownloadStation.Task";
    private static final int version = 3;
    private static final String method = "create";

    public static Map<String, String> getCreateWithURIMap(String sid, String uri) {
        HashMap<String, String> map = new HashMap<>();
        map.put("api", api);
        map.put("version", String.valueOf(version));
        map.put("method", method);
        map.put("uri", uri);
        map.put("_sid", sid);
        return map;
    }
}
