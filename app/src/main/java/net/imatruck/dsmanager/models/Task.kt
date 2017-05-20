package net.imatruck.dsmanager.models

import com.google.gson.annotations.SerializedName


class Task {
    val additional: TaskAdditional? = null
    val id: String? = null
    val size: Double = 0.toDouble()
    val title: String? = null
    val type: String? = null
    val username: String? = null
    val status: String? = null

    @SerializedName("status_extra")
    val statusExtra: TaskStatusExtra? = null
}
