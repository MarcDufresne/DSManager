package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.DSTaskCreateBase;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by marc on 2017-03-24.
 */

public class DSTaskCreateTask extends AsyncTask<Call<DSTaskCreateBase>, Void, DSTaskCreateBase> {

    private Activity context;

    public DSTaskCreateTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final DSTaskCreateBase doInBackground(Call<DSTaskCreateBase>... calls) {

        DSTaskCreateBase dsTaskListBase;
        try {
            dsTaskListBase = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return dsTaskListBase;
    }

    @Override
    protected void onPostExecute(DSTaskCreateBase dsInfo) {
        if (dsInfo != null) {
            boolean success = dsInfo.isSuccess();
            TextView debugTextView = (TextView) context.findViewById(R.id.debug_api_text);

            if (success) {
                String text = "Task Create: Success";
                debugTextView.setText(text);
            } else {
                String text = context.getString(
                        SynologyDSTaskError.getMessageId(dsInfo.getError().getCode()));
                debugTextView.setText(text);
            }
        }
    }
}
