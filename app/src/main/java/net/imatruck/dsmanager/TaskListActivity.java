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
import android.widget.Toast;

import net.imatruck.dsmanager.models.AuthLogoutBase;
import net.imatruck.dsmanager.models.DSStatsInfoBase;
import net.imatruck.dsmanager.models.DSTaskListBase;
import net.imatruck.dsmanager.models.Task;
import net.imatruck.dsmanager.models.TaskAdditionalTransfer;
import net.imatruck.dsmanager.network.SynologyAPI;
import net.imatruck.dsmanager.network.SynologyAPIHelper;
import net.imatruck.dsmanager.utils.BytesFormatter;
import net.imatruck.dsmanager.utils.PercentFormatter;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;
import net.imatruck.dsmanager.views.adapters.TaskListOnClickListener;
import net.imatruck.dsmanager.views.adapters.TasksArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    String sid;
    String server;

    enum SortType {
        DEFAULT, PERCENT, NAME, DL_SPEED, UL_SPEED
    }

    boolean sortAsc = true;
    SortType sortType = SortType.DEFAULT;

    enum Filter {
        NONE, BT, HTTP, NZB, FTP, EMULE
    }

    Filter filter = Filter.NONE;

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
        sidHeader = prefs.getString(getString(R.string.pref_key_sid_header), "");
        sid = prefs.getString(getString(R.string.pref_key_sid), "");
        server = prefs.getString(getString(R.string.pref_key_server), "");

        mHandler = new Handler();

        if (server.isEmpty() || sidHeader.isEmpty()) {
            Toast.makeText(
                    this,
                    getString(R.string.task_list_invalid_server_config),
                    Toast.LENGTH_LONG).show();
            clearStoredSid();
            startLoginActivity();
            return;
        }

        synologyApi = SynologyAPIHelper.INSTANCE.getSynologyApi(this);

        RefreshTasksTask refreshTasksTask = new RefreshTasksTask();
        refreshTasksTask.mTaskListActivity = this;
        refreshTasksTask.execute(synologyApi.dsTaskList(sidHeader));

        GetStatsInfoTask getStatsInfoTask = new GetStatsInfoTask();
        getStatsInfoTask.mTaskListActivity = this;
        getStatsInfoTask.execute(synologyApi.dsStatsInfo(sidHeader));

        startPeriodicRefresh();
    }

    @Override
    protected void onPause() {
        stopPeriodicRefresh();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        if (!BuildConfig.DEBUG) {
            MenuItem debugMenuItem = menu.findItem(R.id.action_debug);
            debugMenuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                startPeriodicRefresh();
                RefreshTasksTask refreshTasksTask = new RefreshTasksTask();
                refreshTasksTask.mTaskListActivity = this;
                refreshTasksTask.execute(synologyApi.dsTaskList(sidHeader));
                break;
            case R.id.action_server_config:
                Intent serverConfigIntent = new Intent(this, ServerConfigActivity.class);
                startActivity(serverConfigIntent);
                break;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_logout:
                LogoutTask logoutTask = new LogoutTask();
                logoutTask.mTaskListActivity = this;
                logoutTask.execute(synologyApi.authLogout(sid));
                break;
            case R.id.action_debug:
                Intent debugIntent = new Intent(this, DebugActivity.class);
                startActivity(debugIntent);
                break;
            case R.id.action_sort_percent_asc:
                item.setChecked(true);
                sortAsc = true;
                sortType = SortType.PERCENT;
                break;
            case R.id.action_sort_percent_desc:
                item.setChecked(true);
                sortAsc = false;
                sortType = SortType.PERCENT;
                break;
            case R.id.action_sort_name_asc:
                item.setChecked(true);
                sortAsc = true;
                sortType = SortType.NAME;
                break;
            case R.id.action_sort_name_desc:
                item.setChecked(true);
                sortAsc = false;
                sortType = SortType.NAME;
                break;
            case R.id.action_sort_dl_speed_asc:
                item.setChecked(true);
                sortAsc = true;
                sortType = SortType.DL_SPEED;
                break;
            case R.id.action_sort_dl_speed_desc:
                item.setChecked(true);
                sortAsc = false;
                sortType = SortType.DL_SPEED;
                break;
            case R.id.action_sort_ul_speed_asc:
                item.setChecked(true);
                sortAsc = true;
                sortType = SortType.UL_SPEED;
                break;
            case R.id.action_sort_ul_speed_desc:
                item.setChecked(true);
                sortAsc = false;
                sortType = SortType.UL_SPEED;
                break;
            case R.id.action_sort_default:
                item.setChecked(true);
                sortAsc = true;
                sortType = SortType.DEFAULT;
                break;
            case R.id.action_filter_none:
                item.setChecked(true);
                filter = Filter.NONE;
                break;
            case R.id.action_filter_bt:
                item.setChecked(true);
                filter = Filter.BT;
                break;
            case R.id.action_filter_emule:
                item.setChecked(true);
                filter = Filter.EMULE;
                break;
            case R.id.action_filter_ftp:
                item.setChecked(true);
                filter = Filter.FTP;
                break;
            case R.id.action_filter_http:
                item.setChecked(true);
                filter = Filter.HTTP;
                break;
            case R.id.action_filter_nzb:
                item.setChecked(true);
                filter = Filter.NZB;
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearStoredSid() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.pref_key_sid), "");
        editor.putString(getString(R.string.pref_key_sid_header), "");
        editor.apply();
    }

    private void startLoginActivity() {
        finish();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
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

    private void sortTasksByPercent(List<Task> tasks, final boolean asc) {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                TaskAdditionalTransfer transfer1 = t1.getAdditional().getTransfer();
                double sizeDownloaded1 = transfer1.getSizeDownloaded();
                double size1 = t1.getSize();
                int percentDone1 = (int) PercentFormatter.INSTANCE.toPercent(sizeDownloaded1, size1);

                TaskAdditionalTransfer transfer2 = t2.getAdditional().getTransfer();
                double sizeDownloaded2 = transfer2.getSizeDownloaded();
                double size2 = t2.getSize();
                int percentDone2 = (int) PercentFormatter.INSTANCE.toPercent(sizeDownloaded2, size2);

                if (percentDone1 < percentDone2)
                    return asc ? -1 : 1;
                else if (percentDone1 > percentDone2)
                    return asc ? 1 : -1;
                return 0;
            }
        });
    }

    private void sortTasksByDlSpeed(List<Task> tasks, final boolean asc) {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                double speed1 = t1.getAdditional().getTransfer().getSpeedDownload();
                double speed2 = t2.getAdditional().getTransfer().getSpeedDownload();

                if (speed1 < speed2)
                    return asc ? -1 : 1;
                else if (speed1 > speed2)
                    return asc ? 1 : -1;
                return 0;
            }
        });
    }

    private void sortTasksByUlSpeed(List<Task> tasks, final boolean asc) {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                double speed1 = t1.getAdditional().getTransfer().getSpeedUpload();
                double speed2 = t2.getAdditional().getTransfer().getSpeedUpload();

                if (speed1 < speed2)
                    return asc ? -1 : 1;
                else if (speed1 > speed2)
                    return asc ? 1 : -1;
                return 0;
            }
        });
    }

    private void sortTasksByName(List<Task> tasks, final boolean asc) {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                String name1 = t1.getTitle();
                String name2 = t2.getTitle();
                if (asc)
                    return name1.compareTo(name2);
                else
                    return name2.compareTo(name1);
            }
        });
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

                    SortType sortType = mTaskListActivity.sortType;
                    boolean sortAsc = mTaskListActivity.sortAsc;
                    Filter filter = mTaskListActivity.filter;

                    switch (sortType) {
                        case PERCENT:
                            mTaskListActivity.sortTasksByPercent(tasks, sortAsc);
                            break;
                        case NAME:
                            mTaskListActivity.sortTasksByName(tasks, sortAsc);
                            break;
                        case DL_SPEED:
                            mTaskListActivity.sortTasksByDlSpeed(tasks, sortAsc);
                            break;
                        case UL_SPEED:
                            mTaskListActivity.sortTasksByUlSpeed(tasks, sortAsc);
                            break;
                    }

                    switch (filter) {
                        case BT:
                            tasks.removeIf(task -> !task.getType().toLowerCase().equals("bt"));
                            break;
                        case HTTP:
                            tasks.removeIf(task -> !task.getType().toLowerCase().equals("http"));
                            break;
                        case FTP:
                            tasks.removeIf(task -> !task.getType().toLowerCase().equals("ftp"));
                            break;
                        case NZB:
                            tasks.removeIf(task -> !task.getType().toLowerCase().equals("nzb"));
                            break;
                        case EMULE:
                            tasks.removeIf(task -> !task.getType().toLowerCase().equals("emule"));
                            break;
                    }

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
                    if (dsTaskListBase.getError().getCode() >= 100 &&
                            dsTaskListBase.getError().getCode() < 200) {
                        mTaskListActivity.clearStoredSid();
                        mTaskListActivity.startLoginActivity();
                    }

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

    private static class LogoutTask extends AsyncTask<Call<AuthLogoutBase>, Void, AuthLogoutBase> {

        TaskListActivity mTaskListActivity = null;

        @SafeVarargs
        @Override
        protected final AuthLogoutBase doInBackground(Call<AuthLogoutBase>... calls) {
            AuthLogoutBase authLogoutBase;
            try {
                authLogoutBase = calls[0].execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return authLogoutBase;
        }

        @Override
        protected void onPostExecute(AuthLogoutBase authLogoutBase) {
            mTaskListActivity.clearStoredSid();
            mTaskListActivity.startLoginActivity();
            mTaskListActivity = null;
        }
    }

}
