package com.nicol.petcave_final;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(inputEmail.getText().toString(),inputPassword.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(MainActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    FirebaseMessaging.getInstance().subscribeToTopic("weAreGrowingNotifications");

                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    intent.putExtra("useremail", inputEmail.getText().toString());

                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference rolesRef = rootRef.child("roles");
                                    DatabaseReference usersRef = rootRef.child("users");

                                    User user = new User();
                                    user.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    user.email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                    usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

                                    rolesRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("normal");
                                    String key = rolesRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).toString();
                                    intent.putExtra("role", "normal");
                                    startActivity(intent);
                                    //finish();
                                }
                            }
                        });
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
