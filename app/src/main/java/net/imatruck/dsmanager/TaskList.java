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
import android.widget.Spinner;
import android.widget.TextView;

import net.imatruck.dsmanager.network.SynologyAPI;
import net.imatruck.dsmanager.network.SynologyAPIHelper;
import net.imatruck.dsmanager.tasks.ApiInfoTask;
import net.imatruck.dsmanager.tasks.AuthLoginTask;
import net.imatruck.dsmanager.tasks.AuthLogoutTask;
import net.imatruck.dsmanager.tasks.DSGetConfigTask;
import net.imatruck.dsmanager.tasks.DSGetInfoTask;
import net.imatruck.dsmanager.tasks.DSSetConfigTask;
import net.imatruck.dsmanager.tasks.DSTaskInfoTask;
import net.imatruck.dsmanager.tasks.DSTaskListTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskList extends AppCompatActivity {
    @BindView(R.id.debug_api_button)
    Button debug_button;
    @BindView(R.id.debug_api_spinner)
    Spinner debug_spinner;
    @BindView(R.id.debug_api_text)
    TextView debug_text;
    @BindView(R.id.debug_api_sid)
    TextView debug_text_sid;
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
        debug_text_sid.setText(sid);

        SynologyAPIHelper helper = new SynologyAPIHelper();
        synologyApi = helper.getSynologyApi(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        debug_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext());
                String sid = prefs.getString(getString(R.string.pref_key_sid), "");
                String sid_header = prefs.getString(getString(R.string.pref_key_sid_header), "");

                switch (debug_spinner.getSelectedItemPosition()) {
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
                        new DSGetInfoTask(TaskList.this).execute(synologyApi.dsGetInfo(sid_header));
                        break;
                    case 4: //  DS Get Config
                        new DSGetConfigTask(TaskList.this).execute(synologyApi.dsGetConfig(sid_header));
                        break;
                    case 5: //  DS Set Config
                        new DSSetConfigTask(TaskList.this).execute(synologyApi.dsSetConfig(
                                sid_header, 0, 0, null, null, null, null, null, null, null));
                        break;
                    case 6: //  DS Task List
                        new DSTaskListTask(TaskList.this).execute(synologyApi.dsTaskList(sid_header));
                        break;
                    case 7: //  DS Task Get Info
                        new DSTaskInfoTask(TaskList.this).execute(synologyApi.dsTaskInfo(sid_header, "dbid_12"));
                        break;
                    case 8: //  DS Task Create URI
                    case 9: // DS Task Create File
                    case 10: // DS Task Delete
                    case 11: // DS Task Pause
                    case 12: // DS Task Resume
                    case 13: // DS Task Edit
                    case 14: // DS Stats Info
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
