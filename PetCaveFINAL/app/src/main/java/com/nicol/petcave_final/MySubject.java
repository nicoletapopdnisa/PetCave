package com.nicol.petcave_final;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 1/7/2018.
 */

public class MySubject implements Subject, Serializable {

    private List<Observer> observers;

    public MySubject()
    {
        observers = new ArrayList<>();
    }

    @Override
    public void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String season) {
        for (Observer observer: observers) {
            observer.update(season);
        }
    }
}
