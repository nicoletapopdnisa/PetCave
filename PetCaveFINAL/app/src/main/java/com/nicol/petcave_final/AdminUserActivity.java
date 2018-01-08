package com.nicol.petcave_final;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by nicol on 1/2/2018.
 */

public class AdminUserActivity extends AppCompatActivity implements Observer, Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_layout);

        final int[] countUsers = {0};
        final int[] countPets = {0};
        final int[] countTasks = {0};

        final MySubject subject = (MySubject) getIntent().getSerializableExtra("subject");
        subject.register(this);

        final TextView users = (TextView) findViewById(R.id.textNrUsers);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersRef = rootRef.child("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                countUsers[0] = 0;

                while(iterator.hasNext())
                {
                    User value = iterator.next().getValue(User.class);
                    if(!Objects.equals(value.email, "nicodeni.pop96@yahoo.com"))
                    {
                        countUsers[0]++;
                    }

                }

                switch(countUsers[0]%4)
                {
                    case 0: subject.notifyObservers("winter");
                        break;
                    case 1: subject.notifyObservers("spring");
                        break;
                    case 2: subject.notifyObservers("summer");
                        break;
                    case 3: subject.notifyObservers("autumn");
                        break;
                }

                users.setText(countUsers[0]+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final TextView pets = (TextView) findViewById(R.id.infoNrPets);

        DatabaseReference petsRef = rootRef.child("pets");
        petsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                countPets[0] = 0;

                while(iterator.hasNext())
                {
                    iterator.next();
                    countPets[0]++;
                }

                pets.setText(countPets[0]+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final TextView tasks = (TextView) findViewById(R.id.infoNrTasks);

        DatabaseReference tasksRef = rootRef.child("tasks");
        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();

                countTasks[0] = 0;

                while(iterator.hasNext())
                {
                    iterator.next();
                    countTasks[0]++;
                }

                tasks.setText(countTasks[0]+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button userManagementButton = (Button) findViewById(R.id.userManagementbutton);
        userManagementButton.setOnClickListener(new OnClickListenerUserManagement(this, new UserManagementActivity()));
    }

    @Override
    public void update(String season) {

        ConstraintLayout layout = findViewById(R.id.adminLayout);

        switch(season)
        {
            case "winter":layout.setBackgroundResource(R.drawable.winter_activity);
                break;
            case "spring":layout.setBackgroundResource(R.drawable.spring_activity);
                break;
            case "summer":layout.setBackgroundResource(R.drawable.summer_activity);
                break;
            case "autumn":layout.setBackgroundResource(R.drawable.autumn_activity);
                break;
            default:
                break;
        }
    }
}
