package com.example.goingroguedesign.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.utils.LoadingAnimation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePasswordActivity extends AppCompatActivity {

    String email, password, newPassword, repeatPassword;
    EditText etPassword, etNewPassword, etRepeatPassword;
    TextView confirmBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LoadingAnimation loadingAnimation;
    ValidateInput validateInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ((AppCompatActivity) UpdatePasswordActivity.this).getSupportActionBar().hide();

        TextView tvCancelUpdate = findViewById(R.id.tvCancelAdd);
        confirmBtn = findViewById(R.id.tvSubmit);
        etPassword = findViewById(R.id.etPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        loadingAnimation = new LoadingAnimation(UpdatePasswordActivity.this);
        validateInput = new ValidateInput(UpdatePasswordActivity.this, etPassword, etNewPassword, etRepeatPassword);

        getData();

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
                if (validateInput.validateUpdatePassword()){
                    final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, etPassword.getText().toString());

                    assert mUser != null;
                    mUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mUser.updatePassword(etNewPassword.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        loadingAnimation.closeLoadingAnimation();
                                                        Toast.makeText(UpdatePasswordActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(UpdatePasswordActivity.this, MainActivity.class);
                                                        intent.putExtra("id", 3);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        loadingAnimation.closeLoadingAnimation();
                                                        Toast.makeText(UpdatePasswordActivity.this, "Cannot authenticate with the information entered.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        loadingAnimation.closeLoadingAnimation();
                                        Toast.makeText(UpdatePasswordActivity.this, "Cannot authenticate with the information entered.", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loadingAnimation.closeLoadingAnimation();
                                    Toast.makeText(UpdatePasswordActivity.this, "Cannot authenticate with the information entered.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    loadingAnimation.closeLoadingAnimation();
                }
            }
        });

    }
    private void getData() {
        if(getIntent().hasExtra("email")) {
            email = getIntent().getStringExtra("email");
        } else {
            Toast.makeText(this, "Error Retrieving data.", Toast.LENGTH_SHORT).show();
        }

    }
}
