package net.imatruck.dsmanager.utils;

import net.imatruck.dsmanager.R;

/**
 * Created by marc on 2017-03-26.
 */

public class StatusFormatter {

    public static int getStatusString(String status) {

        switch (status) {
            case "waiting":
                return R.string.synapi_task_status_waiting;
            case "downloading":
                return R.string.synapi_task_status_downloading;
            case "paused":
                return R.string.synapi_task_status_paused;
            case "finishing":
                return R.string.synapi_task_status_finishing;
            case "finished":
                return R.string.synapi_task_status_finished;
            case "hash_checking":
                return R.string.synapi_task_status_hash_checking;
            case "seeding":
                return R.string.synapi_task_status_seeding;
            case "filehosting_waiting":
                return R.string.synapi_task_status_filehosting_waiting;
            case "extracting":
                return R.string.synapi_task_status_extracting;
            case "error":
                return R.string.synapi_task_status_error;
            default:
                return -1;

        }

    }

}
