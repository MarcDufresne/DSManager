package net.imatruck.dsmanager.utils


object PercentFormatter {

    /**
     * Returns the percentage of `a` over `b`

     * @param a current value
     * *
     * @param b total value
     * *
     * @return percentage
     */
    fun toPercent(a: Double, b: Double): Double {
        return (Math.round(a / b * 10000) / 100f).toDouble()
    }
}
