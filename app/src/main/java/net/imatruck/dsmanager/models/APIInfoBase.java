package net.imatruck.dsmanager.models;

import java.util.Map;


@SuppressWarnings("unused")
public class APIInfoBase extends BaseModel {
    private Map<String, APIInfoData> data;

    public Map<String, APIInfoData> getData() {
        return data;
    }
}
