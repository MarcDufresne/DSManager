package net.imatruck.dsmanager;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import net.imatruck.dsmanager.models.DSGetConfigBase;
import net.imatruck.dsmanager.models.DSGetConfigData;
import net.imatruck.dsmanager.models.DSSetConfigBase;
import net.imatruck.dsmanager.network.SynologyAPI;
import net.imatruck.dsmanager.network.SynologyAPIHelper;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class ServerConfigActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.config_edit_bt_dl)
    EditText editBtDl;
    @BindView(R.id.config_edit_bt_ul)
    EditText editBtUl;
    @BindView(R.id.config_edit_emule_dl)
    EditText editEmuleDl;
    @BindView(R.id.config_edit_emule_ul)
    EditText editEmuleUl;
    @BindView(R.id.config_edit_nzb_dl)
    EditText editNzbDl;
    @BindView(R.id.config_edit_http_dl)
    EditText editHttpDl;
    @BindView(R.id.config_edit_default_dest)
    EditText editDefaultDestination;
    @BindView(R.id.config_edit_emule_dest)
    EditText editEmuleDefaultDestination;

    SynologyAPI synologyAPI;
    String sidHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_config);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        synologyAPI = SynologyAPIHelper.getSynologyApi(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sidHeader = sharedPreferences.getString(getString(R.string.pref_key_sid_header), null);

        new GetServerConfigTask().execute(synologyAPI.dsGetConfig(sidHeader));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_server_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.action_save) {
            saveServerConfig();
        }

        return true;
    }

    private int getSpeedValue(EditText et) {
        String result = et.getText().toString();
        return (TextUtils.isEmpty(result) ? 0 : Integer.parseInt(result));
    }

    private void saveServerConfig() {
        int btDl = getSpeedValue(editBtDl);
        int btUl = getSpeedValue(editBtUl);
        int emuleDl = getSpeedValue(editEmuleDl);
        int emuleUl = getSpeedValue(editEmuleUl);
        int nzbDl = getSpeedValue(editNzbDl);
        int httpDl = getSpeedValue(editHttpDl);

        String defaultDestination = editDefaultDestination.getText().toString();
        String defaultEmuleDestination = editEmuleDefaultDestination.getText().toString();

        new SetServerConfigTask().execute(synologyAPI.dsSetConfig(
                sidHeader, btDl, btUl, defaultDestination, defaultEmuleDestination,
                emuleDl, emuleUl, httpDl, httpDl, nzbDl));
    }

    private class GetServerConfigTask extends AsyncTask<Call<DSGetConfigBase>, Void, DSGetConfigBase> {

        @SafeVarargs
        @Override
        protected final DSGetConfigBase doInBackground(Call<DSGetConfigBase>... calls) {
            DSGetConfigBase dsGetConfigBase;
            try {
                dsGetConfigBase = calls[0].execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return dsGetConfigBase;
        }

        @Override
        protected void onPostExecute(DSGetConfigBase dsGetConfigBase) {
            if (dsGetConfigBase != null) {
                if (dsGetConfigBase.isSuccess()) {
                    DSGetConfigData data = dsGetConfigBase.getData();
                    editBtDl.setText(String.valueOf(data.getBtMaxDownload()));
                    editBtUl.setText(String.valueOf(data.getBtMaxUpload()));
                    editEmuleDl.setText(String.valueOf(data.getEmuleMaxDownload()));
                    editEmuleUl.setText(String.valueOf(data.getEmuleMaxUpload()));
                    editNzbDl.setText(String.valueOf(data.getNzbMaxDownload()));
                    editHttpDl.setText(String.valueOf(data.getHttpMaxDownload()));
                    editDefaultDestination.setText(data.getDefaultDestination());
                    editEmuleDefaultDestination.setText(data.getEmuleDefaultDestination());
                } else {
                    Toast.makeText(ServerConfigActivity.this, R.string.config_get_fail,
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                Toast.makeText(ServerConfigActivity.this, R.string.config_get_fail_network,
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private class SetServerConfigTask extends AsyncTask<Call<DSSetConfigBase>, Void, DSSetConfigBase> {

        @SafeVarargs
        @Override
        protected final DSSetConfigBase doInBackground(Call<DSSetConfigBase>... calls) {
            DSSetConfigBase dsSetConfigBase;
            try {
                dsSetConfigBase = calls[0].execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return dsSetConfigBase;
        }

        @Override
        protected void onPostExecute(DSSetConfigBase dsSetConfigBase) {
            if (dsSetConfigBase != null) {
                if (dsSetConfigBase.isSuccess()) {
                    Toast.makeText(ServerConfigActivity.this, R.string.config_set_success,
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ServerConfigActivity.this, R.string.config_set_fail,
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ServerConfigActivity.this, R.string.config_set_fail_network,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
