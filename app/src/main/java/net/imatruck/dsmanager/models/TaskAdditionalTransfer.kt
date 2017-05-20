package net.imatruck.dsmanager.models

import com.google.gson.annotations.SerializedName


class TaskAdditionalTransfer {

    @SerializedName("downloaded_pieces")
    val downloadedPieces: Int = 0

    @SerializedName("size_downloaded")
    val sizeDownloaded: Double = 0.toDouble()

    @SerializedName("size_uploaded")
    val sizeUploaded: Double = 0.toDouble()

    @SerializedName("speed_download")
    val speedDownload: Double = 0.toDouble()

    @SerializedName("speed_upload")
    val speedUpload: Double = 0.toDouble()
}
