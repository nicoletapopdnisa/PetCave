package com.nicol.petcave_final;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by nicol on 1/2/2018.
 */

public class TasksPerPet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_per_pet);

        Intent intent = getIntent();
        String petName = intent.getStringExtra("petName");
        String petId = intent.getStringExtra("petId");

        TextView petNameTextView = (TextView) findViewById(R.id.textViewTasksForPetName);
        petNameTextView.setText(petName + " TO-DOs ");

        Button buttonCreateTask = (Button) findViewById(R.id.buttonCreateNewTask);
        buttonCreateTask.setOnClickListener(new OnClickListenerCreateTask(this, petId, petName));


        readTasksPerPet(petId);
    }

    public void readTasksPerPet(final String petId)
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasksRef = rootRef.child("tasks");
        final ArrayList<Task> tasks = new ArrayList<>();
        final int count[] = {0};

        tasksRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                tasks.clear();
                count[0] = 0;
                while(iterator.hasNext())
                {
                    Task value = iterator.next().getValue(Task.class);
                    if(Objects.equals(value.petId, petId))
                    {
                        tasks.add(value);
                        count[0]++;
                        ((CustomAdapterTasks) (((ListView)findViewById(R.id.tasksListView)).getAdapter())).notifyDataSetChanged();
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(count[0]);
                stringBuilder.append(" tasks found.");
                ((TextView) findViewById(R.id.textViewNoOfTasks)).setText(stringBuilder.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((ListView)findViewById(R.id.tasksListView)).setAdapter(new CustomAdapterTasks(tasks, this, this));

    }
}
