package com.nicol.petcave_final;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by nicol on 12/31/2017.
 */

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button signOutBtn;
    private Button enterAppButton;
    private MySubject subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        subject = new MySubject();

        welcomeText = (TextView) findViewById(R.id.welcomeText);
        signOutBtn = (Button) findViewById(R.id.sign_out_button);
        enterAppButton = (Button) findViewById(R.id.enter_app_button);

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("useremail");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference tokensRef = rootRef.child("tokens");

        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Token t = new Token();
        t.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        t.refreshedToken = refreshedToken;
        tokensRef.child(refreshedToken).setValue(t);

        final Query deleteQuery = tokensRef.orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final Token[] tokens = {new Token()};
        deleteQuery.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                tokens[0] = dataSnapshot.getValue(Token.class);
                if (!Objects.equals(tokens[0].refreshedToken, refreshedToken))
                    tokensRef.child(tokens[0].refreshedToken).removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
// this listener will be called when there is change in firebase user session
                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            //startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                };

                FirebaseAuth.getInstance().addAuthStateListener(authListener);
            }
        });

        getUserRole(userEmail);
    }

    public void getUserRole(final String userEmail) {
        final String[] role = {""};
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("roles").addListenerForSingleValueEvent(new ValueEventListener() {


            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (Objects.equals(ds.getKey(), FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        role[0] = ds.getValue().toString();

                        StringBuilder welcome = new StringBuilder();
                        welcome.append("Hello, ");
                        welcome.append(userEmail);
                        welcome.append(". Role is: ");
                        welcome.append(role[0]);
                        welcomeText.setText(welcome.toString());
                    }
                }

                Log.d("TAG", role[0]);
                enterAppButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TAGAICI", role[0]);
                        if (Objects.equals(role[0], "normal")) {
                            Intent intent = new Intent(HomeActivity.this, NormalUserActivity.class);
                            intent.putExtra("subject", subject);
                            startActivity(intent);
                        } else
                        {
                            Intent intent = new Intent(HomeActivity.this, AdminUserActivity.class);
                            intent.putExtra("subject", subject);
                            startActivity(intent);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(HomeActivity.this, "You already are on home page. Please sign out in order to exit the app.",
                Toast.LENGTH_SHORT).show();
    }
}