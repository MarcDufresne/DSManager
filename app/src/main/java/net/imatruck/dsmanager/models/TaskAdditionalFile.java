package net.imatruck.dsmanager.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marc on 2017-03-25.
 */

public class TaskAdditionalFile {

    private String filename;
    private double size;
    @SerializedName("size_downloaded")
    private double sizeDownloaded;
    private String priority;
    private int index;
    private boolean wanted;

    public String getFilename() {
        return filename;
    }

    public double getSize() {
        return size;
    }

    public double getSizeDownloaded() {
        return sizeDownloaded;
    }

    public String getPriority() {
        return priority;
    }

    public int getIndex() {
        return index;
    }

    public boolean isWanted() {
        return wanted;
    }

    public float getDownloadedPercent() {
        return Math.round((getSizeDownloaded() / getSize()) * 10000) / 100f;
    }
}
