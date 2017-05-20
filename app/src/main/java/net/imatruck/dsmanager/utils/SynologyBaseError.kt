package net.imatruck.dsmanager.utils

import net.imatruck.dsmanager.R


open class SynologyBaseError {
    companion object {

        /**
         * Converts Synology API error codes into resource string ID

         * @param code synology API error code
         * *
         * @return resource string ID of the corresponding error
         */
        fun getMessageId(code: Int): Int = when (code) {
            0 -> R.string.synapi_error_0
            100 -> R.string.synapi_error_100
            101 -> R.string.synapi_error_101
            102 -> R.string.synapi_error_102
            103 -> R.string.synapi_error_103
            104 -> R.string.synapi_error_104
            105 -> R.string.synapi_error_105
            106 -> R.string.synapi_error_106
            107 -> R.string.synapi_error_107
            else -> R.string.synapi_error_100
        }
    }

}
