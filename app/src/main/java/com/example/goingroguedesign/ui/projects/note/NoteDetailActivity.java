package com.example.goingroguedesign.ui.projects.note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.projects.ProjectDetailActivity;
import com.example.goingroguedesign.utils.LoadingAnimation;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.Map;

public class NoteDetailActivity extends AppCompatActivity {
    private static final String TAG = "NoteDetail";
    Uri imageUri;

    String id, name, description, url, filePath, newTitle, newDes;
    TextView textView, cancel, submit, delete;
    ImageView imageView, editImage, deleteImage;
    EditText etTitle, etDescription;
    CardView cvAddImage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int PICK_IMAGE = 100;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    boolean imageChanged = false, modified = false, imageDeleted = false;
    LoadingAnimation loadingAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ((AppCompatActivity) NoteDetailActivity.this).getSupportActionBar().hide();

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.ivNotePicture);
        editImage = findViewById(R.id.ivEditImage);
        deleteImage = findViewById(R.id.ivDeleteImage);
        cvAddImage = findViewById(R.id.cvAddImage);
        cancel = findViewById(R.id.tvCancelAdd);
        submit = findViewById(R.id.tvSubmit);
        delete = findViewById(R.id.tvDelete);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        loadingAnimation = new LoadingAnimation(NoteDetailActivity.this);

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
                                loadingAnimation.openLoadingAnimation();

                                if (!filePath.equals("null")){
                                    // Create a storage reference from our app
                                    StorageReference storageRef = storage.getReference();

                                    // Create a reference to the file to delete
                                    StorageReference desertRef = storageRef.child("notePictures/"+Uri.parse(filePath).getLastPathSegment());

                                    // Delete the file
                                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // File deleted successfully
                                            db.collection("Note").document(id)
                                                    .delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            loadingAnimation.closeLoadingAnimation();
                                                            finish();
                                                        }
                                                    });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Uh-oh, an error occurred!
                                        }
                                    });
                                } else {
                                    db.collection("Note").document(id)
                                            .delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    loadingAnimation.closeLoadingAnimation();
                                                    finish();
                                                }
                                            });
                                }


                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

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
                imageDeleted = true;
                imageUri = null;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingAnimation.openLoadingAnimation();
                newTitle= etTitle.getText().toString().trim();
                newDes = etDescription.getText().toString().trim();


                if (newTitle.isEmpty() || newDes.isEmpty()) {
                    loadingAnimation.closeLoadingAnimation();
                    Toast.makeText(NoteDetailActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();

                } else {
                    mAuth = FirebaseAuth.getInstance();
                    mUser = mAuth.getCurrentUser();

                    // Get a new write batch
                    final WriteBatch batch = db.batch();
                    final DocumentReference taskRef = db.collection("Note").document(id);

                    if(imageChanged && imageUri!=null) {
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
                                    batch.update(taskRef, "notePictureUrl", downloadUri.toString());
                                    batch.update(taskRef, "notePictureFilePath", imageUri.toString());
                                    batch.update(taskRef, "noteModifiedAt", FieldValue.serverTimestamp());

                                    if(!newTitle.equals(name)) {
                                        batch.update(taskRef, "noteTitle", newTitle);
                                    }

                                    if(!newDes.equals(description)) {
                                        batch.update(taskRef, "noteContent", newDes);
                                    }

                                    if(!filePath.equals("null")){
                                        // Create a storage reference from our app
                                        StorageReference storageRef = storage.getReference();

                                        // Create a reference to the file to delete
                                        StorageReference desertRef = storageRef.child("notePictures/"+Uri.parse(filePath).getLastPathSegment());

                                        // Delete the file
                                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // File deleted successfully
                                                // Commit the batch
                                                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        loadingAnimation.closeLoadingAnimation();
                                                        finish();
                                                    }
                                                });
                                            }
                                        })/*.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Uh-oh, an error occurred!
                                            }
                                        })*/;
                                    } else {
                                        // Commit the batch
                                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                loadingAnimation.closeLoadingAnimation();
                                                finish();
                                            }
                                        });
                                    }

                                }
                            }
                        });
                    } else {


                        if(!newTitle.equals(name)) {
                            batch.update(taskRef, "noteTitle", newTitle);
                            modified = true;
                        }

                        if(!newDes.equals(description)) {
                            batch.update(taskRef, "noteContent", newDes);
                            modified = true;
                        }

                        if (imageDeleted && !url.equals("null")) {

                            batch.update(taskRef, "notePictureUrl", "null");
                            batch.update(taskRef, "notePictureFilePath", "null");
                            // Create a storage reference from our app
                            StorageReference storageRef = storage.getReference();

                            // Create a reference to the file to delete
                            StorageReference desertRef = storageRef.child("notePictures/"+Uri.parse(filePath).getLastPathSegment());

                            // Delete the file
                            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // File deleted successfully
                                    batch.update(taskRef, "noteModifiedAt", FieldValue.serverTimestamp());
                                    // Commit the batch
                                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            loadingAnimation.closeLoadingAnimation();
                                            finish();
                                        }
                                    });
                                }
                            })/*.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Uh-oh, an error occurred!
                                }
                            })*/;
                        }else if(modified) {
                            batch.update(taskRef, "noteModifiedAt", FieldValue.serverTimestamp());
                            // Commit the batch
                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    loadingAnimation.closeLoadingAnimation();
                                    finish();
                                }
                            });
                        } else {
                            loadingAnimation.closeLoadingAnimation();
                            finish();
                        }




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
            imageChanged = true;
            assert data != null;
            imageUri = data.getData();
            imageView.setImageURI(imageUri);


        }
    }
    private void getData() {
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("name") &&
                getIntent().hasExtra("content") &&
                getIntent().hasExtra("url") &&
                getIntent().hasExtra("filePath")) {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            description = getIntent().getStringExtra("content");
            url = getIntent().getStringExtra("url");
            filePath = getIntent().getStringExtra("filePath");
        } else {
            Toast.makeText(this, "Error Retrieving data.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        textView.setText("Update " + name);
        etTitle.setText(name);
        etDescription.setText(description);
        if(!url.equals("null")) {
            cvAddImage.setVisibility(View.INVISIBLE);
            Glide.with(NoteDetailActivity.this).load(url).centerCrop().into(imageView);

        } else {
            editImage.setVisibility(View.INVISIBLE);
            deleteImage.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.GONE);
            cvAddImage.setVisibility(View.VISIBLE);
        }

    }
}
