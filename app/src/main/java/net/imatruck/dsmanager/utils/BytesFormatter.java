package net.imatruck.dsmanager.utils;

import java.util.Locale;

/**
 * Created by marc on 2017-03-25.
 */

public class BytesFormatter {

    public static String humanReadable(double bytes, boolean isSpeed) {
        int unit = 1024;

        if (bytes < unit)
            return bytes + " B" + (isSpeed ? "/s" : "");

        int exp = (int) (Math.log(bytes) / Math.log(unit));

        String pre = "KMGTPE".charAt(exp-1) + "";

        return String.format(Locale.getDefault(), "%.1f %sB%s",
                bytes / Math.pow(unit, exp), pre, isSpeed ? "/s" : "");
    }

}
