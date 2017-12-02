package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.widget.TextView
import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.APIInfoBase
import retrofit2.Call
import java.io.IOException


class ApiInfoTask(private val context: Activity) : AsyncTask<Call<APIInfoBase>, Void, APIInfoBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<APIInfoBase>): APIInfoBase? {

        val apiInfo: APIInfoBase
        try {
            apiInfo = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return apiInfo
    }

    override fun onPostExecute(apiInfo: APIInfoBase?) {
        if (apiInfo != null) {
            var result = ""
            for ((key, value) in apiInfo.data) {
                result += key + ": " + value.path + "\n"
            }
            val debugTextView = context.findViewById<TextView>(R.id.debug_api_text)
            debugTextView.text = result
        }
    }
}
