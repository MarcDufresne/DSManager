package net.imatruck.dsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.imatruck.dsmanager.models.AuthLoginBase;
import net.imatruck.dsmanager.models.DSStatsInfoBase;
import net.imatruck.dsmanager.models.DSTaskListBase;
import net.imatruck.dsmanager.models.Task;
import net.imatruck.dsmanager.network.SynologyAPI;
import net.imatruck.dsmanager.network.SynologyAPIHelper;
import net.imatruck.dsmanager.utils.BytesFormatter;
import net.imatruck.dsmanager.utils.SynologyBaseError;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;
import net.imatruck.dsmanager.views.adapters.TaskListOnClickListener;
import net.imatruck.dsmanager.views.adapters.TasksArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

@SuppressWarnings("ALL")
public class TaskListActivity extends AppCompatActivity implements TaskListOnClickListener {

    @BindView(R.id.task_list_view)
    RecyclerView taskListView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.empty_task_list_text_view)
    TextView emptyListTextView;

    TasksArrayAdapter adapter;

    String sidHeader;
    String server;

    SynologyAPI synologyApi;

    private Handler mHandler;

    Runnable mTaskRefresher = new Runnable() {
        @Override
        public void run() {
            RefreshTasksTask refreshTasksTask = new RefreshTasksTask();
            refreshTasksTask.mTaskListActivity = TaskListActivity.this;
            refreshTasksTask.execute(synologyApi.dsTaskList(sidHeader));

            GetStatsInfoTask getStatsInfoTask = new GetStatsInfoTask();
            getStatsInfoTask.mTaskListActivity = TaskListActivity.this;
            getStatsInfoTask.execute(synologyApi.dsStatsInfo(sidHeader));

            int interval = 3000;
            mHandler.postDelayed(mTaskRefresher, interval);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(TaskListActivity.this, AddTaskActivity.class);
                startActivity(addTaskIntent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskListView.setLayoutManager(layoutManager);

        adapter = new TasksArrayAdapter(this, new ArrayList<Task>(), this);
        taskListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateEmptyVisibility();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sidHeader = prefs.getString(getString(R.string.pref_key_sid_header), null);
        server = prefs.getString(getString(R.string.pref_key_server), null);

        mHandler = new Handler();

        if (server == null || server.isEmpty()) {
            Snackbar.make(toolbar, "See Settings to configure your server's address",
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        synologyApi = SynologyAPIHelper.INSTANCE.getSynologyApi(this);

        if (sidHeader == null || sidHeader.isEmpty()) {
            String account = prefs.getString(getString(R.string.pref_key_account), null);
            String password = prefs.getString(getString(R.string.pref_key_password), null);

            if (account == null || password == null || account.isEmpty() || password.isEmpty()) {
                Snackbar.make(toolbar, "Missing credentials, see Settings",
                        Snackbar.LENGTH_LONG).show();
                stopPeriodicRefresh();
            } else {
                LoginTask loginTask = new LoginTask();
                loginTask.mTaskListActivity = this;
                loginTask.execute(synologyApi.authLogin(account, password));
            }
        } else {
            RefreshTasksTask refreshTasksTask = new RefreshTasksTask();
            refreshTasksTask.mTaskListActivity = this;
            refreshTasksTask.execute(synologyApi.dsTaskList(sidHeader));

            GetStatsInfoTask getStatsInfoTask = new GetStatsInfoTask();
            getStatsInfoTask.mTaskListActivity = this;
            getStatsInfoTask.execute(synologyApi.dsStatsInfo(sidHeader));

            startPeriodicRefresh();
        }
    }

    @Override
    protected void onPause() {
        stopPeriodicRefresh();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            startPeriodicRefresh();
            RefreshTasksTask refreshTasksTask = new RefreshTasksTask();
            refreshTasksTask.mTaskListActivity = this;
            refreshTasksTask.execute(synologyApi.dsTaskList(sidHeader));
        }

        if (id == R.id.action_server_config) {
            Intent serverConfigIntent = new Intent(this, ServerConfigActivity.class);
            startActivity(serverConfigIntent);
        }

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_debug) {
            Intent debugIntent = new Intent(this, DebugActivity.class);
            startActivity(debugIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void startPeriodicRefresh() {
        stopPeriodicRefresh();
        mTaskRefresher.run();
    }

    private void stopPeriodicRefresh() {
        mHandler.removeCallbacks(mTaskRefresher);
    }

    private void updateEmptyVisibility() {
        if (adapter.isEmpty()) {
            emptyListTextView.setVisibility(View.VISIBLE);
            taskListView.setVisibility(View.INVISIBLE);
        } else {
            emptyListTextView.setVisibility(View.INVISIBLE);
            taskListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(int position) {
        Task task = adapter.getTask(position);
        if (task != null) {
            Intent taskDetailIntent = new Intent(this, TaskInfoActivity.class);
            taskDetailIntent.putExtra(TaskInfoActivity.EXTRA_TASK_ID, task.getId());
            startActivity(taskDetailIntent);
        }
    }

    private static class RefreshTasksTask extends AsyncTask<Call<DSTaskListBase>, Void, DSTaskListBase> {

        TaskListActivity mTaskListActivity = null;

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
        protected void onPostExecute(DSTaskListBase dsTaskListBase) {
            if (dsTaskListBase != null) {
                if (dsTaskListBase.isSuccess()) {
                    List<Task> tasks = dsTaskListBase.getData().getTasks();

                    LinearLayoutManager llm = (LinearLayoutManager) mTaskListActivity.taskListView.getLayoutManager();
                    int firstItem = llm.findFirstVisibleItemPosition();
                    View firstView = llm.findViewByPosition(firstItem);
                    float offsetTop = (firstView != null) ? firstView.getTop() : 0;

                    mTaskListActivity.adapter.clear();
                    mTaskListActivity.adapter.addAll(tasks);

                    mTaskListActivity.adapter.notifyDataSetChanged();

                    mTaskListActivity.updateEmptyVisibility();

                    llm.scrollToPositionWithOffset(firstItem, (int) offsetTop);
                } else {
                    String text = mTaskListActivity.getString(
                            SynologyDSTaskError.Companion.getMessageId(dsTaskListBase.getError().getCode()));
                    Snackbar.make(mTaskListActivity.fab, text, Snackbar.LENGTH_LONG).show();
                    mTaskListActivity.stopPeriodicRefresh();
                }
            } else {
                String text = mTaskListActivity.getString(R.string.synapi_error_1);
                Snackbar.make(mTaskListActivity.fab, text, Snackbar.LENGTH_LONG).show();
                mTaskListActivity.stopPeriodicRefresh();
            }

            mTaskListActivity = null;
        }
    }

    private static class GetStatsInfoTask extends AsyncTask<Call<DSStatsInfoBase>, Void, DSStatsInfoBase> {

        TaskListActivity mTaskListActivity = null;

        @SafeVarargs
        @Override
        protected final DSStatsInfoBase doInBackground(Call<DSStatsInfoBase>... calls) {
            DSStatsInfoBase dsStatsInfoBase;
            try {
                dsStatsInfoBase = calls[0].execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return dsStatsInfoBase;
        }

        @Override
        protected void onPostExecute(DSStatsInfoBase dsStatsInfoBase) {
            if (dsStatsInfoBase != null) {
                if (dsStatsInfoBase.isSuccess()) {
                    String text = String.format(Locale.getDefault(), "↓ %s - ↑ %s",
                            BytesFormatter.INSTANCE.humanReadable(dsStatsInfoBase.getData().getSpeedDownload(), true),
                            BytesFormatter.INSTANCE.humanReadable(dsStatsInfoBase.getData().getSpeedUpload(), true));
                    mTaskListActivity.toolbar.setSubtitle(text);
                }
            } else {
                mTaskListActivity.toolbar.setSubtitle(null);
            }

            mTaskListActivity = null;
        }
    }

    private static class LoginTask extends AsyncTask<Call<AuthLoginBase>, Void, AuthLoginBase> {

        TaskListActivity mTaskListActivity = null;

        @SafeVarargs
        @Override
        protected final AuthLoginBase doInBackground(Call<AuthLoginBase>... calls) {
            AuthLoginBase authLoginBase;
            try {
                authLoginBase = calls[0].execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return authLoginBase;
        }

        @Override
        protected void onPostExecute(AuthLoginBase authLoginBase) {
            if (authLoginBase != null) {
                if (authLoginBase.isSuccess()) {
                    String loginSid = authLoginBase.getSid();

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                            mTaskListActivity);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(mTaskListActivity.getString(R.string.pref_key_sid), loginSid);
                    editor.putString(mTaskListActivity.getString(R.string.pref_key_sid_header), "id=" + loginSid);
                    editor.apply();

                    Snackbar.make(mTaskListActivity.toolbar, "Logged-in", Snackbar.LENGTH_SHORT).show();

                    mTaskListActivity.startPeriodicRefresh();
                } else {
                    String error = mTaskListActivity.getString(SynologyBaseError.Companion.getMessageId(
                            authLoginBase.getError().getCode()));
                    Snackbar.make(mTaskListActivity.toolbar, error, Snackbar.LENGTH_LONG);
                    mTaskListActivity.stopPeriodicRefresh();
                }
            }

            mTaskListActivity = null;
        }
    }

}
