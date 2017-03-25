package net.imatruck.dsmanager.network;

import android.content.Context;
import android.content.SharedPreferences;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.TaskList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by marc on 2017-03-24.
 */

public class SynologyAPIHelper {

    public SynologyAPI getSynologyApi(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.pref_key_shared_file), Context.MODE_PRIVATE);
        String server = prefs.getString(context.getString(R.string.pref_key_server), "");

        if (!server.endsWith("/")) {
            server += "/";
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server + "webapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(SynologyAPI.class);
    }

}
