package com.nicol.petcave_final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nicol on 1/2/2018.
 */

public class CustomAdapterPets extends BaseAdapter implements ListAdapter {

    private ArrayList<Pet> list;
    private Context context;
    private static NormalUserActivity normalUserActivity;
    private BreedList breedList;

    public CustomAdapterPets(ArrayList<Pet> list, Context context, NormalUserActivity normalUserActivity, BreedList breedList)
    {
        this.list = list;
        this.context = context;
        this.normalUserActivity = normalUserActivity;
        this.breedList = breedList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pets_row, null);
        }

        TextView petName = (TextView) view.findViewById(R.id.petNameTextView);
        petName.setTag(list.get(i).id + " " + list.get(i).name);
        petName.setText(list.get(i).name);

        petName.setOnLongClickListener(new OnLongClickListenerPet(normalUserActivity, list, breedList));

        TextView petBreed = (TextView) view.findViewById(R.id.petBreedTextView);
        petBreed.setText(list.get(i).breed);

        Button petTask = (Button) view.findViewById(R.id.petTaskButton);

        petTask.setOnClickListener(new OnClickListenerTasksPerPet( normalUserActivity, new TasksPerPet(), list.get(i)));

        return view;
    }
}
