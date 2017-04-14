package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.AuthLoginBase;
import net.imatruck.dsmanager.utils.SynologyBaseError;

import java.io.IOException;

import retrofit2.Call;


public class AuthLoginTask extends AsyncTask<Call<AuthLoginBase>, Void, AuthLoginBase> {

    private Activity context;

    public AuthLoginTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final AuthLoginBase doInBackground(Call<AuthLoginBase>... calls) {

        AuthLoginBase loginInfo;
        try {
            loginInfo = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return loginInfo;
    }

    @Override
    protected void onPostExecute(AuthLoginBase loginInfo) {
        if (loginInfo != null) {
            boolean success = loginInfo.isSuccess();
            String error_message = "";
            if (loginInfo.getError() != null) {
                error_message = context.getString(
                        SynologyBaseError.getMessageId(loginInfo.getError().getCode()));
            }

            TextView debug_text_view = (TextView) context.findViewById(R.id.debug_api_text);

            if (success) {
                String sid = loginInfo.getSid();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(context.getString(R.string.pref_key_sid), sid);
                editor.putString(context.getString(R.string.pref_key_sid_header), "id=" + sid);
                editor.apply();

                TextView debug_text_view_sid = (TextView) context.findViewById(R.id.debug_api_sid);
                debug_text_view_sid.setText(sid);
                String text = "Login: Success";
                debug_text_view.setText(text);
            } else {
                debug_text_view.setText(error_message);
            }
        }
    }
}
