package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.DSTaskDeleteBase;
import net.imatruck.dsmanager.models.DSTaskDeleteData;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by marc on 2017-03-24.
 */

public class DSTaskDeleteTask extends AsyncTask<Call<DSTaskDeleteBase>, Void, DSTaskDeleteBase> {

    private Activity context;

    public DSTaskDeleteTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final DSTaskDeleteBase doInBackground(Call<DSTaskDeleteBase>... calls) {

        DSTaskDeleteBase dsTaskDeleteBase;
        try {
            dsTaskDeleteBase = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return dsTaskDeleteBase;
    }

    @Override
    protected void onPostExecute(DSTaskDeleteBase dsInfo) {
        if (dsInfo != null) {
            boolean success = dsInfo.isSuccess();
            TextView debugTextView = (TextView) context.findViewById(R.id.debug_api_text);

            if (success) {
                List<DSTaskDeleteData> infoData = dsInfo.getData();
                String text = "";

                for (DSTaskDeleteData dsTaskDeleteData : infoData) {
                    text += String.format(Locale.getDefault(),
                            "%s: %s\n",
                            dsTaskDeleteData.getId(),
                            context.getString(
                                    SynologyDSTaskError.getMessageId(dsTaskDeleteData.getError()))
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
