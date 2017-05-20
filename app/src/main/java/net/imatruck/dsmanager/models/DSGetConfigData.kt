package net.imatruck.dsmanager.models

import com.google.gson.annotations.SerializedName


class DSGetConfigData {
    @SerializedName("bt_max_download")
    val btMaxDownload: Int = 0

    @SerializedName("bt_max_upload")
    val btMaxUpload: Int = 0

    @SerializedName("default_destination")
    val defaultDestination: String? = null

    @SerializedName("emule_default_destination")
    val emuleDefaultDestination: String? = null

    @SerializedName("emule_enabled")
    val isEmuleEnabled: Boolean = false

    @SerializedName("emule_max_download")
    val emuleMaxDownload: Int = 0

    @SerializedName("emule_max_upload")
    val emuleMaxUpload: Int = 0

    @SerializedName("ftp_max_download")
    val ftpMaxDownload: Int = 0

    @SerializedName("http_max_download")
    val httpMaxDownload: Int = 0

    @SerializedName("nzb_max_download")
    val nzbMaxDownload: Int = 0

    @SerializedName("unzip_service_enabled")
    val isUnzipServiceEnabled: Boolean = false

    @SerializedName("xunlei_enabled")
    val isXunleiEnabled: Boolean = false
}
