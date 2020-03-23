package com.example.goingroguedesign.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateNameActivity extends AppCompatActivity {
    String firstName, lastName, id;
    EditText etFirstName, etLastName;
    TextView confirmBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Boolean firstNameUpdated = false, lastNameUpdated = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        ((AppCompatActivity) UpdateNameActivity.this).getSupportActionBar().hide();

        TextView tvCancelUpdate = findViewById(R.id.tvCancelAdd);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        confirmBtn = findViewById(R.id.tvSubmit);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        getData();
        setData();
        tvCancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentReference addressRef = db.collection("Customer").document(id);
                addressRef
                        .update("customerFirstName", etFirstName.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                addressRef
                                        .update("customerLastName", etLastName.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(etFirstName.getText().toString())
                                                        .build();

                                                assert user != null;
                                                user.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(UpdateNameActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(UpdateNameActivity.this, MainActivity.class);
                                                                    intent.putExtra("id", 3);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(UpdateNameActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateNameActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                finish();;
                            }
                        });


            }
        });
    }

    private void getData() {
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("firstName")&&
                getIntent().hasExtra("lastName")) {
            id = getIntent().getStringExtra("id");
            firstName = getIntent().getStringExtra("firstName");
            lastName = getIntent().getStringExtra("lastName");
        } else {
            Toast.makeText(this, "Error Retrieving data.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        etFirstName.setText(firstName);
        etLastName.setText(lastName);
    }
}
