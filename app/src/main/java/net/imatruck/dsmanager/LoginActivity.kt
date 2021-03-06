package net.imatruck.dsmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.webkit.URLUtil
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import net.imatruck.dsmanager.models.AuthLoginBase
import net.imatruck.dsmanager.network.SynologyAPIHelper
import net.imatruck.dsmanager.utils.SynologyBaseError
import retrofit2.Call
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private var addTaskData: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        addTaskData = intent.getStringExtra(getString(R.string.extra_add_task_data))

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val storedUsername = prefs.getString(getString(R.string.pref_key_account), "")
        val storedApiURL = prefs.getString(getString(R.string.pref_key_server), "")
        val storedSid = prefs.getString(getString(R.string.pref_key_sid), "")

        if (storedSid.isNotEmpty()) {
            finish()
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
            return
        }

        editLoginURL.setText(storedApiURL)
        editLoginUser.setText(storedUsername)

        button.setOnClickListener {

            val url = editLoginURL.text.toString()

            if (!URLUtil.isValidUrl(url)) {
                Toast.makeText(
                        this, getString(R.string.login_invalid_url),
                        Toast.LENGTH_SHORT).show()

            } else {
                val editor = prefs.edit()
                editor.putString(getString(R.string.pref_key_server), url)
                editor.putString(getString(R.string.pref_key_account), editLoginUser.text.toString())
                editor.apply()

                val synologyAPI = SynologyAPIHelper.getSynologyApi(this)

                val authLoginCall = synologyAPI.authLogin(
                        editLoginUser.text.toString(), editLoginPass.text.toString())

                val loginTask = LoginTask()
                loginTask.mLoginActivity = this
                loginTask.execute(authLoginCall)
            }
        }
    }

    class LoginTask : AsyncTask<Call<AuthLoginBase>, Void, AuthLoginBase>() {

        @SuppressLint("StaticFieldLeak")
        var mLoginActivity: LoginActivity? = null

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

        override fun onPostExecute(result: AuthLoginBase?) {
            if (result != null) {
                val success = result.isSuccess
                var errorMessage = ""
                if (result.error != null) {
                    errorMessage = mLoginActivity!!.getString(
                            SynologyBaseError.getMessageId(result.error.code))
                }

                if (success) {
                    val sid = result.sid

                    val prefs = PreferenceManager.getDefaultSharedPreferences(mLoginActivity)
                    val editor = prefs.edit()
                    editor.putString(
                            mLoginActivity!!.getString(R.string.pref_key_sid), sid)
                    editor.putString(
                            mLoginActivity!!.getString(R.string.pref_key_sid_header),
                            "id=" + sid)
                    editor.apply()

                    mLoginActivity!!.finish()
                    val intent: Intent

                    if (mLoginActivity!!.addTaskData != null) {
                        intent = Intent(mLoginActivity!!, AddTaskActivity::class.java)
                        intent.data = Uri.parse(mLoginActivity!!.addTaskData)
                    } else {
                        intent = Intent(mLoginActivity!!, TaskListActivity::class.java)
                    }
                    mLoginActivity!!.startActivity(intent)

                } else {
                    Toast.makeText(mLoginActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
            mLoginActivity = null
        }

    }

}