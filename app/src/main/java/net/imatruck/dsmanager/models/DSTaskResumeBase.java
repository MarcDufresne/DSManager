package net.imatruck.dsmanager.models;

import java.util.List;

/**
 * Created by marc on 2017-03-26.
 */

public class DSTaskResumeBase extends BaseModel {

    private List<DSTaskResumeData> data;

    public List<DSTaskResumeData> getData() {
        return data;
    }
}
