package com.example.krnekekaam;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskRecyclerViewAdapter.TaskClickListener {

    private List<Task> list;
    private TaskRecyclerViewAdapter taskRecyclerViewAdapter;
    private SharedPreferences sharedPreferences;
    private static final String TASKS_KEY = "tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("com.example.krnekekaam", Context.MODE_PRIVATE);
        list = loadTasks();
        taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(list, this);

        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskRecyclerViewAdapter);

        FloatingActionButton fab = findViewById(R.id.NewTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    showInputDialog();
                }
        });
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a Task");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                Task task = new Task(userInput);
                taskRecyclerViewAdapter.addTask(task);
                saveTasks();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();

        String taskString = sharedPreferences.getString(TASKS_KEY, null);
        if (taskString != null) {
            try {
                JSONArray jsonArray = new JSONArray(taskString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String dsc = object.getString("dsc");
                    tasks.add(new Task(dsc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tasks;
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();

        for (Task task : list) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("dsc", task.getTASK());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        editor.putString(TASKS_KEY, jsonArray.toString());
        editor.apply();
    }

    @Override
    public void onTaskChecked(int position) {
        // Handle task checked event
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_delete);
        builder.setTitle("Delete Task!");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               list.remove(position);
               taskRecyclerViewAdapter.notifyItemRemoved(position);
               saveTasks();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void onEditButtonClick(int posititon){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_input_delete);
        builder.setTitle("Edit Task");

        String prepopulatedtask = taskRecyclerViewAdapter.taskList.get(posititon).getTASK();

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(prepopulatedtask);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Task tsk = new Task(input.getText().toString());
                taskRecyclerViewAdapter.taskList.set(posititon,tsk);
                taskRecyclerViewAdapter.notifyItemChanged(posititon);
                saveTasks();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
