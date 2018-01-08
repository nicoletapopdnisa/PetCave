package com.nicol.petcave_final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nicol on 1/8/2018.
 */

public class CustomAdapterUserPetsTasks extends BaseAdapter implements ListAdapter{

    private ArrayList<Task> list;
    private Context context;

    public CustomAdapterUserPetsTasks(ArrayList<Task> list, Context context, UserPetsTasks userPetsTasks)
    {
        this.list = list;
        this.context = context;
    }

    public void setList(ArrayList<Task> newList)
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
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.userpetstasks_row, null);
        }

        TextView taskDescription = (TextView) view.findViewById(R.id.userPetTaskTextView);
        taskDescription.setTag(list.get(i).id);
        taskDescription.setText(list.get(i).description);

        return view;
    }
}
