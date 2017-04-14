package net.imatruck.dsmanager.utils;


public class PercentFormatter {

    /**
     * Returns the percentage of {@code a} over {@code b}
     * @param a current value
     * @param b total value
     * @return percentage
     */
    public static double toPercent(double a, double b) {
        return Math.round((a / b) * 10000) / 100f;
    }
}
