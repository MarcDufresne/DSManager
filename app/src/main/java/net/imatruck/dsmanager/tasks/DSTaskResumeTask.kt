package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView
import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSTaskResumeBase
import net.imatruck.dsmanager.utils.SynologyDSTaskError
import retrofit2.Call
import java.io.IOException
import java.util.*


class DSTaskResumeTask(private val context: Activity) : AsyncTask<Call<DSTaskResumeBase>, Void, DSTaskResumeBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<DSTaskResumeBase>): DSTaskResumeBase? {

        val dsTaskResumeBase: DSTaskResumeBase
        try {
            dsTaskResumeBase = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return dsTaskResumeBase
    }

    override fun onPostExecute(dsInfo: DSTaskResumeBase?) {
        if (dsInfo != null) {
            val success = dsInfo.isSuccess
            val debugTextView = context.findViewById<TextView>(R.id.debug_api_text)

            if (success) {
                val data = dsInfo.data
                var text = ""

                for (dsTaskResumeData in data) {
                    text += String.format(Locale.getDefault(),
                            "%s: %s\n",
                            dsTaskResumeData.id,
                            context.getString(
                                    SynologyDSTaskError.getMessageId(dsTaskResumeData.error))
                    )
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
