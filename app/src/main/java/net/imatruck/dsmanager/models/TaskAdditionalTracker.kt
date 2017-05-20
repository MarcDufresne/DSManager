package net.imatruck.dsmanager.models

import com.google.gson.annotations.SerializedName


class TaskAdditionalTracker {
    val url: String? = null
    val status: String? = null
    @SerializedName("update_timer")
    val updateTimer: Int = 0
    val seeds: Int = 0
    val peers: Int = 0
}
