package com.nicol.petcave;

/**
 * Created by nicol on 11/5/2017.
 */

public class ObjectTask {
    int id;
    String description;
    String deadline;
    int petId;

    public ObjectTask() {

    }

    @Override
    public String toString() {
        return "ObjectTask{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", deadline='" + deadline + '\'' +
                ", petId=" + petId +
                '}';
    }
}
