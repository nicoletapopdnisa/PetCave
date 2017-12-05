package com.nicol.petcave;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by nicol on 11/6/2017.
 */

public class OnClickListenerCreateTask implements View.OnClickListener {

    private static TasksPerPet tasksPerPet;
    private int petId;
    private String petName;

    public OnClickListenerCreateTask(TasksPerPet tasksPerPet, int petId, String petName) {
        this.tasksPerPet = tasksPerPet;
        this.petId = petId;
        this.petName = petName;
    }
    @Override
    public void onClick(View view) {
        final Context context = view.getRootView().getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.task_input_form, null, false);
        final EditText editTextTaskDescription = (EditText) formElementsView.findViewById(R.id.editTextTaskDescription);

        final EditText editTextTaskDeadline = (EditText) formElementsView.findViewById(R.id.editTextTaskDeadline);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);

                String value = String.valueOf(i)+"-"+String.valueOf(i1)+"-"+String.valueOf(i2);
                editTextTaskDeadline.setText(value);
            }
        };

        editTextTaskDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).
                        show();
            }
        });

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Create New Task for " + petName)
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String description = editTextTaskDescription.getText().toString();
                                String deadline = editTextTaskDeadline.getText().toString();

                                ObjectTask task = new ObjectTask();
                                task.description = description;
                                task.deadline = deadline;
                                task.petId = petId;
                                if(!Objects.equals(task.description, "") && isValidDate(task.deadline))
                                {
                                    boolean createSuccessful = new TableControllerTasks(context).create(task);
                                    if(createSuccessful) {
                                        Toast.makeText(context, "New task created successfully.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                                        intent.setType("text/plain");
                                        intent.setData(Uri.parse("mailto:nicodeni.pop96@gmail.com?cc=nicodeni.pop96@gmail.com&subject=New Task Added For Pet "+petName +" -PETCAVE&body="+
                                                task.toString() ));
                                        try {
                                            context.startActivity(intent);
                                        }
                                        catch(ActivityNotFoundException ex)
                                        {
                                            Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(context, "Unable to create a new task.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(context, "Invalid task data", Toast.LENGTH_SHORT).show();
                                }

                                tasksPerPet.countTasks(petId);
                                tasksPerPet.readTasksPerPet(petId);

                                dialogInterface.cancel();
                            }
                        }).show();
    }

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}
