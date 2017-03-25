package net.imatruck.dsmanager.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marc on 2017-03-25.
 */

public class TaskStatusExtra {

    @SerializedName("error_detail")
    private String errorDetail;

    @SerializedName("unzip_progress")
    private int unzipProgress;
}
