package net.imatruck.dsmanager.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marc on 2017-03-25.
 */

public class TaskAdditionalDetail {

    @SerializedName("completed_time")
    private double completedTime;

    @SerializedName("connected_leechers")
    private int connectedLeechers;

    @SerializedName("connected_peers")
    private int connectedPeers;

    @SerializedName("connected_seeders")
    private int connectedSeeders;

    @SerializedName("create_time")
    private double createTime;

    private String destination;

    @SerializedName("seedelapsed")
    private int seedElapsed;

    @SerializedName("started_time")
    private double startedTime;

    @SerializedName("total_peers")
    private int totalPeers;

    @SerializedName("total_pieces")
    private int totalPieces;

    @SerializedName("unzip_password")
    private String unzipPassword;

    private String uri;

    @SerializedName("waiting_seconds")
    private int waitingSeconds;

    public double getCompletedTime() {
        return completedTime;
    }

    public int getConnectedLeechers() {
        return connectedLeechers;
    }

    public int getConnectedPeers() {
        return connectedPeers;
    }

    public int getConnectedSeeders() {
        return connectedSeeders;
    }

    public double getCreateTime() {
        return createTime;
    }

    public String getDestination() {
        return destination;
    }

    public int getSeedElapsed() {
        return seedElapsed;
    }

    public double getStartedTime() {
        return startedTime;
    }

    public int getTotalPeers() {
        return totalPeers;
    }

    public int getTotalPieces() {
        return totalPieces;
    }

    public String getUnzipPassword() {
        return unzipPassword;
    }

    public String getUri() {
        return uri;
    }

    public int getWaitingSeconds() {
        return waitingSeconds;
    }
}
