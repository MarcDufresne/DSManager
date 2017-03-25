package net.imatruck.dsmanager.utils;

import java.util.Locale;

/**
 * Created by marc on 2017-03-25.
 */

public class BytesFormater {

    public static String humanReadable(double bytes) {
        int unit = 1024;

        if (bytes < unit)
            return bytes + " B";

        int exp = (int) (Math.log(bytes) / Math.log(unit));

        String pre = "KMGTPE".charAt(exp-1) + "";

        return String.format(Locale.getDefault(), "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
