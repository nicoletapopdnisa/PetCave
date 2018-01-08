package com.nicol.petcave_final;

import java.util.UUID;

/**
 * Created by nicol on 1/2/2018.
 */

public class Task {
    String id;
    String description;
    String deadline;
    String petId;

    public Task() {
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", deadline='" + deadline + '\'' +
                ", petId=" + petId +
                '}';
    }
}
