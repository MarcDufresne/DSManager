package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.DSTaskInfoBase;
import net.imatruck.dsmanager.models.DSTaskInfoData;
import net.imatruck.dsmanager.models.Task;
import net.imatruck.dsmanager.utils.BytesFormatter;
import net.imatruck.dsmanager.utils.PercentFormatter;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;
import net.imatruck.dsmanager.utils.TimestampFormatter;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;


public class DSTaskInfoTask extends AsyncTask<Call<DSTaskInfoBase>, Void, DSTaskInfoBase> {

    private Activity context;

    public DSTaskInfoTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final DSTaskInfoBase doInBackground(Call<DSTaskInfoBase>... calls) {

        DSTaskInfoBase dsTaskListBase;
        try {
            dsTaskListBase = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return dsTaskListBase;
    }

    @Override
    protected void onPostExecute(DSTaskInfoBase dsInfo) {
        if (dsInfo != null) {
            boolean success = dsInfo.isSuccess();
            TextView debug_text_view = (TextView) context.findViewById(R.id.debug_api_text);

            if (success) {
                DSTaskInfoData infoData = dsInfo.getData();
                String text = "";

                for (Task task : infoData.getTasks()) {
                    text += String.format(Locale.getDefault(),
                            "%s: %s\n%.2f%%, %s\n%s\n%s\n%s\n\n",
                            task.getId(),
                            task.getTitle(),
                            PercentFormatter.toPercent(
                                    task.getAdditional().getTransfer().getSizeDownloaded(),
                                    task.getSize()),
                            BytesFormatter.humanReadable(task.getSize(), false),
                            task.getAdditional().getDetail().getDestination(),
                            TimestampFormatter.timestampToDatetime(
                                    task.getAdditional().getDetail().getCompletedTime()),
                            TimestampFormatter.timestampToDatetime(
                                    task.getAdditional().getDetail().getCreateTime() * 1000
                            ));
                }
                debug_text_view.setText(text);
            } else {
                String text = context.getString(
                        SynologyDSTaskError.getMessageId(dsInfo.getError().getCode()));
                debug_text_view.setText(text);
            }
        }
    }
}
