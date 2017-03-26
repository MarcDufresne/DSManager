package net.imatruck.dsmanager.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marc on 2017-03-26.
 */

public class DSStatsInfoData {

    @SerializedName("speed_download")
    private double speedDownload;

    @SerializedName("speed_upload")
    private double speedUpload;

    public double getSpeedDownload() {
        return speedDownload;
    }

    public double getSpeedUpload() {
        return speedUpload;
    }
}
