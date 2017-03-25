package net.imatruck.dsmanager.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marc on 2017-03-25.
 */

public class Task {
    private TaskAdditional additional;
    private String id;
    private double size;
    private String title;
    private String type;
    private String username;
    private String status;

    @SerializedName("status_extra")
    private TaskStatusExtra statusExtra;

    public TaskAdditional getAdditional() {
        return additional;
    }

    public String getId() {
        return id;
    }

    public double getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public TaskStatusExtra getStatusExtra() {
        return statusExtra;
    }
}
