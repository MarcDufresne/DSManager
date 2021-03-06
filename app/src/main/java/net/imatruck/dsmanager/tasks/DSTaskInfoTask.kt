package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView
import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSTaskInfoBase
import net.imatruck.dsmanager.utils.BytesFormatter
import net.imatruck.dsmanager.utils.PercentFormatter
import net.imatruck.dsmanager.utils.SynologyDSTaskError
import net.imatruck.dsmanager.utils.TimestampFormatter
import retrofit2.Call
import java.io.IOException
import java.util.*


class DSTaskInfoTask(private val context: Activity) : AsyncTask<Call<DSTaskInfoBase>, Void, DSTaskInfoBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<DSTaskInfoBase>): DSTaskInfoBase? {

        val dsTaskListBase: DSTaskInfoBase
        try {
            dsTaskListBase = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return dsTaskListBase
    }

    override fun onPostExecute(dsInfo: DSTaskInfoBase?) {
        if (dsInfo != null) {
            val success = dsInfo.isSuccess
            val debugTextView = context.findViewById<TextView>(R.id.debug_api_text)

            if (success) {
                val infoData = dsInfo.data
                var text = ""

                for (task in infoData.tasks) {
                    text += String.format(Locale.getDefault(),
                            "%s: %s\n%.2f%%, %s\n%s\n%s\n%s\n\n",
                            task.id,
                            task.title,
                            PercentFormatter.toPercent(
                                    task.additional.transfer.sizeDownloaded,
                                    task.size),
                            BytesFormatter.humanReadable(task.size, false),
                            task.additional.detail.destination,
                            TimestampFormatter.timestampToDatetime(
                                    task.additional.detail.completedTime),
                            TimestampFormatter.timestampToDatetime(
                                    task.additional.detail.createTime * 1000
                            ))
                }
                debugTextView.text = text
            } else {
                val text = context.getString(
                        SynologyDSTaskError.getMessageId(dsInfo.error.code))
                debugTextView.text = text
            }
        }
    }
}
