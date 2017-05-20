package net.imatruck.dsmanager.models

import com.google.gson.annotations.SerializedName


class TaskAdditionalFile {

    val filename: String? = null
    val size: Double = 1.toDouble()
    @SerializedName("size_downloaded")
    val sizeDownloaded: Double = 0.toDouble()
    val priority: String? = null
    val index: Int = 0
    val isWanted: Boolean = false

    val downloadedPercent: Float
        get() = Math.round(sizeDownloaded / size * 10000) / 100f
}
