package net.imatruck.dsmanager.models;

import java.util.List;


@SuppressWarnings("unused")
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
