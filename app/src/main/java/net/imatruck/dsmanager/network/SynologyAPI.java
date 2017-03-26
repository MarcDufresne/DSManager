package net.imatruck.dsmanager.network;

import net.imatruck.dsmanager.models.APIInfoBase;
import net.imatruck.dsmanager.models.AuthLoginBase;
import net.imatruck.dsmanager.models.AuthLogoutBase;
import net.imatruck.dsmanager.models.DSGetConfigBase;
import net.imatruck.dsmanager.models.DSGetInfoBase;
import net.imatruck.dsmanager.models.DSSetConfigBase;
import net.imatruck.dsmanager.models.DSTaskCreateBase;
import net.imatruck.dsmanager.models.DSTaskDeleteBase;
import net.imatruck.dsmanager.models.DSTaskEditBase;
import net.imatruck.dsmanager.models.DSTaskInfoBase;
import net.imatruck.dsmanager.models.DSTaskListBase;
import net.imatruck.dsmanager.models.DSTaskPauseBase;
import net.imatruck.dsmanager.models.DSTaskResumeBase;
import net.imatruck.dsmanager.models.RequestDSTaskCreate;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Synology API wrapper
 */

public interface SynologyAPI {

    @GET("query.cgi?" +
            "api=SYNO.API.Info&" +
            "version=1&" +
            "method=query&" +
            "query=SYNO.API.Auth,SYNO.DownloadStation.Task,SYNO.DownloadStation.Statistics")
    Call<APIInfoBase> apiGetInfo();

    @GET("auth.cgi?" +
            "api=SYNO.API.Auth&" +
            "version=2&" +
            "session=android-session&" +
            "format=sid&" +
            "method=login")
    Call<AuthLoginBase> authLogin(@Query("account") String account, @Query("passwd") String password);

    @GET("auth.cgi?" +
            "api=SYNO.API.Auth&" +
            "version=2&" +
            "session=android-session&" +
            "method=logout")
    Call<AuthLogoutBase> authLogout(@Query("sid") String sid);

    @GET("DownloadStation/info.cgi?" +
            "api=SYNO.DownloadStation.Info&" +
            "version=2&" +
            "method=getinfo")
    Call<DSGetInfoBase> dsGetInfo(@Header("Cookie") String sid);

    @GET("DownloadStation/info.cgi?" +
            "api=SYNO.DownloadStation.Info&" +
            "version=2&" +
            "method=getconfig")
    Call<DSGetConfigBase> dsGetConfig(@Header("Cookie") String sid);

    @GET("DownloadStation/info.cgi?" +
            "api=SYNO.DownloadStation.Info&" +
            "version=2&" +
            "method=setserverconfig")
    Call<DSSetConfigBase> dsSetConfig(@Header("Cookie") String sid,
        @Query("bt_max_download") Integer btMaxDownload,
        @Query("bt_max_upload") Integer btMaxUpload,
        @Query("default_destination") String defaultDestination,
        @Query("emule_default_destination") String emuleDefaultDestination,
        @Query("emule_max_download") Integer emuleMaxDownload,
        @Query("emule_max_upload") Integer emuleMaxUpload,
        @Query("ftp_max_download") Integer ftpMaxDownload,
        @Query("http_max_download") Integer httpMaxDownload,
        @Query("nzb_max_download") Integer nzbMaxDownload);

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=list&" +
            "additional=transfer")
    Call<DSTaskListBase> dsTaskList(@Header("Cookie") String sid);

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=getinfo&" +
            "additional=detail,file,tracker,transfer")
    Call<DSTaskInfoBase> dsTaskInfo(@Header("Cookie") String sid,
        @Query("id") String id);

    @FormUrlEncoded
    @POST("DownloadStation/task.cgi")
    Call<DSTaskCreateBase> dsTaskCreate(@FieldMap Map<String, String> fields);

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=delete")
    Call<DSTaskDeleteBase> dsTaskDelete(@Header("Cookie") String sid,
        @Query("id") String id);

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=pause")
    Call<DSTaskPauseBase> dsTaskPause(@Header("Cookie") String sid,
        @Query("id") String id);

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=resume")
    Call<DSTaskResumeBase> dsTaskResume(@Header("Cookie") String sid,
        @Query("id") String id);

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=edit")
    Call<DSTaskEditBase> dsTaskEdit(@Header("Cookie") String sid,
        @Query("id") String id,
        @Query("destination") String destination);
}
