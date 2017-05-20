package net.imatruck.dsmanager.utils

import java.util.Locale


object BytesFormatter {

    /**
     * Formats a number of bytes in a human readable format
     * i.e: 1KB | 23MB | 1.2GB | 566B

     * @param bytes   number of bytes
     * *
     * @param isSpeed result will be suffixed as "/s" (bytes per second)
     * *
     * @return formatted number of bytes in human readable form: B, KB, MB, GB, TB, PB or EB
     */
    fun humanReadable(bytes: Double, isSpeed: Boolean): String {
        val unit = 1024

        if (bytes < unit)
            return bytes.toString() + " B" + if (isSpeed) "/s" else ""

        val exp = (Math.log(bytes) / Math.log(unit.toDouble())).toInt()

        val pre = "KMGTPE"[exp - 1] + ""

        return String.format(Locale.getDefault(), "%.1f %sB%s",
                bytes / Math.pow(unit.toDouble(), exp.toDouble()), pre, if (isSpeed) "/s" else "")
    }

}
