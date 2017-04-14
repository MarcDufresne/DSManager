package net.imatruck.dsmanager.models;


@SuppressWarnings("unused")
public class APIInfoData {
    private int maxVersion;
    private int minVersion;
    private String path;

    public int getMaxVersion() {
        return maxVersion;
    }

    public int getMinVersion() {
        return minVersion;
    }

    public String getPath() {
        return path;
    }
}
