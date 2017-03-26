package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.AuthLogoutBase;
import net.imatruck.dsmanager.models.DSTaskEditBase;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by marc on 2017-03-24.
 */

public class DSTaskEditTask extends AsyncTask<Call<DSTaskEditBase>, Void, DSTaskEditBase> {

    private Activity context;

    public DSTaskEditTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final DSTaskEditBase doInBackground(Call<DSTaskEditBase>... calls) {

        DSTaskEditBase loginInfo;
        try {
            loginInfo = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return loginInfo;
    }

    @Override
    protected void onPostExecute(DSTaskEditBase result) {
        if (result != null) {
            boolean success = result.isSuccess();

            TextView debugTextView = (TextView) context.findViewById(R.id.debug_api_text);
            String text = "Task Edit: " + String.valueOf(success);
            debugTextView.setText(text);
        }
    }
}
