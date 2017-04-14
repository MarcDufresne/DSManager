package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.DSSetConfigBase;
import net.imatruck.dsmanager.utils.SynologyBaseError;

import java.io.IOException;

import retrofit2.Call;


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
                String text = context.getString(
                        SynologyBaseError.getMessageId(dsInfo.getError().getCode()));
                debug_text_view.setText(text);
            }
        }
    }
}
