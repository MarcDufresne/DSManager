package net.imatruck.dsmanager.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.imatruck.dsmanager.R;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by marc on 2017-03-24.
 */

public class SynologyAPIHelper {

    public static SynologyAPI getSynologyApi(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String server = prefs.getString(context.getString(R.string.pref_key_server), "");

        if (!server.endsWith("/")) {
            server += "/";
        }

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server + "webapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(SynologyAPI.class);
    }

}
