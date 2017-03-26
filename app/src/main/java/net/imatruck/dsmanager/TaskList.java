package net.imatruck.dsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.imatruck.dsmanager.models.RequestDSTaskCreate;
import net.imatruck.dsmanager.network.SynologyAPI;
import net.imatruck.dsmanager.network.SynologyAPIHelper;
import net.imatruck.dsmanager.tasks.ApiInfoTask;
import net.imatruck.dsmanager.tasks.AuthLoginTask;
import net.imatruck.dsmanager.tasks.AuthLogoutTask;
import net.imatruck.dsmanager.tasks.DSGetConfigTask;
import net.imatruck.dsmanager.tasks.DSGetInfoTask;
import net.imatruck.dsmanager.tasks.DSSetConfigTask;
import net.imatruck.dsmanager.tasks.DSStatsInfoTask;
import net.imatruck.dsmanager.tasks.DSTaskCreateTask;
import net.imatruck.dsmanager.tasks.DSTaskDeleteTask;
import net.imatruck.dsmanager.tasks.DSTaskEditTask;
import net.imatruck.dsmanager.tasks.DSTaskInfoTask;
import net.imatruck.dsmanager.tasks.DSTaskListTask;
import net.imatruck.dsmanager.tasks.DSTaskPauseTask;
import net.imatruck.dsmanager.tasks.DSTaskResumeTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskList extends AppCompatActivity {
    @BindView(R.id.debug_api_button)
    Button debugButton;
    @BindView(R.id.debug_api_spinner)
    Spinner debugSpinner;
    @BindView(R.id.debug_edittext)
    EditText debugEditText;
    @BindView(R.id.debug_api_text)
    TextView debugText;
    @BindView(R.id.debug_api_sid)
    TextView debugTextSid;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    SynologyAPI synologyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        ButterKnife.bind(this);

//        final SharedPreferences sharedPref = this.getSharedPreferences(
//                getString(R.string.pref_key_shared_file), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(getString(R.string.pref_key_account), "");
//        editor.putString(getString(R.string.pref_key_password), "");
//        editor.putString(getString(R.string.pref_key_server), "");
//        editor.apply();

        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sid = prefs.getString(getString(R.string.pref_key_sid), "");
        debugTextSid.setText(sid);

        SynologyAPIHelper helper = new SynologyAPIHelper();
        synologyApi = helper.getSynologyApi(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        debugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext());
                String sid = prefs.getString(getString(R.string.pref_key_sid), "");
                String sidHeader = prefs.getString(getString(R.string.pref_key_sid_header), "");

                String editTextValue = debugEditText.getText().toString();

                switch (debugSpinner.getSelectedItemPosition()) {
                    case 0: // API Info
                        new ApiInfoTask(TaskList.this).execute(synologyApi.apiGetInfo());
                        break;
                    case 1: //  Auth Login
                        String account = prefs.getString(getString(R.string.pref_key_account), "");
                        String password = prefs.getString(getString(R.string.pref_key_password), "");
                        new AuthLoginTask(TaskList.this).execute(synologyApi.authLogin(account, password));
                        break;
                    case 2: //  Auth Logout
                        new AuthLogoutTask(TaskList.this).execute(synologyApi.authLogout(sid));
                        break;
                    case 3: //  DS Get Info
                        new DSGetInfoTask(TaskList.this).execute(synologyApi.dsGetInfo(sidHeader));
                        break;
                    case 4: //  DS Get Config
                        new DSGetConfigTask(TaskList.this).execute(synologyApi.dsGetConfig(sidHeader));
                        break;
                    case 5: //  DS Set Config
                        new DSSetConfigTask(TaskList.this).execute(synologyApi.dsSetConfig(
                                sidHeader, 0, 0, null, null, null, null, null, null, null));
                        break;
                    case 6: //  DS Task List
                        new DSTaskListTask(TaskList.this).execute(synologyApi.dsTaskList(sidHeader));
                        break;
                    case 7: //  DS Task Get Info
                        new DSTaskInfoTask(TaskList.this).execute(synologyApi.dsTaskInfo(sidHeader, editTextValue));
                        break;
                    case 8: //  DS Task Create URI
                        new DSTaskCreateTask(TaskList.this).execute(synologyApi.dsTaskCreate(
                                RequestDSTaskCreate.getCreateWithURIMap(sid, editTextValue)));
                        break;
                    case 9: // DS Task Create File
                        break;
                    case 10: // DS Task Delete
                        new DSTaskDeleteTask(TaskList.this).execute(synologyApi.dsTaskDelete(
                                sidHeader, editTextValue));
                        break;
                    case 11: // DS Task Pause
                        new DSTaskPauseTask(TaskList.this).execute(synologyApi.dsTaskPause(
                                sidHeader, editTextValue));
                        break;
                    case 12: // DS Task Resume
                        new DSTaskResumeTask(TaskList.this).execute(synologyApi.dsTaskResume(
                                sidHeader, editTextValue));
                        break;
                    case 13: // DS Task Edit
                        new DSTaskEditTask(TaskList.this).execute(synologyApi.dsTaskEdit(
                                sidHeader, editTextValue, "Downloads"));
                        break;
                    case 14: // DS Stats Info
                        new DSStatsInfoTask(TaskList.this).execute(synologyApi.dsStatsInfo(sidHeader));
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
