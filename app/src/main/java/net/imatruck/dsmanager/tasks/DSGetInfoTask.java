package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.DSGetInfoBase;
import net.imatruck.dsmanager.models.DSGetInfoData;
import net.imatruck.dsmanager.utils.SynologyBaseError;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by marc on 2017-03-24.
 */

public class DSGetInfoTask extends AsyncTask<Call<DSGetInfoBase>, Void, DSGetInfoBase> {

    private Activity context;

    public DSGetInfoTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final DSGetInfoBase doInBackground(Call<DSGetInfoBase>... calls) {

        DSGetInfoBase dsInfo;
        try {
            dsInfo = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return dsInfo;
    }

    @Override
    protected void onPostExecute(DSGetInfoBase dsInfo) {
        if (dsInfo != null) {
            boolean success = dsInfo.isSuccess();
            TextView debug_text_view = (TextView) context.findViewById(R.id.debug_api_text);

            if (success) {
                DSGetInfoData infoData = dsInfo.getData();
                String text = "Version: " + infoData.getVersionString();
                text += "\nVersion ID: " + infoData.getVersion();
                debug_text_view.setText(text);
            } else {
                String text = context.getString(
                        SynologyBaseError.getMessageId(dsInfo.getError().getCode()));
                debug_text_view.setText(text);
            }
        }
    }
}
