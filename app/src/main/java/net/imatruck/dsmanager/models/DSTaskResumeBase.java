package net.imatruck.dsmanager.models;

import java.util.List;


@SuppressWarnings("unused")
public class DSTaskResumeBase extends BaseModel {

    private List<DSTaskResumeData> data;

    public List<DSTaskResumeData> getData() {
        return data;
    }
}
