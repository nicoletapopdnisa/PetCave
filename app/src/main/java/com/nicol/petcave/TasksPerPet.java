package com.nicol.petcave;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 11/5/2017.
 */

public class TasksPerPet extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_per_pet);

        Intent intent = getIntent();
        String petName = intent.getStringExtra("petName");
        int petId = intent.getIntExtra("petId", 0);

        TextView petNameTextView = (TextView) findViewById(R.id.textViewTasksForPetName);
        petNameTextView.setText(petName + " TO-DOs ");

        Button buttonCreateTask = (Button) findViewById(R.id.buttonCreateNewTask);
        buttonCreateTask.setOnClickListener(new OnClickListenerCreateTask(this, petId, petName));

        countTasks(petId);

        final ListView taskList = findViewById(R.id.tasksListView);

        List<ObjectTask> tasks = new TableControllerTasks(this).read(petId);
        final ArrayList<ObjectTask> array = new ArrayList<>(tasks);

        CustomAdapterTasks adapter = new CustomAdapterTasks(array, this, this);
        taskList.setAdapter(adapter);
    }

    public void countTasks(int petId) {
        int tasksCount = new TableControllerTasks(this).count(petId);
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewNoOfTasks);
        textViewRecordCount.setText(tasksCount + " records found.");
    }

    public void readTasksPerPet(int petId) {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutTasks);

        List<ObjectTask> tasks = new TableControllerTasks(this).read(petId);

        if (tasks.size() > 0) {

            final ListView taskList = (ListView) findViewById(R.id.tasksListView);
            final ArrayList<ObjectTask> array = new ArrayList<ObjectTask>(tasks);
            ((CustomAdapterTasks) taskList.getAdapter()).setList(array);
            ((BaseAdapter) taskList.getAdapter()).notifyDataSetChanged();
        }

        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No tasks yet.");

            linearLayoutRecords.addView(locationItem);
        }

    }
}
