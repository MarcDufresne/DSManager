package net.imatruck.dsmanager.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.imatruck.dsmanager.R;
import net.imatruck.dsmanager.models.Task;
import net.imatruck.dsmanager.utils.BytesFormatter;
import net.imatruck.dsmanager.utils.EtaFormatter;
import net.imatruck.dsmanager.utils.PercentFormatter;
import net.imatruck.dsmanager.utils.StatusFormatter;

import java.util.List;
import java.util.Locale;


public class TasksArrayAdapter extends RecyclerView.Adapter<TasksArrayAdapter.ViewHolder> {

    private Context context;
    private List<Task> tasks;
    private static TaskListOnClickListener itemClickListener;

    public TasksArrayAdapter(@NonNull Context context, @NonNull List<Task> tasks,
                             TaskListOnClickListener listener) {
        this.context = context;
        this.tasks = tasks;
        TasksArrayAdapter.itemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.tasks_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = tasks.get(position);

        if (task == null) {
            return;
        }

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
                String.format(Locale.getDefault(), "↓ %s - ↑ %s - %s",
                        BytesFormatter.humanReadable(speedDownload, true),
                        BytesFormatter.humanReadable(speedUpload, true),
                        EtaFormatter.calculateEta(sizeDownloaded, size, speedDownload))
        );
        holder.progressBar.setProgress((int) percent);
        holder.progressBar.setProgressTintList(ColorStateList.valueOf(color));
        holder.progressText.setText(String.format(Locale.getDefault(), "%.0f%%", percent));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public Task getTask(int position) {
        return tasks.get(position);
    }

    public void clear() {
        tasks.clear();
    }

    public void addAll(List<Task> newTasks) {
        tasks.addAll(newTasks);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleText;
        TextView statusText;
        ProgressBar progressBar;
        TextView progressText;
        TextView labelLeftText;

        TextView labelRightText;
        ViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.row_task_title);
            statusText = (TextView) itemView.findViewById(R.id.row_task_status);
            progressBar = (ProgressBar) itemView.findViewById(R.id.row_task_progress_bar);
            progressText = (TextView) itemView.findViewById(R.id.row_task_progress_text);
            labelLeftText = (TextView) itemView.findViewById(R.id.row_task_left_label);
            labelRightText = (TextView) itemView.findViewById(R.id.row_task_right_label);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(this.getLayoutPosition());
        }
    }
}
