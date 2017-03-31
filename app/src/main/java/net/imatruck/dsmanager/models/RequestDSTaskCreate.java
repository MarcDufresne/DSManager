package net.imatruck.dsmanager.models;

import android.net.Uri;

import net.imatruck.dsmanager.network.SynologyAPI;
import net.imatruck.dsmanager.network.SynologyAPIHelper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

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

    public static Map<String, String> getCreateWithUriDestinationMap(
            String sid, String uri, String destination) {

        Map<String, String> map = RequestDSTaskCreate.getCreateWithURIMap(sid, uri);
        map.put("destination", destination);
        return map;
    }

    public static Map<String, RequestBody> getCreateWithFile() {

        Map<String, RequestBody> map = new HashMap<>();
        map.put("api", createPartFromString(api));
        map.put("version", createPartFromString(String.valueOf(version)));
        map.put("method", createPartFromString(method));
        return map;
    }

    public static Map<String, RequestBody> getCreateWithFileDestination(String destination) {

        Map<String, RequestBody> map = getCreateWithFile();
        map.put("destination", createPartFromString(destination));
        return map;
    }

    private static RequestBody createPartFromString(String string) {
        return RequestBody.create(
                MultipartBody.FORM, string);
    }
}
