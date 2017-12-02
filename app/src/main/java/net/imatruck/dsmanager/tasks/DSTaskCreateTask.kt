package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView
import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSTaskCreateBase
import net.imatruck.dsmanager.utils.SynologyDSTaskError
import retrofit2.Call
import java.io.IOException


class DSTaskCreateTask(private val context: Activity) : AsyncTask<Call<DSTaskCreateBase>, Void, DSTaskCreateBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<DSTaskCreateBase>): DSTaskCreateBase? {

        val dsTaskListBase: DSTaskCreateBase
        try {
            dsTaskListBase = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return dsTaskListBase
    }

    override fun onPostExecute(dsInfo: DSTaskCreateBase?) {
        if (dsInfo != null) {
            val success = dsInfo.isSuccess
            val debugTextView = context.findViewById<TextView>(R.id.debug_api_text)

            if (success) {
                val text = "Task Create: Success"
                debugTextView.text = text
            } else {
                val text = context.getString(
                        SynologyDSTaskError.getMessageId(dsInfo.error.code))
                debugTextView.text = text
            }
        }
    }
}
