package net.imatruck.dsmanager.models;

import java.util.List;


@SuppressWarnings("unused")
public class DSTaskDeleteBase extends BaseModel {

    private List<DSTaskDeleteData> data;

    public List<DSTaskDeleteData> getData() {
        return data;
    }
}
