package net.imatruck.dsmanager.models;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class TaskAdditionalTracker {

    private String url;
    private String status;
    @SerializedName("update_timer")
    private int updateTimer;
    private int seeds;
    private int peers;

    public String getUrl() {
        return url;
    }

    public String getStatus() {
        return status;
    }

    public int getUpdateTimer() {
        return updateTimer;
    }

    public int getSeeds() {
        return seeds;
    }

    public int getPeers() {
        return peers;
    }
}
