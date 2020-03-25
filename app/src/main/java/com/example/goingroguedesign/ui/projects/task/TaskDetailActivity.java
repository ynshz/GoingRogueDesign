package com.example.goingroguedesign.ui.projects.task;

import androidx.appcompat.app.AppCompatActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.projects.ProjectDetailActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class TaskDetailActivity extends AppCompatActivity {

    String id, name, description, newTitle, newDes;
    TextView textView, cancel, submit, delete;
    EditText etTitle, etDescription;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        ((AppCompatActivity) TaskDetailActivity.this).getSupportActionBar().hide();

        textView = findViewById(R.id.textView);
        cancel = findViewById(R.id.tvCancelAdd);
        submit = findViewById(R.id.tvSubmit);
        delete = findViewById(R.id.tvDelete);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);

        getData();
        setData();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TaskDetailActivity.this)
                        .setTitle("Permanently Deleting a Task")
                        .setMessage("Are you sure you want to permanently delete task: " + name + " ?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Task").document(id)
                                        .delete();
                                finish();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                newTitle= etTitle.getText().toString().trim();
                newDes = etDescription.getText().toString().trim();
                if (newTitle.isEmpty() || newDes.isEmpty()) {
                    Toast.makeText(TaskDetailActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();

                } else {
                    final DocumentReference taskRef = db.collection("Task").document(id);
                    taskRef.update("taskName", newTitle)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    taskRef.update("taskDescription", newDes)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    finish();

                                                }
                                            });
                                }
                            });

                }
            }
        });
    }

    private void getData() {
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("name") &&
                getIntent().hasExtra("description")) {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            description = getIntent().getStringExtra("description");
        } else {
            Toast.makeText(this, "Error Retrieving data.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        textView.setText("Update " + name);
        etTitle.setText(name);
        etDescription.setText(description);
    }
}
