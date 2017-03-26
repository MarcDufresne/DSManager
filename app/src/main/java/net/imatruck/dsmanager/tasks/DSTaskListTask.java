package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.DSTaskListBase;
import net.imatruck.dsmanager.models.DSTaskListData;
import net.imatruck.dsmanager.models.Task;
import net.imatruck.dsmanager.utils.BytesFormatter;
import net.imatruck.dsmanager.utils.EtaFormatter;
import net.imatruck.dsmanager.utils.PercentFormatter;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by marc on 2017-03-24.
 */

public class DSTaskListTask extends AsyncTask<Call<DSTaskListBase>, Void, DSTaskListBase> {

    private Activity context;

    public DSTaskListTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final DSTaskListBase doInBackground(Call<DSTaskListBase>... calls) {

        DSTaskListBase dsTaskListBase;
        try {
            dsTaskListBase = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return dsTaskListBase;
    }

    @Override
    protected void onPostExecute(DSTaskListBase dsInfo) {
        if (dsInfo != null) {
            boolean success = dsInfo.isSuccess();
            TextView debug_text_view = (TextView) context.findViewById(R.id.debug_api_text);

            if (success) {
                DSTaskListData infoData = dsInfo.getData();
                String text = "";

                for (Task task : infoData.getTasks()) {
                    text += String.format(Locale.getDefault(),
                            "%s: %s\n%.2f%%, %s - Down: %s - Up: %s - ETA: %s\n\n",
                            task.getId(),
                            task.getTitle(),
                            PercentFormatter.toPercent(
                                    task.getAdditional().getTransfer().getSizeDownloaded(),
                                    task.getSize()),
                            BytesFormatter.humanReadable(task.getSize(), false),
                            BytesFormatter.humanReadable(
                                    task.getAdditional().getTransfer().getSpeedDownload(), true),
                            BytesFormatter.humanReadable(
                                    task.getAdditional().getTransfer().getSpeedUpload(), true),
                            EtaFormatter.calculateEta(
                                    task.getAdditional().getTransfer().getSizeDownloaded(),
                                    task.getSize(),
                                    task.getAdditional().getTransfer().getSpeedDownload())
                    );
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
