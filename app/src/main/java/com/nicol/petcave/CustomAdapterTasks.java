package com.nicol.petcave;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nicol on 12/5/2017.
 */

public class CustomAdapterTasks extends BaseAdapter implements ListAdapter {
    private ArrayList<ObjectTask> list;
    private Context context;
    private static TasksPerPet tasksPerPet;

    public CustomAdapterTasks(ArrayList<ObjectTask> list, Context context, TasksPerPet tasksPerPet) {
        this.list = list;
        this.context = context;
        this.tasksPerPet = tasksPerPet;
    }

    public void setList(ArrayList<ObjectTask> newList)
    {
        this.list = newList;
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
        return list.get(i).id;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.task_row, null);
        }

        TextView taskDescription = (TextView) view.findViewById(R.id.taskDescriptionTextView);
        taskDescription.setTag(Integer.toString(Math.toIntExact(getItemId(i))));
        taskDescription.setText(list.get(i).description);

        TextView taskDeadline = (TextView) view.findViewById(R.id.taskDeadlineTextView);
        taskDeadline.setText(list.get(i).deadline);

        taskDescription.setOnLongClickListener(new OnLongClickListenerTask(tasksPerPet, list.get(i).petId));

        return view;
    }
}
