package com.example.goingroguedesign.ui.projects.task;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.account.SignInActivity;
import com.example.goingroguedesign.ui.projects.ProjectDetailActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {

    TextView tvCancel, tvSubmit;
    EditText etTitle, etDescription;
    String id;
    String title, description;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ((AppCompatActivity) AddTaskActivity.this).getSupportActionBar().hide();
        tvCancel = findViewById(R.id.tvCancelAdd);
        tvSubmit = findViewById(R.id.tvSubmit);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        } else {
            Toast.makeText(AddTaskActivity.this, "Error reading Project", Toast.LENGTH_SHORT).show();
            finish();
        }
        tvSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                title= etTitle.getText().toString().trim();
                description = etDescription.getText().toString().trim();
                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AddTaskActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();

                } else {
                    Map<String, Object> task = new HashMap<>();
                    task.put("taskName", title);
                    task.put("taskDescription", description);
                    task.put("taskCreatedAt", FieldValue.serverTimestamp());
                    task.put("taskResolved", false);
                    task.put("taskResolvedAt", FieldValue.serverTimestamp());
                    task.put("projectID", id);

                    db.collection("Task").add(task)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    finish();
                                }
                            });

                }
            }
        });
    }
}
