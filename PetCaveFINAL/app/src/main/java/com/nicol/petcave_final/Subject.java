package com.nicol.petcave_final;

import android.content.Context;

/**
 * Created by nicol on 1/5/2018.
 */

public interface Subject {
    public void register(Observer observer);
    public void unregister(Observer observer);
    public void notifyObservers(String season);
}
