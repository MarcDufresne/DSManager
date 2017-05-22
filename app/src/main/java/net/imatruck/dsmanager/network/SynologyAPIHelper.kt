package net.imatruck.dsmanager.network

import android.content.Context
import android.preference.PreferenceManager

import net.imatruck.dsmanager.R

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object SynologyAPIHelper {

    fun getSynologyApi(context: Context): SynologyAPI {

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        var server: String = prefs.getString(context.getString(R.string.pref_key_server), "")

        if (!server.endsWith('/')) {
            server += "/"
        }

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)

        val retrofit = Retrofit.Builder()
                .baseUrl(server + "webapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create<SynologyAPI>(SynologyAPI::class.java)
    }

}
