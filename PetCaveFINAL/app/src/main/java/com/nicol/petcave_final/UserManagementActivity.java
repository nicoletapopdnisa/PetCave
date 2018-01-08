package com.nicol.petcave_final;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by nicol on 1/8/2018.
 */

public class UserManagementActivity extends AppCompatActivity implements Serializable{

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usermanage);

        listView = (ListView) findViewById(R.id.usersListView);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("users");
        final ArrayList<User> users = new ArrayList<>();
        final int count[] = {0};

        usersRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                users.clear();
                count[0] = 0;

                while(iterator.hasNext())
                {
                    User value = iterator.next().getValue(User.class);
                    if(!Objects.equals(value.email, "nicodeni.pop96@yahoo.com"))
                    {
                        users.add(value);
                        count[0]++;
                        ((CustomAdapterUsers) (((ListView)findViewById(R.id.usersListView)).getAdapter())).notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((ListView)findViewById(R.id.usersListView)).setAdapter(new CustomAdapterUsers(users, this, this));
    }
}
