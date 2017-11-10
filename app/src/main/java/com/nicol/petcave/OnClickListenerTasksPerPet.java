package com.nicol.petcave;

import android.content.Intent;
import android.view.View;

/**
 * Created by nicol on 11/5/2017.
 */

public class OnClickListenerTasksPerPet implements View.OnClickListener {
    private static MainActivity mainActivity;
    private static TasksPerPet tasksPerPet;
    ObjectPet objectPet;

    public OnClickListenerTasksPerPet(MainActivity mainActivity, TasksPerPet tasksPerPet, ObjectPet objectPet) {
        this.mainActivity = mainActivity;
        this.tasksPerPet = tasksPerPet;
        this.objectPet = objectPet;
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mainActivity, tasksPerPet.getClass());
        intent.putExtra("petName", objectPet.name);
        intent.putExtra("petId", objectPet.id);
        mainActivity.startActivity(intent);
    }
}
