package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView

import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSSetConfigBase
import net.imatruck.dsmanager.utils.SynologyBaseError

import java.io.IOException

import retrofit2.Call


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
            val debug_text_view = context.findViewById(R.id.debug_api_text) as TextView

            if (success) {
                val text = "Set Config: Success"
                debug_text_view.text = text
            } else {
                val text = context.getString(
                        SynologyBaseError.getMessageId(dsInfo.error!!.code))
                debug_text_view.text = text
            }
        }
    }
}
