package com.nicol.petcave_final;

import android.content.Intent;
import android.view.View;

/**
 * Created by nicol on 1/8/2018.
 */

public class OnClickListenerUserManagement implements View.OnClickListener {

    private static AdminUserActivity adminUserActivity;
    private static UserManagementActivity userManagementActivity;

    public OnClickListenerUserManagement(AdminUserActivity adminUserActivity, UserManagementActivity userManagementActivity)
    {
        this.adminUserActivity = adminUserActivity;
        this.userManagementActivity = userManagementActivity;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(adminUserActivity, userManagementActivity.getClass());
        adminUserActivity.startActivity(intent);
    }
}
