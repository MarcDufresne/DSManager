package net.imatruck.dsmanager.tasks

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.widget.TextView

import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.AuthLogoutBase

import java.io.IOException

import retrofit2.Call


class AuthLogoutTask(private val context: Activity) : AsyncTask<Call<AuthLogoutBase>, Void, AuthLogoutBase>() {

    @SafeVarargs
    override fun doInBackground(vararg calls: Call<AuthLogoutBase>): AuthLogoutBase? {

        val loginInfo: AuthLogoutBase
        try {
            loginInfo = calls[0].execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return loginInfo
    }

    @SuppressLint("ApplySharedPref")
    override fun onPostExecute(logoutInfo: AuthLogoutBase?) {
        if (logoutInfo != null) {
            val success = logoutInfo.isSuccess

            if (success) {
                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = prefs.edit()
                editor.putString(context.getString(R.string.pref_key_sid), "")
                editor.putString(context.getString(R.string.pref_key_sid_header), "")
                editor.commit()

                val debug_text_view_sid = context.findViewById(R.id.debug_api_sid) as TextView
                debug_text_view_sid.text = ""
            }

            val debug_text_view = context.findViewById(R.id.debug_api_text) as TextView
            val text = "Logout: " + success.toString()
            debug_text_view.text = text
        }
    }
}
