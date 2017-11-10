package com.nicol.petcave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 10/31/2017.
 */

public class TableControllerPet extends DatabaseHandler {

    public TableControllerPet(Context context) {
        super(context);
    }

    public boolean create(ObjectPet pet) {
        ContentValues values = new ContentValues();

        values.put("name", pet.name);
        values.put("breed", pet.breed);
        values.put("age", pet.age);
        values.put("gender", pet.gender);
        values.put("color", pet.color);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("pets", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public int count() {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM pets";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }

    public List<ObjectPet> read() {

        List<ObjectPet> recordsList = new ArrayList<ObjectPet>();

        String sql = "SELECT * FROM pets ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String petName = cursor.getString(cursor.getColumnIndex("name"));
                String petBreed = cursor.getString(cursor.getColumnIndex("breed"));
                String petAge = cursor.getString(cursor.getColumnIndex("age"));
                String petGender = cursor.getString(cursor.getColumnIndex("gender"));
                String petColor = cursor.getString(cursor.getColumnIndex("color"));

                ObjectPet objectPet = new ObjectPet();
                objectPet.id = id;
                objectPet.name = petName;
                objectPet.breed= petBreed;
                objectPet.age = Integer.parseInt(petAge);
                objectPet.gender = petGender;
                objectPet.color = petColor;


                recordsList.add(objectPet);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public ObjectPet readSingleRecord(int petId) {

        ObjectPet objectPet = null;

        String sql = "SELECT * FROM pets WHERE id = " + petId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String breed = cursor.getString(cursor.getColumnIndex("breed"));
            int age = Integer.parseInt(cursor.getString(cursor.getColumnIndex("age")));
            String gender = cursor.getString(cursor.getColumnIndex("gender"));
            String color = cursor.getString(cursor.getColumnIndex("color"));

            objectPet = new ObjectPet();
            objectPet.id = id;
            objectPet.name = name;
            objectPet.breed = breed;
            objectPet.age = age;
            objectPet.gender = gender;
            objectPet.color = color;

        }

        cursor.close();
        db.close();

        return objectPet;

    }

    public boolean update(ObjectPet objectPet) {

        ContentValues values = new ContentValues();

        values.put("name", objectPet.name);
        values.put("breed", objectPet.breed);
        values.put("age", objectPet.age);
        values.put("gender", objectPet.gender);
        values.put("color", objectPet.color);

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(objectPet.id) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("pets", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;

    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("pets", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }
}
