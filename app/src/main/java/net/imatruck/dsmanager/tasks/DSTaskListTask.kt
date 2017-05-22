package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView

import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSTaskListBase
import net.imatruck.dsmanager.utils.BytesFormatter
import net.imatruck.dsmanager.utils.EtaFormatter
import net.imatruck.dsmanager.utils.PercentFormatter
import net.imatruck.dsmanager.utils.SynologyDSTaskError

import java.io.IOException
import java.util.Locale

import retrofit2.Call


class DSTaskListTask(private val context: Activity) : AsyncTask<Call<DSTaskListBase>, Void, DSTaskListBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<DSTaskListBase>): DSTaskListBase? {

        val dsTaskListBase: DSTaskListBase
        try {
            dsTaskListBase = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return dsTaskListBase
    }

    override fun onPostExecute(dsInfo: DSTaskListBase?) {
        if (dsInfo != null) {
            val success = dsInfo.isSuccess
            val debug_text_view = context.findViewById(R.id.debug_api_text) as TextView

            if (success) {
                val infoData = dsInfo.data
                var text = ""

                for (task in infoData.tasks) {
                    text += String.format(Locale.getDefault(),
                            "%s: %s\n%.2f%%, %s - Down: %s - Up: %s - ETA: %s\n\n",
                            task.id,
                            task.title,
                            PercentFormatter.toPercent(
                                    task.additional.transfer.sizeDownloaded,
                                    task.size),
                            BytesFormatter.humanReadable(task.size, false),
                            BytesFormatter.humanReadable(
                                    task.additional.transfer.speedDownload, true),
                            BytesFormatter.humanReadable(
                                    task.additional.transfer.speedUpload, true),
                            EtaFormatter.calculateEta(
                                    task.additional.transfer.sizeDownloaded,
                                    task.size,
                                    task.additional.transfer.speedDownload)
                    )
                }
                debug_text_view.text = text
            } else {
                val text = context.getString(
                        SynologyDSTaskError.getMessageId(dsInfo.error.code))
                debug_text_view.text = text
            }
        }
    }
}
