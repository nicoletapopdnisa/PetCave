package com.nicol.petcave_final;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by nicol on 1/2/2018.
 */

public class OnClickListenerCreateTask implements View.OnClickListener {

    private static TasksPerPet tasksPerPet;
    private String petId;
    private String petName;

    public OnClickListenerCreateTask(TasksPerPet tasksPerPet, String petId, String petName) {
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

                                final Task task = new Task();
                                task.description = description;
                                task.deadline = deadline;
                                task.petId = petId;
                                if(!Objects.equals(task.description, "") && isValidDate(task.deadline))
                                {
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    final DatabaseReference tasksRef = rootRef.child("tasks");
                                    final String taskId = tasksRef.push().getKey();
                                    task.id = taskId;

                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            tasksRef.child(taskId).setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                                    Toast.makeText(tasksPerPet, "createNewTask:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                    if (!task.isSuccessful()){
                                                        Toast.makeText(tasksPerPet, "Unable to create new task: " + task.getException(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {
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
                                                }
                                            });
                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(context, "Invalid task data", Toast.LENGTH_SHORT).show();
                                }

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
