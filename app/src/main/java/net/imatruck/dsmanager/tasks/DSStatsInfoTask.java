package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.APIInfoBase;
import net.imatruck.dsmanager.models.APIInfoData;
import net.imatruck.dsmanager.models.DSStatsInfoBase;
import net.imatruck.dsmanager.utils.BytesFormatter;
import net.imatruck.dsmanager.utils.SynologyBaseError;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by marc on 2017-03-24.
 */

public class DSStatsInfoTask extends AsyncTask<Call<DSStatsInfoBase>, Void, DSStatsInfoBase> {

    private Activity context;

    public DSStatsInfoTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final DSStatsInfoBase doInBackground(Call<DSStatsInfoBase>... calls) {

        DSStatsInfoBase dsStatsInfoBase;
        try {
            dsStatsInfoBase = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return dsStatsInfoBase;
    }

    @Override
    protected void onPostExecute(DSStatsInfoBase dsStatsInfoBase) {
        if (dsStatsInfoBase != null) {
            TextView debugTextView = (TextView) context.findViewById(R.id.debug_api_text);
            String result = "";

            if (dsStatsInfoBase.isSuccess()) {
                String text = String.format(Locale.getDefault(), "DL: %s\nUL: %s",
                        BytesFormatter.humanReadable(dsStatsInfoBase.getData().getSpeedDownload(), true),
                        BytesFormatter.humanReadable(dsStatsInfoBase.getData().getSpeedUpload(), true));
                debugTextView.setText(text);
            } else {
                String text = String.format(Locale.getDefault(), "Error: %s\n",
                        SynologyBaseError.getMessageId(dsStatsInfoBase.getError().getCode()));
                debugTextView.setText(text);
            }



        }
    }
}
