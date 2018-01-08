package com.nicol.petcave_final;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nicol on 1/5/2018.
 */

public class BreedList {

    private List<String> breedList;

    public BreedList()
    {
        breedList = new ArrayList<>();
    }

    public void addToArray(String newEntry)
    {
        breedList.add(newEntry);
    }

    public String[] getArray()
    {
        return breedList.toArray(new String[0]);
    }
}
