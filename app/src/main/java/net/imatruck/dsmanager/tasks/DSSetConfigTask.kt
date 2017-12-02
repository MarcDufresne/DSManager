package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView
import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSSetConfigBase
import net.imatruck.dsmanager.utils.SynologyBaseError
import retrofit2.Call
import java.io.IOException


class DSSetConfigTask(private val context: Activity) : AsyncTask<Call<DSSetConfigBase>, Void, DSSetConfigBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<DSSetConfigBase>): DSSetConfigBase? {

        val dsInfo: DSSetConfigBase
        try {
            dsInfo = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return dsInfo
    }

    override fun onPostExecute(dsInfo: DSSetConfigBase?) {
        if (dsInfo != null) {
            val success = dsInfo.isSuccess
            val debugTextView = context.findViewById<TextView>(R.id.debug_api_text)

            if (success) {
                val text = "Set Config: Success"
                debugTextView.text = text
            } else {
                val text = context.getString(
                        SynologyBaseError.getMessageId(dsInfo.error.code))
                debugTextView.text = text
            }
        }
    }
}
