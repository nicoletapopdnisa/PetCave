package com.nicol.petcave;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by nicol on 10/30/2017.
 */

public class OnClickListenerCreatePet implements View.OnClickListener {

    private static MainActivity mainActivity;
    public OnClickListenerCreatePet(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }
    @Override
    public void onClick(View view) {
        final Context context = view.getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.pet_input_form, null, false);
        final EditText editTextPetName = (EditText) formElementsView.findViewById(R.id.editTextPetName);
        final EditText editTextPetBreed = (EditText) formElementsView.findViewById(R.id.editTextPetBreed);
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
                                String petBreed = editTextPetBreed.getText().toString();
                                String petAge = editTextPetAge.getText().toString();
                                String petColor = editTextPetColor.getText().toString();

                                ObjectPet newPet = new ObjectPet();
                                newPet.name = petName;
                                newPet.breed = petBreed;
                                newPet.age = Integer.parseInt(petAge);
                                newPet.gender = petGender.toString();
                                newPet.color = petColor;

                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setType("text/plain");
                                intent.setData(Uri.parse("mailto:nicodeni.pop96@gmail.com?cc=nicodeni.pop96@gmail.com&subject=New Pet Added-PETCAVE&body="+
                                        newPet.toString() ));
                                try {
                                    context.startActivity(intent);
                                }
                                catch(ActivityNotFoundException ex)
                                {
                                    Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show();
                                }

                                boolean createSuccessful = new TableControllerPet(context).create(newPet);
                                if(createSuccessful) {
                                    Toast.makeText(context, "Pet created successfully.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "Unable to save pet.", Toast.LENGTH_SHORT).show();
                                }
                                mainActivity.countPets();
                                mainActivity.readPets();
                                dialog.cancel();
                            }

                        }).show();
    }
}
