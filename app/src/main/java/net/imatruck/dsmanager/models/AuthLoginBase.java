package net.imatruck.dsmanager.models;

import java.util.Map;

/**
 * Created by marc on 2017-03-24.
 */

public class AuthLoginBase {
    private Map<String, String> data;
    private boolean success;

    public Map<String, String> getData() {
        return data;
    }

    public String getSid() {
        return this.getData().get("sid");
    }

    public boolean isSuccess() {
        return success;
    }
}
