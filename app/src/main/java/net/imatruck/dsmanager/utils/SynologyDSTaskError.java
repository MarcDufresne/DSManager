package net.imatruck.dsmanager.utils;

import net.imatruck.dsmanager.R;


public class SynologyDSTaskError extends SynologyBaseError {

    /**
     * Converts DownloadStation API error codes into resource string ID
     * @param code synology API error code
     * @return resource string ID of the corresponding error
     */
    public static int getMessageId(int code) {
        switch (code) {
            case 400:
                return R.string.synapi_dstask_error_400;
            case 401:
                return R.string.synapi_dstask_error_401;
            case 402:
                return R.string.synapi_dstask_error_402;
            case 403:
                return R.string.synapi_dstask_error_403;
            case 404:
                return R.string.synapi_dstask_error_404;
            case 405:
                return R.string.synapi_dstask_error_405;
            case 406:
                return R.string.synapi_dstask_error_406;
            case 407:
                return R.string.synapi_dstask_error_407;
            case 408:
                return R.string.synapi_dstask_error_408;
            case 544:
                return R.string.synapi_dstask_error_544;
            default:
                return SynologyBaseError.getMessageId(code);
        }
    }
}
