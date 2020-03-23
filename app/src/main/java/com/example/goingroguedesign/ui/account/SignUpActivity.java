package com.example.goingroguedesign.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "";
    ImageView ivBackButton;
    EditText etFirstName, etLastName, etMiddleInitial, etUsername, etEmail, etPassword, etRepeatPassword;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email, password, username, firstname, lastname;
    ValidateInput validateInput;
    Button signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ((AppCompatActivity) SignUpActivity.this).getSupportActionBar().hide();

        ivBackButton = findViewById(R.id.ivBackButton);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMiddleInitial = findViewById(R.id.etMiddleInitial);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etSignUpEmail);
        etPassword = findViewById(R.id.etSignUpPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        signUpButton = findViewById(R.id.btnSignUp);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        validateInput = new ValidateInput(
                SignUpActivity.this,
                etEmail,
                etPassword,
                etRepeatPassword,
                etUsername,
                etFirstName,
                etLastName
        );

        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpNewAccount();

            }
        });
    }

    public void signUpNewAccount() {
        boolean emailVerified = validateInput.validateEmail();
        boolean passwordVerified = validateInput.validatePassword();
        boolean repeatPasswordVerified = validateInput.validateRepeatPassword();
        boolean usernameVerified = validateInput.validateUsername();
        boolean firstnameVerified = validateInput.validateFirstName();
        boolean lastnameVerified = validateInput.validateLastName();

        if(emailVerified && passwordVerified && repeatPasswordVerified && usernameVerified && firstnameVerified && lastnameVerified){

            email = etEmail.getText().toString().trim();
            password = etPassword.getText().toString().trim();
            username = etUsername.getText().toString().trim();
            firstname = etFirstName.getText().toString().trim();
            lastname = etLastName.getText().toString().trim();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                final FirebaseUser user = mAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(firstname)
                                        .build();

                                assert user != null;
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Map<String, Object> mUser = new HashMap<>();
                                                    mUser.put("customerFirstName", firstname);
                                                    mUser.put("customerLastName", lastname);
                                                    mUser.put("customerUsername", username);
                                                    mUser.put("customerPhoneNumber", "");
                                                    mUser.put("customerType", "");
                                                    mUser.put("customerAddress", "");
                                                    mUser.put("customerEmail", email);
                                                    mUser.put("customerID", user.getUid());
                                                    mUser.put("timestamp", FieldValue.serverTimestamp());
                                                    db.collection("Customer").document(user.getUid()).set(mUser)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(SignUpActivity.this, "You have signed up successfully.",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(SignUpActivity.this, "Failed creating customer document.",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }
                                        });


                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUpActivity.this, "Unable to sign in with given information.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else {
            Toast.makeText(SignUpActivity.this, "Failed to sign up with given information.",
                    Toast.LENGTH_SHORT).show();
        }


    }
}
