package net.imatruck.dsmanager.models;

/**
 * Created by marc on 2017-03-25.
 */

public abstract class BaseModel {
    private GenericError error;
    private boolean success;

    public GenericError getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }
}
