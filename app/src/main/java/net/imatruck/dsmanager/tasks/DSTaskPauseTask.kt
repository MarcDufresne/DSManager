package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView

import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSTaskPauseBase
import net.imatruck.dsmanager.utils.SynologyDSTaskError

import java.io.IOException
import java.util.Locale

import retrofit2.Call


class DSTaskPauseTask(private val context: Activity) : AsyncTask<Call<DSTaskPauseBase>, Void, DSTaskPauseBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<DSTaskPauseBase>): DSTaskPauseBase? {

        val dsTaskPauseBase: DSTaskPauseBase
        try {
            dsTaskPauseBase = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return dsTaskPauseBase
    }

    override fun onPostExecute(dsInfo: DSTaskPauseBase?) {
        if (dsInfo != null) {
            val success = dsInfo.isSuccess
            val debugTextView = context.findViewById(R.id.debug_api_text) as TextView

            if (success) {
                val data = dsInfo.data
                var text = ""

                for (dsTaskPauseData in data!!) {
                    text += String.format(Locale.getDefault(),
                            "%s: %s\n",
                            dsTaskPauseData.id,
                            context.getString(
                                    SynologyDSTaskError.getMessageId(dsTaskPauseData.error))
                    )
                }
                debugTextView.text = text
            } else {
                val text = context.getString(
                        SynologyDSTaskError.getMessageId(dsInfo.error!!.code))
                debugTextView.text = text
            }
        }
    }
}
