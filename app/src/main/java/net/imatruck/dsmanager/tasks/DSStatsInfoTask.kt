package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView

import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSStatsInfoBase
import net.imatruck.dsmanager.utils.BytesFormatter
import net.imatruck.dsmanager.utils.SynologyBaseError

import java.io.IOException
import java.util.Locale

import retrofit2.Call


class DSStatsInfoTask(private val context: Activity) : AsyncTask<Call<DSStatsInfoBase>, Void, DSStatsInfoBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<DSStatsInfoBase>): DSStatsInfoBase? {

        val dsStatsInfoBase: DSStatsInfoBase
        try {
            dsStatsInfoBase = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return dsStatsInfoBase
    }

    override fun onPostExecute(dsStatsInfoBase: DSStatsInfoBase?) {
        if (dsStatsInfoBase != null) {
            val debugTextView = context.findViewById(R.id.debug_api_text) as TextView

            if (dsStatsInfoBase.isSuccess) {
                val text = String.format(Locale.getDefault(), "DL: %s\nUL: %s",
                        BytesFormatter.humanReadable(dsStatsInfoBase.data!!.speedDownload, true),
                        BytesFormatter.humanReadable(dsStatsInfoBase.data.speedUpload, true))
                debugTextView.text = text
            } else {
                val text = String.format(Locale.getDefault(), "Error: %s\n",
                        SynologyBaseError.getMessageId(dsStatsInfoBase.error!!.code))
                debugTextView.text = text
            }
        }
    }
}
