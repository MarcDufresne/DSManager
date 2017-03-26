package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.DSGetConfigBase;
import net.imatruck.dsmanager.models.DSGetConfigData;
import net.imatruck.dsmanager.utils.SynologyBaseError;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by marc on 2017-03-24.
 */

public class DSGetConfigTask extends AsyncTask<Call<DSGetConfigBase>, Void, DSGetConfigBase> {

    private Activity context;

    public DSGetConfigTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final DSGetConfigBase doInBackground(Call<DSGetConfigBase>... calls) {

        DSGetConfigBase dsInfo;
        try {
            dsInfo = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return dsInfo;
    }

    @Override
    protected void onPostExecute(DSGetConfigBase dsInfo) {
        if (dsInfo != null) {
            boolean success = dsInfo.isSuccess();
            TextView debug_text_view = (TextView) context.findViewById(R.id.debug_api_text);

            if (success) {
                DSGetConfigData infoData = dsInfo.getData();
                String text = "BT Max DL: " + infoData.getBtMaxDownload();
                text += "\nBT Max UL: " + infoData.getBtMaxUpload();
                text += "\nDefault Dest: " + infoData.getDefaultDestination();
                debug_text_view.setText(text);
            } else {
                String text = context.getString(
                        SynologyBaseError.getMessageId(dsInfo.getError().getCode()));
                debug_text_view.setText(text);
            }
        }
    }
}
