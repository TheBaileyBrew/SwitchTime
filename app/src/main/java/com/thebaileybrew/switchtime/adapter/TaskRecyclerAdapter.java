package com.thebaileybrew.switchtime.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.thebaileybrew.switchtime.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Task> tasksList;

    final private TaskRecyclerClickHandler taskAdapterClickHandler;

    public interface TaskRecyclerClickHandler {
        void onClick(View view, Task task);
    }

    public TaskRecyclerAdapter(Context context, List<Task> tasksList, TaskRecyclerClickHandler clicker) {
        this.layoutInflater = LayoutInflater.from(context);
        this.tasksList = tasksList;
        this.taskAdapterClickHandler = clicker;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task currentTask = tasksList.get(position);

        holder.minuteValue.setText(currentTask.taskMinuteValue);
        holder.taskDescription.setText(currentTask.taskDescription);
    }

    @Override
    public int getItemCount() {
        if (tasksList == null) {
            return 0;
        } else {
            return tasksList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView minuteValue;
        final TextView taskDescription;

        private ViewHolder(View taskView) {
            super(taskView);
            minuteValue = taskView.findViewById(R.id.time_value);
            taskDescription = taskView.findViewById(R.id.time_description);
            taskView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Task currentTask = tasksList.get(getAdapterPosition());
            taskAdapterClickHandler.onClick(v, currentTask);
        }
    }


}
