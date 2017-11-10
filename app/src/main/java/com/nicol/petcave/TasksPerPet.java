package com.nicol.petcave;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nicol on 11/5/2017.
 */

public class TasksPerPet extends Activity {
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
        readTasksPerPet(petId);
    }

    public void countTasks(int petId) {
        int tasksCount = new TableControllerTasks(this).count(petId);
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewNoOfTasks);
        textViewRecordCount.setText(tasksCount + " records found.");
    }

    public void readTasksPerPet(int petId) {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutTasks);
        linearLayoutRecords.removeAllViews();

        List<ObjectTask> tasks = new TableControllerTasks(this).read(petId);

        if (tasks.size() > 0) {

            for (ObjectTask obj : tasks) {

                int id = obj.id;
                String description = obj.description;

                String textViewContents = description;

                TextView textViewPetItem= new TextView(this);
                textViewPetItem.setPadding(0, 10, 0, 10);
                textViewPetItem.setText(textViewContents);
                textViewPetItem.setTag(Integer.toString(id));
                textViewPetItem.setOnLongClickListener(new OnLongClickListenerTask(this, petId));
                linearLayoutRecords.addView(textViewPetItem);
            }

        }

        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No tasks yet.");

            linearLayoutRecords.addView(locationItem);
        }

    }
}
