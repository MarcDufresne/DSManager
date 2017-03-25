package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.AuthLoginBase;
import net.imatruck.dsmanager.models.AuthLogoutBase;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by marc on 2017-03-24.
 */

public class AuthLogoutTask extends AsyncTask<Call<AuthLogoutBase>, Void, AuthLogoutBase> {

    private Activity context;

    public AuthLogoutTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final AuthLogoutBase doInBackground(Call<AuthLogoutBase>... calls) {

        AuthLogoutBase loginInfo;
        try {
            loginInfo = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return loginInfo;
    }

    @Override
    protected void onPostExecute(AuthLogoutBase logoutInfo) {
        if (logoutInfo != null) {
            boolean success = logoutInfo.isSuccess();

            if (success) {
                SharedPreferences prefs = context.getSharedPreferences(
                        context.getString(R.string.pref_key_shared_file), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(context.getString(R.string.pref_key_sid), "");
                editor.apply();

                TextView debug_text_view_sid = (TextView) context.findViewById(R.id.debug_api_sid);
                debug_text_view_sid.setText("");
            }

            TextView debug_text_view = (TextView) context.findViewById(R.id.debug_api_text);
            String text = "Logout: " + String.valueOf(success);
            debug_text_view.setText(text);
        }
    }
}
