package net.imatruck.dsmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import net.imatruck.dsmanager.tasks.AuthLoginTask;
import net.imatruck.dsmanager.tasks.ApiInfoTask;
import net.imatruck.dsmanager.network.SynologyAPI;
import net.imatruck.dsmanager.network.SynologyAPIHelper;
import net.imatruck.dsmanager.tasks.AuthLogoutTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskList extends AppCompatActivity {
    @BindView(R.id.debug_api_button) Button debug_button;
    @BindView(R.id.debug_api_spinner) Spinner debug_spinner;
    @BindView(R.id.debug_api_text) TextView debug_text;
    @BindView(R.id.debug_api_sid) TextView debug_text_sid;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;

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

        SharedPreferences prefs = TaskList.this.getSharedPreferences(
                getString(R.string.pref_key_shared_file), Context.MODE_PRIVATE);
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
                SharedPreferences prefs = TaskList.this.getSharedPreferences(
                        getString(R.string.pref_key_shared_file), Context.MODE_PRIVATE);
                String sid = prefs.getString(getString(R.string.pref_key_sid), "");

                switch (debug_spinner.getSelectedItemPosition()) {
                    case 0: // API Info
                        new ApiInfoTask(TaskList.this).execute(synologyApi.getApiInfo());
                        break;
                    case 1: //  Auth Login
                        String account = prefs.getString(getString(R.string.pref_key_account), "");
                        String password = prefs.getString(getString(R.string.pref_key_password), "");
                        new AuthLoginTask(TaskList.this).execute(synologyApi.login(account, password));
                        break;
                    case 2: //  Auth Logout
                        new AuthLogoutTask(TaskList.this).execute(synologyApi.logout(sid));
                        break;
                    case 4: //  DS Get Info
                    case 5: //  DS Get Config
                    case 6: //  DS Set Config
                    case 7: //  DS Task List
                    case 8: //  DS Task Get Info
                    case 9: //  DS Task Create URI
                    case 10: // DS Task Create File
                    case 11: // DS Task Delete
                    case 12: // DS Task Pause
                    case 13: // DS Task Resume
                    case 14: // DS Task Edit
                    case 15: // DS Stats Info
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
