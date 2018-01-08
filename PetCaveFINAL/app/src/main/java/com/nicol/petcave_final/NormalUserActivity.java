package com.nicol.petcave_final;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by nicol on 1/2/2018.
 */

public class NormalUserActivity extends AppCompatActivity {

    private Button createPet;
    private TextView petCountTextView;
    private BreedList breedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_user_layout);

        createPet = (Button) findViewById(R.id.buttonCreatePet);
        breedList = new BreedList();
        breedList.addToArray("dog");
        breedList.addToArray("cat");
        breedList.addToArray("parrot");
        breedList.addToArray("canary");
        breedList.addToArray("guinea pig");
        breedList.addToArray("cow");
        breedList.addToArray("hen");
        breedList.addToArray("mouse");
        breedList.addToArray("tortoise");
        breedList.addToArray("horse");
        breedList.addToArray("pig");
        createPet.setOnClickListener(new OnClickListenerCreatePet(this, breedList));

        petCountTextView = (TextView) findViewById(R.id.textViewPetCount);

        readPets();
    }

    public void readPets()
    {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference petsRef = rootRef.child("pets");
        final ArrayList<Pet> pets = new ArrayList<>();
        final int[] count = {0};

        petsRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                pets.clear();
                count[0] = 0;
                while(iterator.hasNext())
                {
                    Pet value = iterator.next().getValue(Pet.class);
                    if(Objects.equals(value.userId, FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        pets.add(value);
                        count[0]++;
                        ((CustomAdapterPets) (((ListView)findViewById(R.id.petsListView)).getAdapter())).notifyDataSetChanged();
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(count[0]);
                stringBuilder.append(" pets found.");
                petCountTextView.setText(stringBuilder.toString());
                buildChart(rootRef, pets);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((ListView)findViewById(R.id.petsListView)).setAdapter(new CustomAdapterPets(pets, this, this, breedList));
    }

    public void buildChart(DatabaseReference rootRef, final ArrayList<Pet> pets)
    {
        final DatabaseReference tasksRef = rootRef.child("tasks");

        tasksRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                BarChart chart = (BarChart) findViewById(R.id.chart1);

                ArrayList<BarEntry> barEntry = new ArrayList<>();

                ArrayList<String> barEntryLabels = new ArrayList<String>();

                addValuesToBarEntry(dataSnapshot, pets, barEntry);
                addValuesToBarEntryLabels(pets, barEntryLabels);

                BarDataSet barDataSet = new BarDataSet(barEntry, "Tasks");
                BarData barData = new BarData(barEntryLabels, barDataSet);
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                chart.setData(barData);
                XAxis xAxis = chart.getXAxis();
                xAxis.setTextSize(0.1f);
                chart.animateX(2000);
                chart.animateY(3000);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addValuesToBarEntry(DataSnapshot dataSnapshot, List<Pet> pets, ArrayList<BarEntry> barEntry)
    {
        int count = -1;
        for(Pet pet: pets)
        {
            count++;
            int taskCount = 0;
            for(DataSnapshot ds: dataSnapshot.getChildren())
            {
                if(Objects.equals(ds.getValue(Task.class).petId, pet.id))
                {
                    taskCount++;
                }
            }
            barEntry.add(new BarEntry((float) taskCount, count));
        }
    }

    public void addValuesToBarEntryLabels(List<Pet> pets, ArrayList<String> barEntryLabels)
    {
        Pet[] petss = new Pet[pets.size()];
        petss = pets.toArray(petss);
        for(int i = 0; i < petss.length;i++)
        {
            barEntryLabels.add(petss[i].name);
        }
    }
}
