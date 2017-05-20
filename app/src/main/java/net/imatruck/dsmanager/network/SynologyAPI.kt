package net.imatruck.dsmanager.network

import net.imatruck.dsmanager.models.APIInfoBase
import net.imatruck.dsmanager.models.AuthLoginBase
import net.imatruck.dsmanager.models.AuthLogoutBase
import net.imatruck.dsmanager.models.DSGetConfigBase
import net.imatruck.dsmanager.models.DSGetInfoBase
import net.imatruck.dsmanager.models.DSSetConfigBase
import net.imatruck.dsmanager.models.DSStatsInfoBase
import net.imatruck.dsmanager.models.DSTaskCreateBase
import net.imatruck.dsmanager.models.DSTaskDeleteBase
import net.imatruck.dsmanager.models.DSTaskEditBase
import net.imatruck.dsmanager.models.DSTaskInfoBase
import net.imatruck.dsmanager.models.DSTaskListBase
import net.imatruck.dsmanager.models.DSTaskPauseBase
import net.imatruck.dsmanager.models.DSTaskResumeBase

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

/**
 * Synology API wrapper
 */

interface SynologyAPI {

    @GET("query.cgi?" +
            "api=SYNO.API.Info&" +
            "version=1&" +
            "method=query&" +
            "query=SYNO.API.Auth,SYNO.DownloadStation.Task,SYNO.DownloadStation.Statistics")
    fun apiGetInfo(): Call<APIInfoBase>

    @GET("auth.cgi?" +
            "api=SYNO.API.Auth&" +
            "version=2&" +
            "session=android-session&" +
            "format=sid&" +
            "method=login")
    fun authLogin(@Query("account") account: String, @Query("passwd") password: String): Call<AuthLoginBase>

    @GET("auth.cgi?" +
            "api=SYNO.API.Auth&" +
            "version=2&" +
            "session=android-session&" +
            "method=logout")
    fun authLogout(@Query("sid") sid: String): Call<AuthLogoutBase>

    @GET("DownloadStation/info.cgi?" +
            "api=SYNO.DownloadStation.Info&" +
            "version=2&" +
            "method=getinfo")
    fun dsGetInfo(@Header("Cookie") sid: String): Call<DSGetInfoBase>

    @GET("DownloadStation/info.cgi?" +
            "api=SYNO.DownloadStation.Info&" +
            "version=2&" +
            "method=getconfig")
    fun dsGetConfig(@Header("Cookie") sid: String): Call<DSGetConfigBase>

    @GET("DownloadStation/info.cgi?" +
            "api=SYNO.DownloadStation.Info&" +
            "version=2&" +
            "method=setserverconfig")
    fun dsSetConfig(@Header("Cookie") sid: String,
                    @Query("bt_max_download") btMaxDownload: Int?,
                    @Query("bt_max_upload") btMaxUpload: Int?,
                    @Query("default_destination") defaultDestination: String,
                    @Query("emule_default_destination") emuleDefaultDestination: String,
                    @Query("emule_max_download") emuleMaxDownload: Int?,
                    @Query("emule_max_upload") emuleMaxUpload: Int?,
                    @Query("ftp_max_download") ftpMaxDownload: Int?,
                    @Query("http_max_download") httpMaxDownload: Int?,
                    @Query("nzb_max_download") nzbMaxDownload: Int?): Call<DSSetConfigBase>

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=list&" +
            "additional=transfer")
    fun dsTaskList(@Header("Cookie") sid: String): Call<DSTaskListBase>

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=getinfo&" +
            "additional=detail,file,tracker,transfer")
    fun dsTaskInfo(@Header("Cookie") sid: String,
                   @Query("id") id: String): Call<DSTaskInfoBase>

    @FormUrlEncoded
    @POST("DownloadStation/task.cgi")
    fun dsTaskCreateUri(@FieldMap fields: Map<String, String>): Call<DSTaskCreateBase>

    @Multipart
    @POST("DownloadStation/task.cgi")
    fun dsTaskCreateFile(@Header("cookie") sid: String,
                         @PartMap parts: Map<String, RequestBody>,
                         @Part file: MultipartBody.Part): Call<DSTaskCreateBase>

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=delete")
    fun dsTaskDelete(@Header("Cookie") sid: String,
                     @Query("id") id: String): Call<DSTaskDeleteBase>

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=pause")
    fun dsTaskPause(@Header("Cookie") sid: String,
                    @Query("id") id: String): Call<DSTaskPauseBase>

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=resume")
    fun dsTaskResume(@Header("Cookie") sid: String,
                     @Query("id") id: String): Call<DSTaskResumeBase>

    @GET("DownloadStation/task.cgi?" +
            "api=SYNO.DownloadStation.Task&" +
            "version=2&" +
            "method=edit")
    fun dsTaskEdit(@Header("Cookie") sid: String,
                   @Query("id") id: String,
                   @Query("destination") destination: String): Call<DSTaskEditBase>

    @GET("DownloadStation/statistic.cgi?" +
            "api=SYNO.DownloadStation.Statistic&" +
            "version=1&" +
            "method=getinfo")
    fun dsStatsInfo(@Header("Cookie") sid: String): Call<DSStatsInfoBase>
}
