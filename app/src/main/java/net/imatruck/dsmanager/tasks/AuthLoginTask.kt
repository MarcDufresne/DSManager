package net.imatruck.dsmanager.tasks

import android.app.Activity
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.widget.TextView

import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.AuthLoginBase
import net.imatruck.dsmanager.utils.SynologyBaseError

import java.io.IOException

import retrofit2.Call


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

            val debug_text_view = context.findViewById(R.id.debug_api_text) as TextView

            if (success) {
                val sid = loginInfo.sid

                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = prefs.edit()
                editor.putString(context.getString(R.string.pref_key_sid), sid)
                editor.putString(context.getString(R.string.pref_key_sid_header), "id=" + sid!!)
                editor.apply()

                val debug_text_view_sid = context.findViewById(R.id.debug_api_sid) as TextView
                debug_text_view_sid.text = sid
                val text = "Login: Success"
                debug_text_view.text = text
            } else {
                debug_text_view.text = error_message
            }
        }
    }
}
