package com.nicol.petcave_final;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

/**
 * Created by nicol on 1/2/2018.
 */

public class OnClickListenerCreatePet implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private static NormalUserActivity normalUserActivity;
    private BreedList breedList;
    Spinner editTextPetBreed;

    public OnClickListenerCreatePet(NormalUserActivity normalUserActivity, BreedList breedList)
    {
        this.normalUserActivity = normalUserActivity;
        this.breedList = breedList;
    }

    @Override
    public void onClick(View view) {
        final Context context = view.getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.pet_input_form, null, false);
        final EditText editTextPetName = (EditText) formElementsView.findViewById(R.id.editTextPetName);
        editTextPetBreed = (Spinner) formElementsView.findViewById(R.id.editTextPetBreed);
        editTextPetBreed.setOnItemSelectedListener(this);
        String[] list = breedList.getArray();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTextPetBreed.setAdapter(dataAdapter);


        final EditText editTextPetAge = (EditText) formElementsView.findViewById(R.id.editTextPetAge);
        final RadioGroup petGenderRadioGroup = (RadioGroup) formElementsView.findViewById(R.id.petGenderRadioGroup);
        final EditText editTextPetColor = (EditText) formElementsView.findViewById(R.id.editTextPetColor);
        final StringBuffer petGender = new StringBuffer();
        petGender.setLength(0);
        int checkedId = petGenderRadioGroup.getCheckedRadioButtonId();
        if(checkedId == R.id.maleGenderRadioButton) {
            petGender.append("Male");
        }
        else {
            petGender.append("Female");
        }
        petGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.maleGenderRadioButton) {
                    petGender.setLength(0);
                    petGender.append("Male");
                }
                else {
                    petGender.setLength(0);
                    petGender.append("Female");
                }
            }
        });

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Create Pet")
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {


                            public void onClick(DialogInterface dialog, int id) {
                                String petName = editTextPetName.getText().toString();
                                String petBreed = editTextPetBreed.getSelectedItem().toString();
                                String petAge = editTextPetAge.getText().toString();
                                String petColor = editTextPetColor.getText().toString();

                                final Pet newPet = new Pet();
                                newPet.name = petName;
                                newPet.breed = petBreed;
                                newPet.age = Integer.parseInt(petAge);
                                newPet.gender = petGender.toString();
                                newPet.color = petColor;
                                newPet.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                final DatabaseReference petsRef = rootRef.child("pets");
                                final String petId = petsRef.push().getKey();
                                newPet.id = petId;

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        petsRef.child(petId).setValue(newPet).addOnCompleteListener(normalUserActivity, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(normalUserActivity, "createNewPet:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                if (!task.isSuccessful()){
                                                    Toast.makeText(normalUserActivity, "Unable to create new pet: " + task.getException(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                                    intent.setType("text/plain");
                                                    intent.setData(Uri.parse("mailto:"+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"?cc="+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"&subject=New Pet Added-PETCAVE&body="+
                                                            newPet.toString() ));
                                                    try {
                                                        context.startActivity(intent);
                                                    }
                                                    catch(ActivityNotFoundException ex)
                                                    {
                                                        Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });


                                dialog.cancel();
                            }

                        }).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
