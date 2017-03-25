package net.imatruck.dsmanager.models;

/**
 * Created by marc on 2017-03-24.
 */

public class AuthLoginBase extends BaseModel {
    private AuthLoginData data;

    public AuthLoginData getData() {
        return data;
    }

    public String getSid() {
        return this.getData().getSid();
    }
}
