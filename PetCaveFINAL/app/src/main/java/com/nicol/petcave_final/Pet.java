package com.nicol.petcave_final;

import java.util.UUID;

/**
 * Created by nicol on 1/2/2018.
 */

public class Pet {
    String id;
    String name;
    String breed;
    int age;
    String gender;
    String color;
    String userId;

    public Pet() {
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", color='" + color + '\'' +
                ", userId=" + userId +
                '}';
    }
}
