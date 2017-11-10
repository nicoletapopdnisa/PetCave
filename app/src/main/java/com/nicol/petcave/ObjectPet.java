package com.nicol.petcave;

/**
 * Created by nicol on 10/31/2017.
 */

public class ObjectPet {
    int id;
    String name;
    String breed;
    int age;
    String gender;
    String color;

    public ObjectPet() {

    }

    @Override
    public String toString()
    {
        return "PET = name: " + name + ", breed: " + breed +
                ", age: " + age + ", gender: " + gender +
                ", color: " + color + ".";
    }
}
