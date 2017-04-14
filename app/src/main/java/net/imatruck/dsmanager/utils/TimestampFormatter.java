package net.imatruck.dsmanager.utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimestampFormatter {

    /**
     * Converts a timestamp into a human readable relative description
     * i.e.: 2 days ago | 3 hours ago
     *
     * @param timestamp unix timestamp
     * @return relative time description (1 day ago) with formatted date (Apr 21 3:45)
     */
    public static String timestampToDatetime(long timestamp) {
        if (timestamp <= 0) return "N/A";
        Date date = new Date(timestamp);
        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d HH:mm", Locale.getDefault());
        return prettyTime.format(date) + " (" + formatter.format(timestamp) + ")";
    }
}
