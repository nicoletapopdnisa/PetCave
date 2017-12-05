package com.nicol.petcave;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 12/4/2017.
 */

public class CustomAdapterPets extends BaseAdapter implements ListAdapter {

    private ArrayList<ObjectPet> list;
    private Context context;
    private static MainActivity mainActivity;

    public CustomAdapterPets(ArrayList<ObjectPet> list, Context context, MainActivity mainActivity) {
        this.list = list;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    public void setList(ArrayList<ObjectPet> newList)
    {
        this.list = newList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ObjectPet getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).id;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pets_row, null);
        }

        TextView petName = (TextView) view.findViewById(R.id.petNameTextView);
        petName.setTag(Integer.toString(getItem(i).id) + " " + getItem(i).name);
        petName.setText(getItem(i).name);

        petName.setOnLongClickListener(new OnLongClickListenerPet(mainActivity));

        TextView petBreed = (TextView) view.findViewById(R.id.petBreedTextView);
        petBreed.setText(getItem(i).breed);

        Button petTask = (Button) view.findViewById(R.id.petTaskButton);

        petTask.setOnClickListener(new OnClickListenerTasksPerPet( mainActivity, new TasksPerPet(), getItem(i)));

        return view;
    }
}
