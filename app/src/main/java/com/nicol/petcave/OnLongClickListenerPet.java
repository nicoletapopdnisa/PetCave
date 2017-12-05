package com.nicol.petcave;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

/**
 * Created by nicol on 11/5/2017.
 */

public class OnLongClickListenerPet implements View.OnLongClickListener {
    private static MainActivity mainActivity;
    Context context;
    String id;
    String petName;

    public OnLongClickListenerPet(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onLongClick(View view) {
        context = view.getContext();
        String[]contents = view.getTag().toString().split(" ");
        id = contents[0];
        petName = contents[1];

        final CharSequence[] items = { "Edit", "Delete" };
        new AlertDialog.Builder(context).setTitle("My Pet "+petName)
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            editPet(Integer.parseInt(id));
                        }
                        else if (item == 1) {

                            boolean deleteSuccessful = new TableControllerPet(context).delete(Integer.parseInt(id));

                            if (deleteSuccessful){
                                Toast.makeText(context, "Pet record was deleted.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Unable to delete pet record.", Toast.LENGTH_SHORT).show();
                            }

                            mainActivity.countPets();
                            mainActivity.readPets();

                        }
                        dialog.dismiss();

                    }
                }).show();

        return false;
    }

    public void editPet(final int petId) {
        final TableControllerPet tableControllerPet = new TableControllerPet(context);
        ObjectPet objectPet = tableControllerPet.readSingleRecord(petId);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.pet_input_form, null, false);

        final EditText editTextPetName= (EditText) formElementsView.findViewById(R.id.editTextPetName);
        final EditText editTextPetBreed = (EditText) formElementsView.findViewById(R.id.editTextPetBreed);
        final EditText editTextPetAge = (EditText) formElementsView.findViewById(R.id.editTextPetAge);
        final RadioGroup petGenderRadioGroup = (RadioGroup) formElementsView.findViewById(R.id.petGenderRadioGroup);
        final EditText editTextPetColor = (EditText) formElementsView.findViewById(R.id.editTextPetColor);

        editTextPetName.setText(objectPet.name);
        editTextPetBreed.setText(objectPet.breed);
        editTextPetAge.setText(objectPet.age+"");
        if(Objects.equals(objectPet.gender, "Male")) {
            petGenderRadioGroup.check(R.id.maleGenderRadioButton);
        }
        else if(Objects.equals(objectPet.gender, "Female")){
            petGenderRadioGroup.check(R.id.femaleGenderRadioButton);
        }
        editTextPetColor.setText(objectPet.color);

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

                                ObjectPet objectPet = new ObjectPet();
                                objectPet.id = petId;
                                objectPet.name = editTextPetName.getText().toString();
                                objectPet.breed = editTextPetBreed.getText().toString();
                                objectPet.age = Integer.parseInt(editTextPetAge.getText().toString());
                                objectPet.gender = petGender.toString();
                                objectPet.color = editTextPetColor.getText().toString();

                                boolean updateSuccessful = tableControllerPet.update(objectPet);

                                if(updateSuccessful){
                                    Toast.makeText(context, "Pet record was updated.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Unable to update pet record.", Toast.LENGTH_SHORT).show();
                                }

                                mainActivity.countPets();
                                mainActivity.readPets();

                                dialog.cancel();
                            }

                        }).show();
    }
}
