package com.example.goingroguedesign.ui.projects.note;

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

public class NoteDetailActivity extends AppCompatActivity {
    private static final String TAG = "NoteDetail";

    String id, name, description, newTitle, newDes;
    TextView textView, cancel, submit, delete;
    EditText etTitle, etDescription;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ((AppCompatActivity) NoteDetailActivity.this).getSupportActionBar().hide();

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
                new AlertDialog.Builder(NoteDetailActivity.this)
                        .setTitle("Permanently Deleting a Note")
                        .setMessage("Are you sure you want to permanently delete note: " + name + " ?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Note").document(id)
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
                    Toast.makeText(NoteDetailActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(NoteDetailActivity.this, newTitle + id, Toast.LENGTH_SHORT).show();

                    final DocumentReference taskRef = db.collection("Note").document(id);
                    taskRef.update("noteTitle", newTitle)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    taskRef.update("noteContent", newDes)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    taskRef.update("noteModifiedAt", FieldValue.serverTimestamp())
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                   finish();
                                                                }
                                                            });

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
                getIntent().hasExtra("content")) {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            description = getIntent().getStringExtra("content");
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
