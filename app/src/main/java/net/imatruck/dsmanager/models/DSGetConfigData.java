package net.imatruck.dsmanager.models;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class DSGetConfigData {
    @SerializedName("bt_max_download")
    private int btMaxDownload;

    @SerializedName("bt_max_upload")
    private int btMaxUpload;

    @SerializedName("default_destination")
    private String defaultDestination;

    @SerializedName("emule_default_destination")
    private String emuleDefaultDestination;

    @SerializedName("emule_enabled")
    private boolean emuleEnabled;

    @SerializedName("emule_max_download")
    private int emuleMaxDownload;

    @SerializedName("emule_max_upload")
    private int emuleMaxUpload;

    @SerializedName("ftp_max_download")
    private int ftpMaxDownload;

    @SerializedName("http_max_download")
    private int httpMaxDownload;

    @SerializedName("nzb_max_download")
    private int nzbMaxDownload;

    @SerializedName("unzip_service_enabled")
    private boolean unzipServiceEnabled;

    @SerializedName("xunlei_enabled")
    private boolean xunleiEnabled;

    public int getBtMaxDownload() {
        return btMaxDownload;
    }

    public int getBtMaxUpload() {
        return btMaxUpload;
    }

    public String getDefaultDestination() {
        return defaultDestination;
    }

    public String getEmuleDefaultDestination() {
        return emuleDefaultDestination;
    }

    public boolean isEmuleEnabled() {
        return emuleEnabled;
    }

    public int getEmuleMaxDownload() {
        return emuleMaxDownload;
    }

    public int getEmuleMaxUpload() {
        return emuleMaxUpload;
    }

    public int getFtpMaxDownload() {
        return ftpMaxDownload;
    }

    public int getHttpMaxDownload() {
        return httpMaxDownload;
    }

    public int getNzbMaxDownload() {
        return nzbMaxDownload;
    }

    public boolean isUnzipServiceEnabled() {
        return unzipServiceEnabled;
    }

    public boolean isXunleiEnabled() {
        return xunleiEnabled;
    }
}
