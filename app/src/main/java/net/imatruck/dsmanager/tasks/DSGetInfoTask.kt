package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView

import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSGetInfoBase
import net.imatruck.dsmanager.utils.SynologyBaseError

import java.io.IOException

import retrofit2.Call


class DSGetInfoTask(private val context: Activity) : AsyncTask<Call<DSGetInfoBase>, Void, DSGetInfoBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<DSGetInfoBase>): DSGetInfoBase? {

        val dsInfo: DSGetInfoBase
        try {
            dsInfo = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return dsInfo
    }

    override fun onPostExecute(dsInfo: DSGetInfoBase?) {
        if (dsInfo != null) {
            val success = dsInfo.isSuccess
            val debug_text_view = context.findViewById(R.id.debug_api_text) as TextView

            if (success) {
                val infoData = dsInfo.data
                var text = "Version: " + infoData!!.versionString!!
                text += "\nVersion ID: " + infoData.version
                debug_text_view.text = text
            } else {
                val text = context.getString(
                        SynologyBaseError.getMessageId(dsInfo.error!!.code))
                debug_text_view.text = text
            }
        }
    }
}
