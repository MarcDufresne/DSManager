package net.imatruck.dsmanager;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import net.imatruck.dsmanager.models.DSTaskCreateBase;
import net.imatruck.dsmanager.models.RequestDSTaskCreate;
import net.imatruck.dsmanager.network.SynologyAPI;
import net.imatruck.dsmanager.network.SynologyAPIHelper;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class AddTaskActivity extends AppCompatActivity {

    @BindView(R.id.radio_uri)
    RadioButton radioUri;
    @BindView(R.id.input_uri)
    EditText editInputUri;
    @BindView(R.id.input_destination)
    EditText editInputDestination;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String sid;
    String defaultDestination;
    SynologyAPI synologyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        defaultDestination = prefs.getString(getString(R.string.pref_key_default_destination), null);
        editInputDestination.setText(defaultDestination);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.action_add) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            sid = prefs.getString(getString(R.string.pref_key_sid), null);
            defaultDestination = prefs.getString(getString(R.string.pref_key_default_destination), null);

            synologyApi = SynologyAPIHelper.getSynologyApi(this);

            Map<String, String> request = createRequest();

            new AddNewTask().execute(synologyApi.dsTaskCreate(request));
        }

        return super.onOptionsItemSelected(item);
    }

    private Map<String, String> createRequest() {
        String uri = editInputUri.getText().toString();
        String destination = editInputDestination.getText().toString();

        if (destination.isEmpty()) {
            destination = defaultDestination;
        }

        return RequestDSTaskCreate.getCreateWithUriDestinationMap(
                sid, uri, destination);

    }

    private class AddNewTask extends AsyncTask<Call<DSTaskCreateBase>, Void, DSTaskCreateBase> {

        @SafeVarargs
        @Override
        protected final DSTaskCreateBase doInBackground(Call<DSTaskCreateBase>... calls) {
            DSTaskCreateBase dsTaskCreateBase;
            try {
                dsTaskCreateBase = calls[0].execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return dsTaskCreateBase;
        }

        @Override
        protected void onPostExecute(DSTaskCreateBase dsTaskCreateBase) {
            if (dsTaskCreateBase != null) {
                if (dsTaskCreateBase.isSuccess()) {
                    Toast.makeText(AddTaskActivity.this, getString(R.string.toast_task_created),
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String text = getString(
                            SynologyDSTaskError.getMessageId(dsTaskCreateBase.getError().getCode()));
                    Snackbar.make(toolbar, text, Snackbar.LENGTH_LONG).show();
                }
            } else {
                String text = getString(R.string.synapi_error_1);
                Snackbar.make(toolbar, text, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
