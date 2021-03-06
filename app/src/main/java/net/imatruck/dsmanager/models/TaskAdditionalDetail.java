package net.imatruck.dsmanager.models;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class TaskAdditionalDetail {

    @SerializedName("completed_time")
    private long completedTime;

    @SerializedName("connected_leechers")
    private int connectedLeechers;

    @SerializedName("connected_peers")
    private int connectedPeers;

    @SerializedName("connected_seeders")
    private int connectedSeeders;

    @SerializedName("create_time")
    private long createTime;

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

    public long getCompletedTime() {
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

    public long getCreateTime() {
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
