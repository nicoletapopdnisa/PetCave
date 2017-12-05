package com.nicol.petcave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonCreatePet = (Button) findViewById(R.id.buttonCreatePet);
        buttonCreatePet.setOnClickListener(new OnClickListenerCreatePet(this));

        final ListView petsList = findViewById(R.id.petsListView);

        List<ObjectPet> pets = new TableControllerPet(this).read();
        final ArrayList<ObjectPet> array = new ArrayList<>(pets);

        CustomAdapterPets adapter = new CustomAdapterPets(array, this, this);
        petsList.setAdapter(adapter);

        countPets();

        BarChart chart = (BarChart) findViewById(R.id.chart1);

        ArrayList<BarEntry> barEntry = new ArrayList<>();

        ArrayList<String> barEntryLabels = new ArrayList<String>();

        addValuesToBarEntry(pets, barEntry);
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
    protected void onResume()
    {
        super.onResume();
        refreshChart();
    }

    public void refreshChart()
    {
        List<ObjectPet> pets = new TableControllerPet(this).read();

        BarChart chart = (BarChart) findViewById(R.id.chart1);

        ArrayList<BarEntry> barEntry = new ArrayList<>();

        ArrayList<String> barEntryLabels = new ArrayList<String>();

        addValuesToBarEntry(pets, barEntry);
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

    public void addValuesToBarEntry(List<ObjectPet> pets, ArrayList<BarEntry> barEntry)
    {
        int count = 0;
        for(ObjectPet pet: pets)
        {
            int tasksCount = new TableControllerTasks(this).count(pet.id);
            barEntry.add(new BarEntry((float)tasksCount, count));
            count++;
        }
    }

    public void addValuesToBarEntryLabels(List<ObjectPet> pets, ArrayList<String> barEntryLabels)
    {
        ObjectPet[] petss = new ObjectPet[pets.size()];
        petss = pets.toArray(petss);
        for(int i = 0; i < petss.length;i++)
        {
            barEntryLabels.add(petss[i].name);
        }
    }

    public void countPets() {
        int petsCount = new TableControllerPet(this).count();
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewPetCount);
        textViewRecordCount.setText(petsCount + " records found.");
    }

    public void readPets() {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);

        List<ObjectPet> pets = new TableControllerPet(this).read();

        if (pets.size() > 0) {
            final ListView petsList = (ListView) findViewById(R.id.petsListView);
            final ArrayList<ObjectPet> array = new ArrayList<>(pets);
            ((CustomAdapterPets) petsList.getAdapter()).setList(array);
            ((BaseAdapter) petsList.getAdapter()).notifyDataSetChanged();

            refreshChart();

        }

        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No records yet.");

            linearLayoutRecords.addView(locationItem);
        }

    }
}
