package net.imatruck.dsmanager.models;

import java.util.List;

/**
 * Created by marc on 2017-03-25.
 */

public class DSTaskListData {
    private List<Task> tasks;
    private int total;

    public List<Task> getTasks() {
        return tasks;
    }

    public int getTotal() {
        return total;
    }
}
