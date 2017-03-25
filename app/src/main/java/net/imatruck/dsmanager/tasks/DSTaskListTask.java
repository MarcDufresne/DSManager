package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.DSGetConfigBase;
import net.imatruck.dsmanager.models.DSGetConfigData;
import net.imatruck.dsmanager.models.DSTaskListBase;
import net.imatruck.dsmanager.models.DSTaskListData;
import net.imatruck.dsmanager.models.Task;
import net.imatruck.dsmanager.utils.BytesFormater;
import net.imatruck.dsmanager.utils.PercentFormater;

import java.io.IOException;
import java.text.DecimalFormat;
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
                            "%s: %s\n%.2f%%, %s\n\n",
                            task.getId(),
                            task.getTitle(),
                            PercentFormater.toPercent(
                                    task.getAdditional().getTransfer().getSizeDownloaded(),
                                    task.getSize()),
                            BytesFormater.humanReadable(task.getSize()));
                }
                debug_text_view.setText(text);
            } else {
                String text = context.getString(dsInfo.getError().getMessageId());
                debug_text_view.setText(text);
            }
        }
    }
}
