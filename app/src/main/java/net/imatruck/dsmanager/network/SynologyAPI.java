package net.imatruck.dsmanager.network;

import net.imatruck.dsmanager.models.APIInfoBase;
import net.imatruck.dsmanager.models.AuthLoginBase;
import net.imatruck.dsmanager.models.AuthLogoutBase;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
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
    Call<APIInfoBase> getApiInfo();

    @GET("auth.cgi?" +
            "api=SYNO.API.Auth&" +
            "version=2&" +
            "session=android-session&" +
            "format=cookie&" +
            "method=login")
    Call<AuthLoginBase> login(@Query("account") String account, @Query("passwd") String password);

    @GET("auth.cgi?" +
            "api=SYNO.API.Auth&" +
            "version=2&" +
            "session=android-session&" +
            "method=logout")
    Call<AuthLogoutBase> logout(@Query("sid") String sid);

}
