package net.imatruck.dsmanager.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marc on 2017-03-25.
 */

public class TaskAdditionalTransfer {

    @SerializedName("downloaded_pieces")
    private int downloadedPieces;

    @SerializedName("size_downloaded")
    private double sizeDownloaded;

    @SerializedName("size_uploaded")
    private double sizeUploaded;

    @SerializedName("speed_download")
    private double speedDownload;

    @SerializedName("speed_upload")
    private double speedUpload;

    public int getDownloadedPieces() {
        return downloadedPieces;
    }

    public double getSizeDownloaded() {
        return sizeDownloaded;
    }

    public double getSizeUploaded() {
        return sizeUploaded;
    }

    public double getSpeedDownload() {
        return speedDownload;
    }

    public double getSpeedUpload() {
        return speedUpload;
    }
}
