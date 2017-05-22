package net.imatruck.dsmanager.models;

import java.util.List;


@SuppressWarnings("unused")
public class DSTaskPauseBase extends BaseModel {

    private List<DSTaskPauseData> data;

    public List<DSTaskPauseData> getData() {
        return data;
    }
}
