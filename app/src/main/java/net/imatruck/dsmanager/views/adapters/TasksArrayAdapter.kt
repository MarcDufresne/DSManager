package net.imatruck.dsmanager.views.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import net.imatruck.dsmanager.R
import net.imatruck.dsmanager.models.Task
import net.imatruck.dsmanager.utils.BytesFormatter
import net.imatruck.dsmanager.utils.EtaFormatter
import net.imatruck.dsmanager.utils.PercentFormatter
import net.imatruck.dsmanager.utils.StatusFormatter
import java.util.*


class TasksArrayAdapter(private val context: Context, private val tasks: MutableList<Task>,
                        listener: TaskListOnClickListener) : RecyclerView.Adapter<TasksArrayAdapter.ViewHolder>() {

    init {
        TasksArrayAdapter.itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.tasks_list_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]

        val taskTitle = task.title
        val statusString = StatusFormatter.getStatusString(task.status)
        val status = if (statusString != -1) context.getString(statusString) else task.status
        val colorRes = StatusFormatter.getStatusColor(task.status)
        val color = ResourcesCompat.getColor(context.resources, colorRes, null)
        val size = task.size
        val speedDownload = task.additional!!.transfer!!.speedDownload
        val speedUpload = task.additional.transfer!!.speedUpload
        val sizeDownloaded = task.additional.transfer.sizeDownloaded
        val percent = PercentFormatter.toPercent(sizeDownloaded, size)

        holder.titleText.text = taskTitle
        holder.statusText.text = status
        holder.labelLeftText.text = BytesFormatter.humanReadable(size, false)
        holder.labelRightText.text = String.format(Locale.getDefault(),
                "↓ ${BytesFormatter.humanReadable(speedDownload, true)} - " +
                        "↑ ${BytesFormatter.humanReadable(speedUpload, true)} - " +
                        EtaFormatter.calculateEta(sizeDownloaded, size, speedDownload))
        holder.progressBar.progress = percent.toInt()
        holder.progressBar.progressTintList = ColorStateList.valueOf(color)
        holder.progressText.text = String.format(Locale.getDefault(), "%.0f%%", percent)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun getTask(position: Int): Task {
        return tasks[position]
    }

    fun clear() {
        tasks.clear()
    }

    fun addAll(newTasks: List<Task>) {
        tasks.addAll(newTasks)
    }

    val isEmpty: Boolean
        get() = tasks.isEmpty()

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var titleText: TextView = itemView.findViewById(R.id.row_task_title) as TextView
        internal var statusText: TextView = itemView.findViewById(R.id.row_task_status) as TextView
        internal var progressBar: ProgressBar = itemView.findViewById(R.id.row_task_progress_bar) as ProgressBar
        internal var progressText: TextView = itemView.findViewById(R.id.row_task_progress_text) as TextView
        internal var labelLeftText: TextView = itemView.findViewById(R.id.row_task_left_label) as TextView
        internal var labelRightText: TextView = itemView.findViewById(R.id.row_task_right_label) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemClickListener!!.onItemClick(this.layoutPosition)
        }
    }

    companion object {
        private var itemClickListener: TaskListOnClickListener? = null
    }
}
