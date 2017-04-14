package net.imatruck.dsmanager.utils;

import net.imatruck.dsmanager.R;


public class StatusFormatter {

    /**
     * Converts DownloadStation API status to resource string ID
     * @param status synology API status string
     * @return resource string ID for corresponding status
     */
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

    /**
     * Converts DownloadStation API status to a resource color ID
     * @param status synology API status string
     * @return resource color ID
     */
    public static int getStatusColor(String status) {

        switch (status) {
            case "waiting":
                return R.color.colorStatusWaiting;
            case "downloading":
                return R.color.colorAccent;
            case "paused":
                return R.color.colorStatusPaused;
            case "finishing":
                return R.color.colorStatusDone;
            case "finished":
                return R.color.colorStatusDone;
            case "hash_checking":
                return R.color.colorStatusWaiting;
            case "seeding":
                return R.color.colorStatusSeeding;
            case "filehosting_waiting":
                return R.color.colorStatusWaiting;
            case "extracting":
                return R.color.colorStatusWaiting;
            case "error":
                return R.color.colorStatusError;
            default:
                return R.color.colorAccent;
        }

    }

}
