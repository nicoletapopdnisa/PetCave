package com.nicol.petcave_final;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by nicol on 1/2/2018.
 */

public class OnLongClickListenerTask implements View.OnLongClickListener {

    private static TasksPerPet tasksPerPet;
    private String petId;
    private String id;
    Context context;
    ArrayList<Task> tasks;

    public OnLongClickListenerTask(ArrayList<Task> tasks, TasksPerPet tasksPerPet, String petId) {
        this.petId = petId;
        this.tasksPerPet = tasksPerPet;
        this.tasks = tasks;
    }

    @Override
    public boolean onLongClick(View view) {
        context = view.getContext();
        String content = view.getTag().toString();
        id = content;

        final CharSequence[] items = { "Edit", "Delete" };
        new AlertDialog.Builder(context).setTitle("This Task")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(DialogInterface dialog, int item) {

                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        final DatabaseReference tasksRef = rootRef.child("tasks");
                        if (item == 0) {
                            editTask(id, tasksRef);
                        }
                        else if (item == 1) {

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                    tasksRef.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                            Toast.makeText(tasksPerPet, "deleteTask:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            if(!task.isSuccessful())
                                            {
                                                Toast.makeText(context, "Unable to delete pet task record.", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Pet task record was deleted.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                            /*
                            boolean deleteSuccessful = new TableControllerTasks(context).delete(id);

                            if (deleteSuccessful){
                                Toast.makeText(context, "Pet task was deleted.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Unable to delete pet task.", Toast.LENGTH_SHORT).show();
                            }
                            */

                        }
                        dialog.dismiss();

                    }
                }).show();

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void editTask(final String taskId, final DatabaseReference tasksRef) {

        Task task = new Task();
        for(Task t: tasks)
        {
            if(Objects.equals(task.id, id))
            {
                task = t;
            }
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.task_input_form, null, false);

        final EditText editTextTaskDescription= (EditText) formElementsView.findViewById(R.id.editTextTaskDescription);

        editTextTaskDescription.setText(task.description);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Edit Task")
                .setPositiveButton("Save Changes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final Task newObjectTask = new Task();
                                newObjectTask.id = taskId;
                                newObjectTask.description = editTextTaskDescription.getText().toString();
                                newObjectTask.petId = petId;

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        tasksRef.child(petId).setValue(newObjectTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                                Toast.makeText(tasksPerPet, "updateTask:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                if (!task.isSuccessful()){
                                                    Toast.makeText(tasksPerPet, "Unable to update pet task: " + task.getException(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(context, "Pet task record was updated.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });

                                dialog.cancel();
                            }

                        }).show();
    }
}
