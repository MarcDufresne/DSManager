package net.imatruck.dsmanager.models;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class TaskStatusExtra {

    @SerializedName("error_detail")
    private String errorDetail;

    @SerializedName("unzip_progress")
    private int unzipProgress;
}
