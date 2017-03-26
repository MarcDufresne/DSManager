package net.imatruck.dsmanager.models;

import java.util.List;

/**
 * Created by marc on 2017-03-26.
 */

public class DSTaskDeleteBase extends BaseModel {

    private List<DSTaskDeleteData> data;

    public List<DSTaskDeleteData> getData() {
        return data;
    }
}
