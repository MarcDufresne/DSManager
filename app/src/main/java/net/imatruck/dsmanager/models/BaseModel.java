package net.imatruck.dsmanager.models;


@SuppressWarnings("unused")
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
