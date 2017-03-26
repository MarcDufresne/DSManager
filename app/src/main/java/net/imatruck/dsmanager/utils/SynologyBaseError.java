package net.imatruck.dsmanager.utils;

import net.imatruck.dsmanager.R;

/**
 * Created by marc on 2017-03-26.
 */

public class SynologyBaseError {

    public static int getMessageId(int code) {
        switch (code) {
            case 0:
                return R.string.synapi_error_0;
            case 100:
                return R.string.synapi_error_100;
            case 101:
                return R.string.synapi_error_101;
            case 102:
                return R.string.synapi_error_102;
            case 103:
                return R.string.synapi_error_103;
            case 104:
                return R.string.synapi_error_104;
            case 105:
                return R.string.synapi_error_105;
            case 106:
                return R.string.synapi_error_106;
            case 107:
                return R.string.synapi_error_107;
            default:
                return R.string.synapi_error_100;
        }
    }

}
