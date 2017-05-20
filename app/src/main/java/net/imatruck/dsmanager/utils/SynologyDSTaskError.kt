package net.imatruck.dsmanager.utils

import net.imatruck.dsmanager.R


class SynologyDSTaskError : SynologyBaseError() {
    companion object {

        /**
         * Converts DownloadStation API error codes into resource string ID

         * @param code synology API error code
         * *
         * @return resource string ID of the corresponding error
         */
        fun getMessageId(code: Int): Int = when (code) {
            400 -> R.string.synapi_dstask_error_400
            401 -> R.string.synapi_dstask_error_401
            402 -> R.string.synapi_dstask_error_402
            403 -> R.string.synapi_dstask_error_403
            404 -> R.string.synapi_dstask_error_404
            405 -> R.string.synapi_dstask_error_405
            406 -> R.string.synapi_dstask_error_406
            407 -> R.string.synapi_dstask_error_407
            408 -> R.string.synapi_dstask_error_408
            544 -> R.string.synapi_dstask_error_544
            else -> SynologyBaseError.getMessageId(code)
        }
    }
}
