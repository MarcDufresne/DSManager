package net.imatruck.dsmanager.models

import com.google.gson.annotations.SerializedName


class DSGetInfoData {
    @SerializedName("is_manager")
    val isManager: Boolean = false
    val version: Int = 0
    @SerializedName("version_string")
    val versionString: String? = null
}
