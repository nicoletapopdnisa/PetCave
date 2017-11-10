package com.nicol.petcave;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 11/5/2017.
 */

public class TableControllerTasks extends DatabaseHandler {

    public TableControllerTasks(Context context) {
        super(context);
    }

    public int count(int petId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM tasks WHERE petID = " + petId;

        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;
    }

    public boolean create(ObjectTask task) {
        ContentValues values = new ContentValues();

        values.put("description", task.description);
        values.put("petID", task.petId);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("tasks", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public ObjectTask readByID(int id) {
        ObjectTask objectTask = null;

        String sql = "SELECT * FROM tasks WHERE id = " + id;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int taskId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            int petId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("petID")));

            objectTask= new ObjectTask();
            objectTask.id = taskId;
            objectTask.description = description;
            objectTask.petId = petId;
        }

        cursor.close();
        db.close();

        return objectTask;
    }

    public List<ObjectTask> read(int petId) {

        List<ObjectTask> recordsList = new ArrayList<ObjectTask>();

        String sql = "SELECT * FROM tasks WHERE petID = " + petId + " ORDER BY description";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                ObjectTask objectTask = new ObjectTask();
                objectTask.id = id;
                objectTask.description = description;
                objectTask.petId = petId;

                recordsList.add(objectTask);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public boolean update(ObjectTask objectTask) {

        ContentValues values = new ContentValues();

        values.put("description", objectTask.description);

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(objectTask.id) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("tasks", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;

    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("tasks", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }
}
