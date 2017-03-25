package net.imatruck.dsmanager.models;

import java.util.Map;

/**
 * Created by marc on 2017-03-24.
 */

public class APIInfoBase extends BaseModel {
    private Map<String, APIInfoData> data;

    public Map<String, APIInfoData> getData() {
        return data;
    }
}
