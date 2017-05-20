package net.imatruck.dsmanager.models

import com.google.gson.annotations.SerializedName


class TaskStatusExtra {

    @SerializedName("error_detail")
    private val errorDetail: String? = null

    @SerializedName("unzip_progress")
    private val unzipProgress: Int = 0
}
