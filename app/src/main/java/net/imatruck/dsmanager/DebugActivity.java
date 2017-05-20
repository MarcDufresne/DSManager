package net.imatruck.dsmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class DebugActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_debug);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sid = prefs.getString(getString(R.string.pref_key_sid), "");
        debugTextSid.setText(sid);

        synologyApi = SynologyAPIHelper.INSTANCE.getSynologyApi(this);

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
                        new ApiInfoTask(DebugActivity.this).execute(synologyApi.apiGetInfo());
                        break;
                    case 1: //  Auth Login
                        String account = prefs.getString(getString(R.string.pref_key_account), "");
                        String password = prefs.getString(getString(R.string.pref_key_password), "");
                        new AuthLoginTask(DebugActivity.this).execute(synologyApi.authLogin(account, password));
                        break;
                    case 2: //  Auth Logout
                        new AuthLogoutTask(DebugActivity.this).execute(synologyApi.authLogout(sid));
                        break;
                    case 3: //  DS Get Info
                        new DSGetInfoTask(DebugActivity.this).execute(synologyApi.dsGetInfo(sidHeader));
                        break;
                    case 4: //  DS Get Config
                        new DSGetConfigTask(DebugActivity.this).execute(synologyApi.dsGetConfig(sidHeader));
                        break;
                    case 5: //  DS Set Config
                        new DSSetConfigTask(DebugActivity.this).execute(synologyApi.dsSetConfig(
                                sidHeader, 0, 0, null, null, null, null, null, null, null));
                        break;
                    case 6: //  DS Task List
                        new DSTaskListTask(DebugActivity.this).execute(synologyApi.dsTaskList(sidHeader));
                        break;
                    case 7: //  DS Task Get Info
                        new DSTaskInfoTask(DebugActivity.this).execute(synologyApi.dsTaskInfo(sidHeader, editTextValue));
                        break;
                    case 8: //  DS Task Create URI
                        new DSTaskCreateTask(DebugActivity.this).execute(synologyApi.dsTaskCreateUri(
                                RequestDSTaskCreate.INSTANCE.getCreateWithURIMap(sid, editTextValue)));
                        break;
                    case 9: // DS Task Create File
                        break;
                    case 10: // DS Task Delete
                        new DSTaskDeleteTask(DebugActivity.this).execute(synologyApi.dsTaskDelete(
                                sidHeader, editTextValue));
                        break;
                    case 11: // DS Task Pause
                        new DSTaskPauseTask(DebugActivity.this).execute(synologyApi.dsTaskPause(
                                sidHeader, editTextValue));
                        break;
                    case 12: // DS Task Resume
                        new DSTaskResumeTask(DebugActivity.this).execute(synologyApi.dsTaskResume(
                                sidHeader, editTextValue));
                        break;
                    case 13: // DS Task Edit
                        new DSTaskEditTask(DebugActivity.this).execute(synologyApi.dsTaskEdit(
                                sidHeader, editTextValue, "Downloads"));
                        break;
                    case 14: // DS Stats Info
                        new DSStatsInfoTask(DebugActivity.this).execute(synologyApi.dsStatsInfo(sidHeader));
                        break;
                    default:
                        break;
                }

            }
        });
    }
}
