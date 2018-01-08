package com.nicol.petcave_final;

import android.content.Intent;
import android.view.View;

/**
 * Created by nicol on 1/8/2018.
 */

public class OnClickListenerUserPets implements View.OnClickListener {
    private static UserManagementActivity userManagementActivity;
    private static UserPets userPets;
    String userId;
    String userEmail;

    public OnClickListenerUserPets(UserManagementActivity userManagementActivity, UserPets userPets, String userId, String userEmail)
    {
        this.userManagementActivity = userManagementActivity;
        this.userPets = userPets;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(userManagementActivity, userPets.getClass());
        intent.putExtra("userId", userId);
        intent.putExtra("userEmail", userEmail);
        userManagementActivity.startActivity(intent);
    }
}
