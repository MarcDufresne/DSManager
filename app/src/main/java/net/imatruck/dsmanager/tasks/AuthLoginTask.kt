package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.widget.TextView
import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.AuthLoginBase
import net.imatruck.dsmanager.utils.SynologyBaseError
import retrofit2.Call
import java.io.IOException


class AuthLoginTask(private val context: Activity) : AsyncTask<Call<AuthLoginBase>, Void, AuthLoginBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<AuthLoginBase>): AuthLoginBase? {

        val loginInfo: AuthLoginBase
        try {
            loginInfo = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return loginInfo
    }

    override fun onPostExecute(loginInfo: AuthLoginBase?) {
        if (loginInfo != null) {
            val success = loginInfo.isSuccess
            var error_message = ""
            if (loginInfo.error != null) {
                error_message = context.getString(
                        SynologyBaseError.getMessageId(loginInfo.error.code))
            }

            val debugTextView = context.findViewById<TextView>(R.id.debug_api_text)

            if (success) {
                val sid = loginInfo.sid

                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = prefs.edit()
                editor.putString(context.getString(R.string.pref_key_sid), sid)
                editor.putString(context.getString(R.string.pref_key_sid_header), "id=" + sid)
                editor.apply()

                val debugTextViewSID = context.findViewById<TextView>(R.id.debug_api_sid)
                debugTextViewSID.text = sid
                val text = "Login: Success"
                debugTextView.text = text
            } else {
                debugTextView.text = error_message
            }
        }
    }
}
