package net.imatruck.dsmanager.utils;

/**
 * Created by marc on 2017-03-25.
 */

public class PercentFormater {

    public static double toPercent(double a, double b) {
        return Math.round((a / b) * 10000) / 100f;
    }
}
