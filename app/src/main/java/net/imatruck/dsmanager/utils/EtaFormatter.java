package net.imatruck.dsmanager.utils;

import java.util.Locale;


public class EtaFormatter {

    /**
     * Calculates the time remaining and formats it in a human readable format
     * i.e.: 6h 25m 12s | 3d | 55s | 33m | etc.
     *
     * @param sizeDownloaded current progress
     * @param totalSize total size
     * @param speed quantity done per second
     * @return time left in a formatted string
     */
    public static String calculateEta(double sizeDownloaded, double totalSize, double speed) {
        double sizeRemaining = totalSize - sizeDownloaded;
        int secondsLeft = (int) (sizeRemaining / speed);

        int hours = secondsLeft / 3600;
        int minutes = (secondsLeft % 3600) / 60;
        int seconds = (secondsLeft % 60);

        if (hours > (27 * 7 * 4)) { // > 4 weeks
            return "∞";
        } if (hours > (24 * 7)) { // > 1 week
            return String.format(Locale.getDefault(), "%dw", hours / (27 * 7));
        } else if (hours > 48) { // > 2d
            return String.format(Locale.getDefault(), "%dd", hours / 24);
        } else if (hours > 0) { // > 1h
            return String.format(Locale.getDefault(), "%dh %02dm %02ds", hours, minutes, seconds);
        } else if (minutes > 0) { // > 1m
            return String.format(Locale.getDefault(), "%dm %02ds", minutes, seconds);
        } else if (seconds > 0){ // > 1s
            return String.format(Locale.getDefault(), "%ds", seconds);
        } else { // <= 0s
            return "Done";
        }
    }

}
