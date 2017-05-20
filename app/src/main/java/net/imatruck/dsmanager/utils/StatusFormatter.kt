package net.imatruck.dsmanager.utils

import net.imatruck.dsmanager.R


object StatusFormatter {

    /**
     * Converts DownloadStation API status to resource string ID

     * @param status synology API status string
     * *
     * @return resource string ID for corresponding status
     */
    fun getStatusString(status: String?): Int = when (status) {
        "waiting" -> R.string.synapi_task_status_waiting
        "downloading" -> R.string.synapi_task_status_downloading
        "paused" -> R.string.synapi_task_status_paused
        "finishing" -> R.string.synapi_task_status_finishing
        "finished" -> R.string.synapi_task_status_finished
        "hash_checking" -> R.string.synapi_task_status_hash_checking
        "seeding" -> R.string.synapi_task_status_seeding
        "filehosting_waiting" -> R.string.synapi_task_status_filehosting_waiting
        "extracting" -> R.string.synapi_task_status_extracting
        "error" -> R.string.synapi_task_status_error
        else -> -1
    }

    /**
     * Converts DownloadStation API status to a resource color ID

     * @param status synology API status string
     * *
     * @return resource color ID
     */
    fun getStatusColor(status: String?): Int = when (status) {
        "waiting" -> R.color.colorStatusWaiting
        "downloading" -> R.color.colorAccent
        "paused" -> R.color.colorStatusPaused
        "finishing" -> R.color.colorStatusDone
        "finished" -> R.color.colorStatusDone
        "hash_checking" -> R.color.colorStatusWaiting
        "seeding" -> R.color.colorStatusSeeding
        "filehosting_waiting" -> R.color.colorStatusWaiting
        "extracting" -> R.color.colorStatusWaiting
        "error" -> R.color.colorStatusError
        else -> R.color.colorAccent
    }

}
