package net.imatruck.dsmanager.network;

import net.imatruck.dsmanager.models.APIInfoBase;
import net.imatruck.dsmanager.models.AuthLoginBase;
import net.imatruck.dsmanager.models.AuthLogoutBase;
import net.imatruck.dsmanager.models.DSGetInfoBase;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
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

}
