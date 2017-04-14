package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.DSTaskResumeBase;
import net.imatruck.dsmanager.models.DSTaskResumeData;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;


public class DSTaskResumeTask extends AsyncTask<Call<DSTaskResumeBase>, Void, DSTaskResumeBase> {

    private Activity context;

    public DSTaskResumeTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final DSTaskResumeBase doInBackground(Call<DSTaskResumeBase>... calls) {

        DSTaskResumeBase dsTaskResumeBase;
        try {
            dsTaskResumeBase = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return dsTaskResumeBase;
    }

    @Override
    protected void onPostExecute(DSTaskResumeBase dsInfo) {
        if (dsInfo != null) {
            boolean success = dsInfo.isSuccess();
            TextView debugTextView = (TextView) context.findViewById(R.id.debug_api_text);

            if (success) {
                List<DSTaskResumeData> data = dsInfo.getData();
                String text = "";

                for (DSTaskResumeData dsTaskResumeData : data) {
                    text += String.format(Locale.getDefault(),
                            "%s: %s\n",
                            dsTaskResumeData.getId(),
                            context.getString(
                                    SynologyDSTaskError.getMessageId(dsTaskResumeData.getError()))
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
