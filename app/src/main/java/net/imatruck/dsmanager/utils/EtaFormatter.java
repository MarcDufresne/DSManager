package net.imatruck.dsmanager.utils;

import java.util.Locale;

/**
 * Created by marc on 2017-03-26.
 */

public class EtaFormatter {

    public static String calculateEta(double sizeDownloaded, double totalSize, double speed) {
        double sizeRemaining = totalSize - sizeDownloaded;
        int secondsLeft = (int) (sizeRemaining / speed);

        int hours = secondsLeft / 3600;
        int minutes = (secondsLeft % 3600) / 60;
        int seconds = (secondsLeft % 60);

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%dh %02dm %02ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format(Locale.getDefault(), "%dm %02ds", minutes, seconds);
        } else if (seconds > 0){
            return String.format(Locale.getDefault(), "%ds", seconds);
        } else {
            return "N/A";
        }
    }

}
