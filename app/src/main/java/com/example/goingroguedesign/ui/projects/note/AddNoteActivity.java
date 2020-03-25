package com.example.goingroguedesign.ui.projects.note;

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

public class AddNoteActivity extends AppCompatActivity {

    TextView tvCancel, tvSubmit;
    EditText etTitle, etContent;
    String id;
    String title, content;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ((AppCompatActivity) AddNoteActivity.this).getSupportActionBar().hide();
        tvCancel = findViewById(R.id.tvCancelAdd);
        tvSubmit = findViewById(R.id.tvSubmit);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etDescription);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        } else {
            Toast.makeText(AddNoteActivity.this, "Error reading Project", Toast.LENGTH_SHORT).show();
            finish();
        }
        tvSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                title= etTitle.getText().toString().trim();
                content = etContent.getText().toString().trim();
                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(AddNoteActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();

                } else {
                    Map<String, Object> note = new HashMap<>();
                    note.put("noteTitle", title);
                    note.put("noteContent", content);
                    note.put("noteCreatedAt", FieldValue.serverTimestamp());
                    note.put("noteModifiedAt", FieldValue.serverTimestamp());
                    note.put("projectID", id);

                    db.collection("Note").add(note)
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
