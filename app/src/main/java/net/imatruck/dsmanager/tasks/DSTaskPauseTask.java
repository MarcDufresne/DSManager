package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.DSTaskPauseBase;
import net.imatruck.dsmanager.models.DSTaskPauseData;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by marc on 2017-03-24.
 */

public class DSTaskPauseTask extends AsyncTask<Call<DSTaskPauseBase>, Void, DSTaskPauseBase> {

    private Activity context;

    public DSTaskPauseTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final DSTaskPauseBase doInBackground(Call<DSTaskPauseBase>... calls) {

        DSTaskPauseBase dsTaskPauseBase;
        try {
            dsTaskPauseBase = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return dsTaskPauseBase;
    }

    @Override
    protected void onPostExecute(DSTaskPauseBase dsInfo) {
        if (dsInfo != null) {
            boolean success = dsInfo.isSuccess();
            TextView debugTextView = (TextView) context.findViewById(R.id.debug_api_text);

            if (success) {
                List<DSTaskPauseData> data = dsInfo.getData();
                String text = "";

                for (DSTaskPauseData dsTaskPauseData : data) {
                    text += String.format(Locale.getDefault(),
                            "%s: %s\n",
                            dsTaskPauseData.getId(),
                            context.getString(
                                    SynologyDSTaskError.getMessageId(dsTaskPauseData.getError()))
                    );
                }
                debugTextView.setText(text);
            } else {
                String text = context.getString(
                        SynologyDSTaskError.getMessageId(dsInfo.getError().getCode()));
                debugTextView.setText(text);
            }
        }
    }
}
