package com.nicol.petcave_final;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by nicol on 1/8/2018.
 */

public class UserPets extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_pets);

        listView = (ListView) findViewById(R.id.userPetsListView);

        final String userId = getIntent().getStringExtra("userId");
        final String userEmail = getIntent().getStringExtra("userEmail");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("pets");
        final ArrayList<Pet> userPets = new ArrayList<>();
        final int count[] = {0};

        usersRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                userPets.clear();
                count[0] = 0;

                while(iterator.hasNext())
                {
                    Pet value = iterator.next().getValue(Pet.class);
                    if(Objects.equals(value.userId, userId))
                    {

                        userPets.add(value);
                        count[0]++;
                        ((CusotmAdapterUserPets) (((ListView)findViewById(R.id.userPetsListView)).getAdapter())).notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((ListView)findViewById(R.id.userPetsListView)).setAdapter(new CusotmAdapterUserPets(userPets, this, this, userEmail));
    }
}
