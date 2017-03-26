package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.DSGetInfoBase;
import net.imatruck.dsmanager.models.DSGetInfoData;
import net.imatruck.dsmanager.models.DSSetConfigBase;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by marc on 2017-03-24.
 */

public class DSSetConfigTask extends AsyncTask<Call<DSSetConfigBase>, Void, DSSetConfigBase> {

    private Activity context;

    public DSSetConfigTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final DSSetConfigBase doInBackground(Call<DSSetConfigBase>... calls) {

        DSSetConfigBase dsInfo;
        try {
            dsInfo = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return dsInfo;
    }

    @Override
    protected void onPostExecute(DSSetConfigBase dsInfo) {
        if (dsInfo != null) {
            boolean success = dsInfo.isSuccess();
            TextView debug_text_view = (TextView) context.findViewById(R.id.debug_api_text);

            if (success) {
                String text = "Set Config: Success";
                debug_text_view.setText(text);
            } else {
                String text = context.getString(dsInfo.getError().getMessageId());
                debug_text_view.setText(text);
            }
        }
    }
}