package net.imatruck.dsmanager.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.Task;
import net.imatruck.dsmanager.utils.BytesFormatter;
import net.imatruck.dsmanager.utils.EtaFormatter;
import net.imatruck.dsmanager.utils.PercentFormatter;
import net.imatruck.dsmanager.utils.StatusFormatter;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by marc on 2017-03-26.
 */

public class TasksArrayAdapter extends ArrayAdapter {

    Context context;
    List<Task> tasks;

    static class ViewHolder {
        TextView titleText;
        TextView statusText;
        ProgressBar progressBar;
        TextView progressText;
        TextView labelLeftText;
        TextView labelRightText;
    }

    public TasksArrayAdapter(@NonNull Context context, @NonNull List<Task> tasks) {
        super(context, -1, tasks);
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater.from(context));
            convertView = inflater.inflate(R.layout.tasks_list_row, parent, false);

            holder = new ViewHolder();
            holder.titleText = (TextView) convertView.findViewById(R.id.row_task_title);
            holder.statusText = (TextView) convertView.findViewById(R.id.row_task_status);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.row_task_progress_bar);
            holder.progressText = (TextView) convertView.findViewById(R.id.row_task_progress_text);
            holder.labelLeftText = (TextView) convertView.findViewById(R.id.row_task_left_label);
            holder.labelRightText = (TextView) convertView.findViewById(R.id.row_task_right_label);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Task task = tasks.get(position);

        if (task != null) {
            String taskTitle = task.getTitle();
            int statusString = StatusFormatter.getStatusString(task.getStatus());
            String status = statusString != -1 ? context.getString(statusString) : task.getStatus();
            int colorRes = StatusFormatter.getStatusColor(task.getStatus());
            int color = ResourcesCompat.getColor(context.getResources(), colorRes, null);
            double size = task.getSize();
            double speedDownload = task.getAdditional().getTransfer().getSpeedDownload();
            double speedUpload = task.getAdditional().getTransfer().getSpeedUpload();
            double sizeDownloaded = task.getAdditional().getTransfer().getSizeDownloaded();
            double percent = PercentFormatter.toPercent(sizeDownloaded, size);

            holder.titleText.setText(taskTitle);
            holder.statusText.setText(status);
            holder.labelLeftText.setText(BytesFormatter.humanReadable(size, false));
            holder.labelRightText.setText(
                    String.format(Locale.getDefault(), "DL: %s - UL: %s - ETA: %s",
                            BytesFormatter.humanReadable(speedDownload, true),
                            BytesFormatter.humanReadable(speedUpload, true),
                            EtaFormatter.calculateEta(sizeDownloaded, size, speedDownload))
            );
            holder.progressBar.setProgress((int) percent);
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(color));
            holder.progressText.setText(String.format(Locale.getDefault(), "%.0f%%", percent));
        }

        return convertView;
    }
}
