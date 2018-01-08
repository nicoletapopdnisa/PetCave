package com.nicol.petcave_final;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by nicol on 1/5/2018.
 */

public interface Observer extends Serializable {
    public void update(final String season);
}
