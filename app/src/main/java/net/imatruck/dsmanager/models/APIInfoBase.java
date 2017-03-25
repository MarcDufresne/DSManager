package net.imatruck.dsmanager.models;

import java.util.Map;

/**
 * Created by marc on 2017-03-24.
 */

public class APIInfoBase {
    private Map<String, APIInfoData> data;
    private boolean success;

    public Map<String, APIInfoData> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }
}
