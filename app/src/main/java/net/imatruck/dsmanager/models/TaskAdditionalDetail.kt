package net.imatruck.dsmanager.models

import com.google.gson.annotations.SerializedName


class TaskAdditionalDetail {

    @SerializedName("completed_time")
    val completedTime: Long = 0

    @SerializedName("connected_leechers")
    val connectedLeechers: Int = 0

    @SerializedName("connected_peers")
    val connectedPeers: Int = 0

    @SerializedName("connected_seeders")
    val connectedSeeders: Int = 0

    @SerializedName("create_time")
    val createTime: Long = 0

    val destination: String? = null

    @SerializedName("seedelapsed")
    val seedElapsed: Int = 0

    @SerializedName("started_time")
    val startedTime: Double = 0.toDouble()

    @SerializedName("total_peers")
    val totalPeers: Int = 0

    @SerializedName("total_pieces")
    val totalPieces: Int = 0

    @SerializedName("unzip_password")
    val unzipPassword: String? = null

    val uri: String? = null

    @SerializedName("waiting_seconds")
    val waitingSeconds: Int = 0
}
