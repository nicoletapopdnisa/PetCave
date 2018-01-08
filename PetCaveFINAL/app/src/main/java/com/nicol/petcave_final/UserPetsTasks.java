package com.nicol.petcave_final;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

public class UserPetsTasks extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_pets_tasks);

        listView = (ListView) findViewById(R.id.userPetsTasksListView);

        final String petId = getIntent().getStringExtra("petId");
        final String userEmail = getIntent().getStringExtra("userEmail");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("tasks");
        final ArrayList<Task> userPets = new ArrayList<>();
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
                    Task value = iterator.next().getValue(Task.class);
                    if(Objects.equals(value.petId, petId))
                    {

                        userPets.add(value);
                        count[0]++;
                        ((CustomAdapterUserPetsTasks) (((ListView)findViewById(R.id.userPetsTasksListView)).getAdapter())).notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((ListView)findViewById(R.id.userPetsTasksListView)).setAdapter(new CustomAdapterUserPetsTasks(userPets, this, this));

        Button report = (Button) findViewById(R.id.reportUserButton);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:"+ userEmail +"?cc="+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"&subject=Admin have reported you.PETCAVE&body="+
                        "PetCave does not agree with your way of taking care of animals. They are God's souls too! Reestablish your schedule or you're gonna be deleted soon." ));
                try {
                    view.getContext().startActivity(intent);
                }
                catch(ActivityNotFoundException ex)
                {
                    Toast.makeText(view.getContext(), "No email app available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
