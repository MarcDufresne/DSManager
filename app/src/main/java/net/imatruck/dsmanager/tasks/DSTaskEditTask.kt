package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView

import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.DSTaskEditBase

import java.io.IOException

import retrofit2.Call


class DSTaskEditTask(private val context: Activity) : AsyncTask<Call<DSTaskEditBase>, Void, DSTaskEditBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<DSTaskEditBase>): DSTaskEditBase? {

        val loginInfo: DSTaskEditBase
        try {
            loginInfo = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return loginInfo
    }

    override fun onPostExecute(result: DSTaskEditBase?) {
        if (result != null) {
            val success = result.isSuccess

            val debugTextView = context.findViewById(R.id.debug_api_text) as TextView
            val text = "Task Edit: " + success.toString()
            debugTextView.text = text
        }
    }
}
