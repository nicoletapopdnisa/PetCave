package com.nicol.petcave_final;

/**
 * Created by nicol on 1/2/2018.
 */

public class User {
    String userId;
    String email;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
