package com.example.goingroguedesign.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    EditText etSignInEmail, etSignInPassword;
    CheckBox cbRememberMe;
    Button btnSignIn;
    TextView tvSignUp, tvReset;
    ImageView ivBackButton;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    ValidateInput validateInput;
    String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ((AppCompatActivity) SignInActivity.this).getSupportActionBar().hide();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            finish();
        } else {
            etSignInEmail = findViewById(R.id.etSignUpEmail);
            etSignInPassword = findViewById(R.id.etSignUpPassword);
            cbRememberMe = findViewById(R.id.cbRememberMe);
            btnSignIn = findViewById(R.id.btnSignUp);
            tvSignUp = findViewById(R.id.tvSignUp);
            tvReset = findViewById(R.id.tvReset);
            ivBackButton = findViewById(R.id.ivBackButton);

            validateInput = new ValidateInput(this, etSignInEmail, etSignInPassword);

            ivBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInAccount();

                }
            });

            tvSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }


    }

    public void signInAccount() {
        boolean emailVerified = validateInput.validateEmail();
        boolean passwordVerified = validateInput.validatePassword();

        if(emailVerified && passwordVerified){

            email = etSignInEmail.getText().toString().trim();
            password = etSignInPassword.getText().toString().trim();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignInActivity.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(SignInActivity.this, "Sign In failed", Toast.LENGTH_SHORT).show();

        }

    }
}
