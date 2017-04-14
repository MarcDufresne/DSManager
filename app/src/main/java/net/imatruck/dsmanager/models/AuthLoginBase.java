package net.imatruck.dsmanager.models;


@SuppressWarnings("unused")
public class AuthLoginBase extends BaseModel {
    private AuthLoginData data;

    public AuthLoginData getData() {
        return data;
    }

    public String getSid() {
        return this.getData().getSid();
    }
}
