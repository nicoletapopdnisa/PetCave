package com.nicol.petcave_final;

import android.content.Intent;
import android.view.View;

/**
 * Created by nicol on 1/8/2018.
 */

public class OnClickListenerUserPetsTasks implements View.OnClickListener {

    private static UserPets userPets;
    private static UserPetsTasks userPetsTasks;
    String petId;
    String userEmail;

    public OnClickListenerUserPetsTasks(UserPets userPets, UserPetsTasks userPetsTasks, String petId, String userEmail)
    {
        this.userPets = userPets;
        this.userPetsTasks = userPetsTasks;
        this.petId = petId;
        this.userEmail = userEmail;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(userPets, userPetsTasks.getClass());
        intent.putExtra("petId", petId);
        intent.putExtra("userEmail", userEmail);
        userPets.startActivity(intent);
    }
}
