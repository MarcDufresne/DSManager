package net.imatruck.dsmanager.utils

import org.ocpsoft.prettytime.PrettyTime

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object TimestampFormatter {

    /**
     * Converts a timestamp into a human readable relative description
     * i.e.: 2 days ago | 3 hours ago

     * @param timestamp unix timestamp
     * *
     * @return relative time description (1 day ago) with formatted date (Apr 21 3:45)
     */
    fun timestampToDatetime(timestamp: Long): String {
        if (timestamp <= 0) return "N/A"
        val date = Date(timestamp)
        val prettyTime = PrettyTime(Locale.getDefault())
        val formatter = SimpleDateFormat("MMM d HH:mm", Locale.getDefault())
        return prettyTime.format(date) + " (" + formatter.format(timestamp) + ")"
    }
}
