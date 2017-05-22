package net.imatruck.dsmanager.models;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class DSGetInfoData {
    @SerializedName("is_manager")
    private boolean isManager;
    private int version;
    @SerializedName("version_string")
    private String versionString;

    public boolean isManager() {
        return isManager;
    }

    public int getVersion() {
        return version;
    }

    public String getVersionString() {
        return versionString;
    }
}
