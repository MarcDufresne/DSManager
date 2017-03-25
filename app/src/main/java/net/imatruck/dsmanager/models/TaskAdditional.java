package net.imatruck.dsmanager.models;

import java.util.List;

/**
 * Created by marc on 2017-03-25.
 */

public class TaskAdditional {

    private TaskAdditionalDetail detail;
    private List<TaskAdditionalFile> file;
    private List<TaskAdditionalTracker> tracker;
    private TaskAdditionalTransfer transfer;

    public TaskAdditionalDetail getDetail() {
        return detail;
    }

    public List<TaskAdditionalFile> getFile() {
        return file;
    }

    public List<TaskAdditionalTracker> getTracker() {
        return tracker;
    }

    public TaskAdditionalTransfer getTransfer() {
        return transfer;
    }
}
