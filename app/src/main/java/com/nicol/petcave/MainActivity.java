package com.nicol.petcave;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonCreatePet = (Button) findViewById(R.id.buttonCreatePet);
        buttonCreatePet.setOnClickListener(new OnClickListenerCreatePet(this));
        countPets();
        readPets();
    }

    public void countPets() {
        int petsCount = new TableControllerPet(this).count();
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewPetCount);
        textViewRecordCount.setText(petsCount + " records found.");
    }

    public void readPets() {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);
        linearLayoutRecords.removeAllViews();

        List<ObjectPet> pets = new TableControllerPet(this).read();

        if (pets.size() > 0) {

            for (ObjectPet obj : pets) {

                int id = obj.id;
                String petName = obj.name;
                String petBreed = obj.breed;
                int petAge = obj.age;
                String petGender = obj.gender;
                String petColor = obj.color;

                String textViewContents = petName + " - " + petBreed + ", " + petGender;

                TextView textViewPetItem= new TextView(this);
                textViewPetItem.setPadding(0, 10, 0, 10);
                textViewPetItem.setText(textViewContents);
                textViewPetItem.setTag(Integer.toString(id) + " " + petName);
                textViewPetItem.setOnLongClickListener(new OnLongClickListenerPet(this));
                Button tasksButton = new Button(this);
                tasksButton.setOnClickListener(new OnClickListenerTasksPerPet(this, new TasksPerPet(), obj));
                tasksButton.setText("TASKS for " + petName);
                linearLayoutRecords.addView(textViewPetItem);
                linearLayoutRecords.addView(tasksButton);
            }

        }

        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No records yet.");

            linearLayoutRecords.addView(locationItem);
        }

    }
}
