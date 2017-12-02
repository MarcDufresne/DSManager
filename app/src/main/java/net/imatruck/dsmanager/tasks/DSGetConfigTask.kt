package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView
import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSGetConfigBase
import net.imatruck.dsmanager.utils.SynologyBaseError
import retrofit2.Call
import java.io.IOException


class DSGetConfigTask(private val context: Activity) : AsyncTask<Call<DSGetConfigBase>, Void, DSGetConfigBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<DSGetConfigBase>): DSGetConfigBase? {

        val dsInfo: DSGetConfigBase
        try {
            dsInfo = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return dsInfo
    }

    override fun onPostExecute(dsInfo: DSGetConfigBase?) {
        if (dsInfo != null) {
            val success = dsInfo.isSuccess
            val debugTextView = context.findViewById<TextView>(R.id.debug_api_text)
            if (success) {
                val infoData = dsInfo.data
                var text = "BT Max DL: " + infoData.btMaxDownload
                text += "\nBT Max UL: " + infoData.btMaxUpload
                text += "\nDefault Dest: " + infoData.defaultDestination
                debugTextView.text = text
            } else {
                val text = context.getString(
                        SynologyBaseError.getMessageId(dsInfo.error.code))
                debugTextView.text = text
            }
        }
    }
}
