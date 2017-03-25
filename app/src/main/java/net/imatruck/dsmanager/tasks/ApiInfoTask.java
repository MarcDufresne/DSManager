package net.imatruck.dsmanager.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.APIInfoData;
import net.imatruck.dsmanager.models.APIInfoBase;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by marc on 2017-03-24.
 */

public class ApiInfoTask extends AsyncTask<Call<APIInfoBase>, Void, APIInfoBase> {

    private Activity context;

    public ApiInfoTask(Activity context) {
        this.context = context;
    }

    @SafeVarargs
    @Override
    protected final APIInfoBase doInBackground(Call<APIInfoBase>... calls) {

        APIInfoBase apiInfo;
        try {
            apiInfo = calls[0].execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return apiInfo;
    }

    @Override
    protected void onPostExecute(APIInfoBase apiInfo) {
        if (apiInfo != null) {
            String result = "";
            for (Map.Entry<String, APIInfoData> entry : apiInfo.getData().entrySet()) {
                result += entry.getKey() + ": " + entry.getValue().getPath() + "\n";
            }
            TextView debug_text_view = (TextView) context.findViewById(R.id.debug_api_text);
            debug_text_view.setText(result);
        }
    }
}
