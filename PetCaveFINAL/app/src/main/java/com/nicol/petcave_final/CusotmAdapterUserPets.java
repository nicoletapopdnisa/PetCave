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
 * Created by nicol on 1/8/2018.
 */

public class CusotmAdapterUserPets extends BaseAdapter implements ListAdapter{
    private ArrayList<Pet> list;
    private Context context;
    private static UserPets userPets;
    String userEmail;

    public CusotmAdapterUserPets(ArrayList<Pet> list, Context context, UserPets userPets, String userEmail)
    {
        this.list = list;
        this.context = context;
        this.userPets = userPets;
        this.userEmail = userEmail;
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
            view = inflater.inflate(R.layout.userpets_row, null);
        }

        TextView petName = (TextView) view.findViewById(R.id.userPetTextView);
        petName.setTag(list.get(i).id + " " + list.get(i).name);
        petName.setText(list.get(i).name);

        Button petTask = (Button) view.findViewById(R.id.userPetButton);
        petTask.setOnClickListener(new OnClickListenerUserPetsTasks(userPets, new UserPetsTasks(), list.get(i).id, userEmail));

        return view;
    }
}
