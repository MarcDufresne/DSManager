package net.imatruck.dsmanager.models

import com.google.gson.annotations.SerializedName


class DSStatsInfoData {
    @SerializedName("speed_download")
    val speedDownload: Double = 0.toDouble()
    @SerializedName("speed_upload")
    val speedUpload: Double = 0.toDouble()
}
