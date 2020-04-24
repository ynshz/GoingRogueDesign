package com.example.goingroguedesign.ui.projects.note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.account.SignInActivity;
import com.example.goingroguedesign.ui.projects.ProjectDetailActivity;
import com.example.goingroguedesign.utils.LoadingAnimation;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddNoteActivity extends AppCompatActivity {
    Uri imageUri;

    TextView tvCancel, tvSubmit;
    EditText etTitle, etContent;
    String id;
    String title, content;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CardView cvAddImage;
    ImageView imageView, editImage, deleteImage;
    private static final int PICK_IMAGE = 100;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    boolean imageAdded = false;
    LoadingAnimation loadingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ((AppCompatActivity) AddNoteActivity.this).getSupportActionBar().hide();
        tvCancel = findViewById(R.id.tvCancelAdd);
        tvSubmit = findViewById(R.id.tvSubmit);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etDescription);
        imageView = findViewById(R.id.ivNotePicture);
        editImage = findViewById(R.id.ivEditImage);
        deleteImage = findViewById(R.id.ivDeleteImage);
        cvAddImage = findViewById(R.id.cvAddImage);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView.setVisibility(View.GONE);
        editImage.setVisibility(View.INVISIBLE);
        deleteImage.setVisibility(View.INVISIBLE);
        cvAddImage.setVisibility(View.VISIBLE);

        loadingAnimation = new LoadingAnimation(AddNoteActivity.this);

        if(getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        } else {
            Toast.makeText(AddNoteActivity.this, "Error reading Project", Toast.LENGTH_SHORT).show();
            finish();
        }

        cvAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(View.GONE);
                editImage.setVisibility(View.INVISIBLE);
                deleteImage.setVisibility(View.INVISIBLE);
                cvAddImage.setVisibility(View.VISIBLE);
                imageUri = null;
                imageAdded = false;
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingAnimation.openLoadingAnimation();
                title= etTitle.getText().toString().trim();
                content = etContent.getText().toString().trim();
                if (title.isEmpty() || content.isEmpty()) {
                    loadingAnimation.closeLoadingAnimation();
                    Toast.makeText(AddNoteActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();

                } else {
                    final Map<String, Object> note = new HashMap<>();
                    note.put("noteTitle", title);
                    note.put("noteContent", content);
                    note.put("noteCreatedAt", FieldValue.serverTimestamp());
                    note.put("noteModifiedAt", FieldValue.serverTimestamp());
                    note.put("projectID", id);

                    if (imageAdded && imageUri!=null) {
                        StorageReference storageRef = storage.getReference();
                        final StorageReference picRef = storageRef.child("notePictures/"+imageUri.getLastPathSegment());
                        UploadTask uploadTask = picRef.putFile(imageUri);
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return picRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    final Uri downloadUri = task.getResult();
                                    note.put("notePictureUrl", downloadUri.toString());
                                    note.put("notePictureFilePath", imageUri.toString());
                                    db.collection("Note").add(note)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    loadingAnimation.closeLoadingAnimation();
                                                    finish();
                                                }
                                            });
                                }
                            }
                        });
                    } else {
                        note.put("notePictureUrl", "null");
                        note.put("notePictureFilePath", "null");
                        db.collection("Note").add(note)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        loadingAnimation.closeLoadingAnimation();
                                        finish();
                                    }
                                });
                    }




                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageView.setVisibility(View.VISIBLE);
            editImage.setVisibility(View.VISIBLE);
            deleteImage.setVisibility(View.VISIBLE);
            cvAddImage.setVisibility(View.INVISIBLE);
            assert data != null;
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageAdded = true;

        }
    }
}
