package net.imatruck.dsmanager.models;

import java.util.List;

/**
 * Created by marc on 2017-03-26.
 */

public class DSTaskPauseBase extends BaseModel {

    private List<DSTaskPauseData> data;

    public List<DSTaskPauseData> getData() {
        return data;
    }
}
