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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.imatruck.dsmanager.models.AuthLoginBase;
import net.imatruck.dsmanager.models.DSTaskListBase;
import net.imatruck.dsmanager.models.Task;
import net.imatruck.dsmanager.network.SynologyAPI;
import net.imatruck.dsmanager.network.SynologyAPIHelper;
import net.imatruck.dsmanager.utils.SynologyBaseError;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;
import net.imatruck.dsmanager.views.adapters.TasksArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class TaskListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.task_list_view)
    ListView taskListView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.empty_task_list_text_view)
    TextView emptyListTextView;

    TasksArrayAdapter adapter;

    String sidHeader;

    SynologyAPI synologyApi;

    private int mInterval = 3000;
    private Handler mHandler;

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

        taskListView.setEmptyView(emptyListTextView);

        adapter = new TasksArrayAdapter(this, new ArrayList<Task>());
        taskListView.setAdapter(adapter);

        taskListView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sidHeader = prefs.getString(getString(R.string.pref_key_sid_header), null);

        synologyApi = SynologyAPIHelper.getSynologyApi(this);

        mHandler = new Handler();

        if (sidHeader == null) {
            String account = prefs.getString(getString(R.string.pref_key_account), null);
            String password = prefs.getString(getString(R.string.pref_key_password), null);

            if (account == null || password == null) {
                Snackbar.make(toolbar, "Missing credentials, see Settings",
                        Snackbar.LENGTH_LONG).show();
                stopPeriodicRefresh();
            } else {
                new LoginTask().execute(synologyApi.authLogin(account, password));
            }
        } else {
            new RefreshTasks().execute(synologyApi.dsTaskList(sidHeader));
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
            new RefreshTasks().execute(synologyApi.dsTaskList(sidHeader));
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

    Runnable mTaskRefresher = new Runnable() {
        @Override
        public void run() {
            new RefreshTasks().execute(synologyApi.dsTaskList(sidHeader));
            mHandler.postDelayed(mTaskRefresher, mInterval);
        }
    };

    private void startPeriodicRefresh() {
        stopPeriodicRefresh();
        mTaskRefresher.run();
    }

    private void stopPeriodicRefresh() {
        mHandler.removeCallbacks(mTaskRefresher);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Task task = (Task) adapter.getItem(position);
        if (task != null) {
            Intent taskDetailIntent = new Intent(this, TaskInfoActivity.class);
            taskDetailIntent.putExtra(TaskInfoActivity.EXTRA_TASK_ID, task.getId());
            startActivity(taskDetailIntent);
        }
    }

    private class RefreshTasks extends AsyncTask<Call<DSTaskListBase>, Void, DSTaskListBase> {

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

                    int index = taskListView.getFirstVisiblePosition();
                    View v = taskListView.getChildAt(0);
                    int top = (v == null) ? 0 : (v.getTop() - taskListView.getPaddingTop());

                    adapter.clear();
                    adapter.addAll(tasks);

                    adapter.notifyDataSetChanged();

                    taskListView.setSelectionFromTop(index, top);
                } else {
                    String text = getString(
                            SynologyDSTaskError.getMessageId(dsTaskListBase.getError().getCode()));
                    Snackbar.make(fab, text, Snackbar.LENGTH_LONG).show();
                }
            } else {
                String text = getString(R.string.synapi_error_1);
                Snackbar.make(fab, text, Snackbar.LENGTH_LONG).show();
                stopPeriodicRefresh();
            }
        }
    }

    private class LoginTask extends AsyncTask<Call<AuthLoginBase>, Void, AuthLoginBase> {

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
                            TaskListActivity.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(getString(R.string.pref_key_sid), loginSid);
                    editor.putString(getString(R.string.pref_key_sid_header), "id=" + loginSid);
                    editor.apply();

                    Snackbar.make(toolbar, "Logged-in", Snackbar.LENGTH_SHORT).show();

                    startPeriodicRefresh();
                } else {
                    String error = getString(SynologyBaseError.getMessageId(
                            authLoginBase.getError().getCode()));
                    Snackbar.make(toolbar, error, Snackbar.LENGTH_LONG);
                    stopPeriodicRefresh();
                }
            }
        }
    }

}
