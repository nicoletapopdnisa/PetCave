package com.nicol.petcave_final;

/**
 * Created by nicol on 1/4/2018.
 */

public class Token {

    String userId;
    String refreshedToken;

    public Token() {
    }

    @Override
    public String toString() {
        return "Token{" +
                "userId='" + userId + '\'' +
                ", refreshedToken='" + refreshedToken + '\'' +
                '}';
    }
}
