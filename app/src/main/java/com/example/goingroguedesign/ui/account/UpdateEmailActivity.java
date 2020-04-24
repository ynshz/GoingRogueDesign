package com.example.goingroguedesign.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.utils.LoadingAnimation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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

public class UpdateEmailActivity extends AppCompatActivity {

    String id, email;
    EditText etEmail, etPassword;
    TextView confirmBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LoadingAnimation loadingAnimation;
    ValidateInput validateInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        ((AppCompatActivity) UpdateEmailActivity.this).getSupportActionBar().hide();

        TextView tvCancelUpdate = findViewById(R.id.tvCancelAdd);
        confirmBtn = findViewById(R.id.tvSubmit);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        loadingAnimation = new LoadingAnimation(UpdateEmailActivity.this);
        validateInput = new ValidateInput(UpdateEmailActivity.this, etEmail);
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
                loadingAnimation.openLoadingAnimation();
                if(validateInput.validateEmail()){
                    final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, etPassword.getText().toString());

                    mUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mUser.updateEmail(etEmail.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentReference addressRef = db.collection("Customer").document(id);
                                                        addressRef
                                                                .update("customerEmail", etEmail.getText().toString())
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        loadingAnimation.closeLoadingAnimation();
                                                                        Toast.makeText(UpdateEmailActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(UpdateEmailActivity.this, MainActivity.class);
                                                                        intent.putExtra("id", 3);
                                                                        startActivity(intent);

                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        loadingAnimation.closeLoadingAnimation();
                                                                        Toast.makeText(UpdateEmailActivity.this, "Server Error: Updating document failed", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    loadingAnimation.closeLoadingAnimation();
                                                    Toast.makeText(UpdateEmailActivity.this, "Server Error: Updating Login Email failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loadingAnimation.closeLoadingAnimation();
                                    Toast.makeText(UpdateEmailActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                                }
                            });


                } else {
                    loadingAnimation.closeLoadingAnimation();
                }

            }
        });
    }

    private void getData() {
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("email")) {
            id = getIntent().getStringExtra("id");
            email = getIntent().getStringExtra("email");
        } else {
            Toast.makeText(this, "Error Retrieving data.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        etEmail.setText(email);
    }
}
