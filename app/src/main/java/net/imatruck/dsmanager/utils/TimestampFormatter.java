package net.imatruck.dsmanager.utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by marc on 2017-03-25.
 */

public class TimestampFormatter {

    public static String timestampToDatetime(long timestamp) {
        if (timestamp <= 0) return "N/A";
        Date date = new Date(timestamp);
        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d HH:mm", Locale.getDefault());
        return prettyTime.format(date) + " (" + formatter.format(timestamp) + ")";
    }
}
