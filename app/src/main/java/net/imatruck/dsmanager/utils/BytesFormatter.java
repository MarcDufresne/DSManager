package net.imatruck.dsmanager.utils;

import java.util.Locale;


public class BytesFormatter {

    /**
     * Formats a number of bytes in a human readable format
     * i.e: 1KB | 23MB | 1.2GB | 566B
     *
     * @param bytes number of bytes
     * @param isSpeed result will be suffixed as "/s" (bytes per second)
     * @return formatted number of bytes in human readable form: B, KB, MB, GB, TB, PB or EB
     */
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
