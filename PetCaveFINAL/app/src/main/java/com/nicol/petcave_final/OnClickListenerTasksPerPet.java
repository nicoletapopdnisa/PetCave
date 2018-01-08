package com.nicol.petcave_final;

import android.content.Intent;
import android.view.View;

/**
 * Created by nicol on 1/2/2018.
 */

public class OnClickListenerTasksPerPet implements View.OnClickListener {

    private static NormalUserActivity normalUserActivity;
    private static TasksPerPet tasksPerPet;
    Pet pet;

    public OnClickListenerTasksPerPet(NormalUserActivity normalUserActivity, TasksPerPet tasksPerPet, Pet pet)
    {
        this.normalUserActivity = normalUserActivity;
        this.tasksPerPet = tasksPerPet;
        this.pet = pet;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(normalUserActivity, tasksPerPet.getClass());
        intent.putExtra("petName", pet.name);
        intent.putExtra("petId", pet.id);
        normalUserActivity.startActivity(intent);
    }
}
