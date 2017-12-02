package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView
import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSTaskDeleteBase
import net.imatruck.dsmanager.utils.SynologyDSTaskError
import retrofit2.Call
import java.io.IOException
import java.util.*


class DSTaskDeleteTask(private val context: Activity) : AsyncTask<Call<DSTaskDeleteBase>, Void, DSTaskDeleteBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<DSTaskDeleteBase>): DSTaskDeleteBase? {

        val dsTaskDeleteBase: DSTaskDeleteBase
        try {
            dsTaskDeleteBase = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return dsTaskDeleteBase
    }

    override fun onPostExecute(dsInfo: DSTaskDeleteBase?) {
        if (dsInfo != null) {
            val success = dsInfo.isSuccess
            val debugTextView = context.findViewById<TextView>(R.id.debug_api_text)

            if (success) {
                val infoData = dsInfo.data
                var text = ""

                for (dsTaskDeleteData in infoData) {
                    text += String.format(Locale.getDefault(),
                            "%s: %s\n",
                            dsTaskDeleteData.id,
                            context.getString(
                                    SynologyDSTaskError.getMessageId(dsTaskDeleteData.error))
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
