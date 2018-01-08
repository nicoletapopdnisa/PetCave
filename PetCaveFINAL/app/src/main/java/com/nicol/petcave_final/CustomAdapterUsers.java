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

public class CustomAdapterUsers extends BaseAdapter implements ListAdapter {
    private ArrayList<User> list;
    private Context context;
    private static UserManagementActivity userManagementActivity;

    public CustomAdapterUsers(ArrayList<User> list, Context context, UserManagementActivity userManagementActivity)
    {
        this.list = list;
        this.context = context;
        this.userManagementActivity = userManagementActivity;
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
            view = inflater.inflate(R.layout.users_row, null);
        }

        TextView userName = (TextView) view.findViewById(R.id.userTextView);
        userName.setTag(list.get(i).userId + " " + list.get(i).email);
        userName.setText(list.get(i).email);

        Button userPets = (Button) view.findViewById(R.id.userPetButton);

        userPets.setOnClickListener(new OnClickListenerUserPets( userManagementActivity, new UserPets(), list.get(i).userId, list.get(i).email));

        return view;
    }
}
