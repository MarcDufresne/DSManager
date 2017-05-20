package net.imatruck.dsmanager.utils

import java.util.Locale


object EtaFormatter {

    /**
     * Calculates the time remaining and formats it in a human readable format
     * i.e.: 6h 25m 12s | 3d | 55s | 33m | etc.

     * @param sizeDownloaded current progress
     * *
     * @param totalSize      total size
     * *
     * @param speed          quantity done per second
     * *
     * @return time left in a formatted string
     */
    fun calculateEta(sizeDownloaded: Double, totalSize: Double, speed: Double): String {
        val sizeRemaining = totalSize - sizeDownloaded
        val secondsLeft = (sizeRemaining / speed).toInt()

        val hours = secondsLeft / 3600
        val minutes = secondsLeft % 3600 / 60
        val seconds = secondsLeft % 60

        if (hours > 27 * 7 * 4) { // > 4 weeks
            return "∞"
        } else if (hours > 24 * 7) { // > 1 week
            return String.format(Locale.getDefault(), "${hours / 24 * 7}w")
        } else if (hours > 48) { // > 2d
            return String.format(Locale.getDefault(), "${hours / 24}d")
        } else if (hours > 0) { // > 1h
            return String.format(Locale.getDefault(), "${hours}h ${minutes}m ${seconds}s")
        } else if (minutes > 0) { // > 1m
            return String.format(Locale.getDefault(), "${minutes}m ${seconds}s")
        } else if (seconds > 0) { // > 1s
            return String.format(Locale.getDefault(), "${seconds}s")
        } else { // <= 0s
            return "Done"
        }
    }

}
