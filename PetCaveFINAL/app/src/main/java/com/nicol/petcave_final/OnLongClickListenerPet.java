package com.nicol.petcave_final;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by nicol on 1/2/2018.
 */

public class OnLongClickListenerPet implements View.OnLongClickListener, AdapterView.OnItemSelectedListener {

    private static NormalUserActivity normalUserActivity;
    private ArrayList<Pet> pets;
    private BreedList breedList;
    Context context;
    String id;
    String petName;

    public OnLongClickListenerPet(NormalUserActivity normalUserActivity, ArrayList<Pet> pets, BreedList breedList)
    {
        this.normalUserActivity = normalUserActivity;
        this.pets = pets;
        this.breedList = breedList;
    }

    @Override
    public boolean onLongClick(final View view) {

        context = view.getContext();
        String[]contents = view.getTag().toString().split(" ");
        id = contents[0];
        petName = contents[1];

        final CharSequence[] items = { "Edit", "Delete" };
        new AlertDialog.Builder(context).setTitle("My Pet "+petName)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(DialogInterface dialog, int item) {
                        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        final DatabaseReference petsRef = rootRef.child("pets");
                        if (item == 0) {
                            editPet(id, petsRef, view);
                        }
                        else if (item == 1) {

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                    petsRef.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(normalUserActivity, "deletePet:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            if(!task.isSuccessful())
                                            {
                                                Toast.makeText(context, "Unable to delete pet record.", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Pet record was deleted.", Toast.LENGTH_SHORT).show();
                                                final DatabaseReference tasksRef = rootRef.child("tasks");
                                                final Query deleteQuery = tasksRef.orderByChild("petId").equalTo(id);
                                                final com.nicol.petcave_final.Task[] petTasks = {new com.nicol.petcave_final.Task()};
                                                AsyncTask.execute(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        deleteQuery.addChildEventListener(new ChildEventListener() {
                                                            @Override
                                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                                                petTasks[0] = dataSnapshot.getValue(com.nicol.petcave_final.Task.class);
                                                                tasksRef.child(petTasks[0].id).removeValue();
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
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        dialog.dismiss();

                    }
                }).show();

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editPet(final String petId, final DatabaseReference petsRef, View view) {

        Pet pet = new Pet();
        for(Pet p: pets)
        {
            if(Objects.equals(p.id, petId))
            {
                pet = p;
            }
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.pet_input_form, null, false);

        final EditText editTextPetName= (EditText) formElementsView.findViewById(R.id.editTextPetName);
        final Spinner editTextPetBreed = (Spinner) formElementsView.findViewById(R.id.editTextPetBreed);
        editTextPetBreed.setOnItemSelectedListener(this);
        String[] list = breedList.getArray();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTextPetBreed.setAdapter(dataAdapter);
        final EditText editTextPetAge = (EditText) formElementsView.findViewById(R.id.editTextPetAge);
        final RadioGroup petGenderRadioGroup = (RadioGroup) formElementsView.findViewById(R.id.petGenderRadioGroup);
        final EditText editTextPetColor = (EditText) formElementsView.findViewById(R.id.editTextPetColor);
        int spinnerPos = dataAdapter.getPosition(pet.breed);
        editTextPetName.setText(pet.name);
        editTextPetBreed.setSelection(spinnerPos);
        editTextPetAge.setText(pet.age+"");

        if(Objects.equals(pet.gender, "Male")) {
            petGenderRadioGroup.check(R.id.maleGenderRadioButton);
        }
        else if(Objects.equals(pet.gender, "Female")){
            petGenderRadioGroup.check(R.id.femaleGenderRadioButton);
        }
        editTextPetColor.setText(pet.color);

        final StringBuffer petGender = new StringBuffer();
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
        petGender.setLength(0);
        int checkedId = petGenderRadioGroup.getCheckedRadioButtonId();
        if(checkedId == R.id.maleGenderRadioButton) {
            petGender.append("Male");
        }
        else {
            petGender.append("Female");
        }

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Edit Pet")
                .setPositiveButton("Save Changes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final Pet objectPet = new Pet();
                                objectPet.id = petId;
                                objectPet.name = editTextPetName.getText().toString();
                                objectPet.breed = editTextPetBreed.getSelectedItem().toString();
                                objectPet.age = Integer.parseInt(editTextPetAge.getText().toString());
                                objectPet.gender = petGender.toString();
                                objectPet.color = editTextPetColor.getText().toString();
                                objectPet.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        petsRef.child(petId).setValue(objectPet).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(normalUserActivity, "updatePet:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                if (!task.isSuccessful()){
                                                    Toast.makeText(normalUserActivity, "Unable to update pet: " + task.getException(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(context, "Pet record was updated.", Toast.LENGTH_SHORT).show();
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
