package com.example.krnekekaam;

import static android.widget.Toast.LENGTH_LONG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.MyViewHolder> {

    public List<Task> taskList;
    public interface TaskClickListener {
        void onTaskChecked(int position);
        void onEditButtonClick(int position);
    }

    private TaskClickListener listener;

    public TaskRecyclerViewAdapter(List<Task> taskList , TaskClickListener listener){
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewAdapter.MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.task.setText(task.getTASK());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void addTask(Task task) {

        taskList.add(task);
        notifyItemInserted(taskList.size() - 1);
    }

    public void removeTask(int position){
        taskList.remove(position);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView task;
        Button editTask;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView , TaskClickListener listener) {
            super(itemView);

            task = itemView.findViewById(R.id.textView);
            editTask = itemView.findViewById(R.id.button);
            checkBox = itemView.findViewById(R.id.checkBox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null && checkBox.isChecked()){
                        listener.onTaskChecked(getAdapterPosition());
                    }
                }
            });

            editTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!= null){
                        listener.onEditButtonClick(getAdapterPosition());
                    }
                }
            });
        }

    }



}
