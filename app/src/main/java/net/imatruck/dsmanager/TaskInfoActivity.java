package net.imatruck.dsmanager;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.imatruck.dsmanager.models.DSTaskInfoBase;
import net.imatruck.dsmanager.models.DSTaskListBase;
import net.imatruck.dsmanager.models.Task;
import net.imatruck.dsmanager.models.TaskAdditionalTransfer;
import net.imatruck.dsmanager.network.SynologyAPI;
import net.imatruck.dsmanager.network.SynologyAPIHelper;
import net.imatruck.dsmanager.utils.BytesFormatter;
import net.imatruck.dsmanager.utils.EtaFormatter;
import net.imatruck.dsmanager.utils.PercentFormatter;
import net.imatruck.dsmanager.utils.StatusFormatter;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class TaskInfoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.detail_title_text)
    TextView detailTitleText;
    @BindView(R.id.detail_status_text)
    TextView detailStatusText;
    @BindView(R.id.detail_progress_bar)
    ProgressBar detailProgressBar;
    @BindView(R.id.detail_progress_text)
    TextView detailProgressText;
    @BindView(R.id.detail_download_speed_text)
    TextView detailDownloadSpeedText;
    @BindView(R.id.detail_upload_speed_text)
    TextView detailUploadText;
    @BindView(R.id.detail_eta_text)
    TextView detailEtaText;
    @BindView(R.id.detail_size_text)
    TextView detailSizeText;
    @BindView(R.id.detail_destination_text)
    TextView detailDestinationText;

    SynologyAPI synologyAPI;

    String sidHeader;
    String taskId;

    private int mInterval = 3000;
    private Handler mHandler;

    public static final String EXTRA_TASK_ID = "extra_task_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        if (taskId == null) {
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sidHeader = prefs.getString(getString(R.string.pref_key_sid_header), null);

        synologyAPI = SynologyAPIHelper.getSynologyApi(this);

        new GetTaskDetailTask().execute(synologyAPI.dsTaskInfo(sidHeader, taskId));

        mHandler = new Handler();
        startPeriodicRefresh();
    }

    @Override
    protected void onPause() {
        stopPeriodicRefresh();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_edit) {

        } else if (itemId == R.id.action_pause) {

        } else if (itemId == R.id.action_resume) {

        } else if (itemId == R.id.action_delete) {

        }

        return super.onOptionsItemSelected(item);
    }

    Runnable mTaskRefresher = new Runnable() {
        @Override
        public void run() {
            new GetTaskDetailTask().execute(synologyAPI.dsTaskInfo(sidHeader, taskId));
            mHandler.postDelayed(mTaskRefresher, mInterval);
        }
    };

    private void startPeriodicRefresh() {
        mTaskRefresher.run();
    }

    private void stopPeriodicRefresh() {
        mHandler.removeCallbacks(mTaskRefresher);
    }

    private void updateDetailScreen(Task task) {

        String title = task.getTitle();
        double size = task.getSize();
        String statusCode = task.getStatus();

        String destination = task.getAdditional().getDetail().getDestination();

        TaskAdditionalTransfer transfer = task.getAdditional().getTransfer();
        double sizeDownloaded = transfer.getSizeDownloaded();
        double sizeUploaded = transfer.getSizeUploaded();
        double downloadSpeed = transfer.getSpeedDownload();
        double uploadSpeed = transfer.getSpeedUpload();

        String statusString = getString(StatusFormatter.getStatusString(statusCode));

        int percentDone = (int) PercentFormatter.toPercent(sizeDownloaded, size);
        String percentString = String.format(Locale.getDefault(), "%d%%", percentDone);
        int colorRes = StatusFormatter.getStatusColor(task.getStatus());
        int color = ResourcesCompat.getColor(getResources(), colorRes, null);

        String downloadSpeedString = BytesFormatter.humanReadable(downloadSpeed, true);
        String uploadSpeedString = BytesFormatter.humanReadable(uploadSpeed, true);
        String sizeDownloadedString = BytesFormatter.humanReadable(sizeDownloaded, false);
        String sizeUploadedString = BytesFormatter.humanReadable(sizeUploaded, false);

        String downloadString = String.format(Locale.getDefault(), "%s (%s)",
                downloadSpeedString, sizeDownloadedString);
        String uploadString = String.format(Locale.getDefault(), "%s (%s)",
                uploadSpeedString, sizeUploadedString);

        String etaString = EtaFormatter.calculateEta(sizeDownloaded, size, downloadSpeed);

        String sizeString = BytesFormatter.humanReadable(size, false);

        detailTitleText.setText(title);
        detailStatusText.setText(statusString);
        detailProgressBar.setProgress(percentDone);
        detailProgressBar.setProgressTintList(ColorStateList.valueOf(color));
        detailProgressText.setText(percentString);
        detailDownloadSpeedText.setText(downloadString);
        detailUploadText.setText(uploadString);
        detailEtaText.setText(etaString);
        detailSizeText.setText(sizeString);
        detailDestinationText.setText(destination);
    }

    private class GetTaskDetailTask extends AsyncTask<Call<DSTaskInfoBase>, Void, DSTaskInfoBase> {

        @Override
        protected DSTaskInfoBase doInBackground(Call<DSTaskInfoBase>... calls) {
            DSTaskInfoBase dsTaskInfoBase;
            try {
                dsTaskInfoBase = calls[0].execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return dsTaskInfoBase;
        }

        @Override
        protected void onPostExecute(DSTaskInfoBase dsTaskInfoBase) {
            if (dsTaskInfoBase != null) {
                if (dsTaskInfoBase.isSuccess()) {
                    List<Task> tasks = dsTaskInfoBase.getData().getTasks();
                    Task task = tasks.get(0);
                    if (task != null) {
                        updateDetailScreen(task);
                    } else {
                        Snackbar.make(toolbar, getString(R.string.synapi_error_100),
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    String text = getString(
                            SynologyDSTaskError.getMessageId(dsTaskInfoBase.getError().getCode()));
                    Snackbar.make(toolbar, text, Snackbar.LENGTH_LONG).show();
                }
            } else {
                String text = getString(R.string.synapi_error_1);
                Snackbar.make(toolbar, text, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}