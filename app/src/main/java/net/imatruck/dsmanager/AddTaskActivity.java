package net.imatruck.dsmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.imatruck.dsmanager.models.DSTaskCreateBase;
import net.imatruck.dsmanager.models.RequestDSTaskCreate;
import net.imatruck.dsmanager.network.SynologyAPI;
import net.imatruck.dsmanager.network.SynologyAPIHelper;
import net.imatruck.dsmanager.utils.FileUtils;
import net.imatruck.dsmanager.utils.SynologyDSTaskError;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AddTaskActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.radio_group_type)
    RadioGroup radioGroup;
    @BindView(R.id.radio_uri)
    RadioButton radioUri;
    @BindView(R.id.radio_file)
    RadioButton radioFile;
    @BindView(R.id.input_uri)
    EditText editInputUri;
    @BindView(R.id.input_destination)
    EditText editInputDestination;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String sid;
    String sidHeader;
    String defaultDestination;
    SynologyAPI synologyApi;

    Uri fileUri;

    private final static int READ_REQUEST_CODE = 90;
    private final static int PERMISSION_CHECK_REQUEST_CODE = 65;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        defaultDestination = prefs.getString(getString(R.string.pref_key_default_destination), null);
        editInputDestination.setText(defaultDestination);

        radioGroup.setOnCheckedChangeListener(this);
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
            sidHeader = prefs.getString(getString(R.string.pref_key_sid_header), null);
            defaultDestination = prefs.getString(getString(R.string.pref_key_default_destination), null);

            synologyApi = SynologyAPIHelper.getSynologyApi(this);

            if (radioUri.isChecked() && !editInputUri.getText().toString().isEmpty()) {
                Map<String, String> request = createUriRequest();
                new AddNewTask().execute(synologyApi.dsTaskCreateUri(request));
            } else if (radioFile.isChecked() && fileUri != null) {
                Map<String, RequestBody> request = createFileRequest();
                MultipartBody.Part file = prepareFile();
                new AddNewTask().execute(synologyApi.dsTaskCreateFile(sidHeader, request, file));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private Map<String, String> createUriRequest() {
        String uri = editInputUri.getText().toString();
        String destination = editInputDestination.getText().toString();

        if (destination.isEmpty()) {
            destination = defaultDestination;
        }

        return RequestDSTaskCreate.getCreateWithUriDestinationMap(
                sid, uri, destination);

    }

    private Map<String, RequestBody> createFileRequest() {
        String destination = editInputDestination.getText().toString();

        if (destination.isEmpty()) {
            destination = defaultDestination;
        }

        return RequestDSTaskCreate.getCreateWithFileDestination(destination);

    }

    private MultipartBody.Part prepareFile() {
        File file = FileUtils.getFile(this, fileUri);

        RequestBody requestFile = RequestBody.create(
                MediaType.parse(getContentResolver().getType(fileUri)),
                file);

        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.radio_file) {
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                startFilePickerActivity();
            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_CHECK_REQUEST_CODE);
            }

        }
    }

    private void startFilePickerActivity() {
        Intent filePicker = FileUtils.createGetContentIntent("application/x-bittorrent");
        startActivityForResult(filePicker, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (data != null) {
                uri = data.getData();
                fileUri = uri;

                String fileDisplayName = getFileDisplayName(uri);
                editInputUri.setText(fileDisplayName);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CHECK_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startFilePickerActivity();
                } else {
                    Snackbar.make(toolbar, "You cannot upload file without read permission.",
                            Snackbar.LENGTH_LONG).show();
                }
        }
    }

    private String getFileDisplayName(Uri uri) {
        String displayName = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            displayName = cursor.getString(
                    cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            cursor.close();
        }
        return displayName;
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
