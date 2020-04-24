package com.example.goingroguedesign.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.utils.LoadingAnimation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class UpdateUsernameActivity extends AppCompatActivity {

    String username, id;
    EditText etUsername;
    TextView confirmBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ValidateInput validateInput;
    LoadingAnimation loadingAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_username);
        ((AppCompatActivity) UpdateUsernameActivity.this).getSupportActionBar().hide();

        TextView tvCancelUpdate = findViewById(R.id.tvCancelAdd);
        confirmBtn = findViewById(R.id.tvSubmit);
        etUsername = findViewById(R.id.etUsername);
        validateInput = new ValidateInput(UpdateUsernameActivity.this, etUsername);
        loadingAnimation = new LoadingAnimation(UpdateUsernameActivity.this);
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
                if (validateInput.validateUsername()) {
                    DocumentReference addressRef = db.collection("Customer").document(id);
                    //final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    //final FirebaseUser user = mAuth.getCurrentUser();
                    addressRef
                            .update("customerUsername", etUsername.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    /*
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(etUsername.getText().toString())
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {*/
                                                        loadingAnimation.closeLoadingAnimation();
                                                        Toast.makeText(UpdateUsernameActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(UpdateUsernameActivity.this, MainActivity.class);
                                                        intent.putExtra("id", 3);
                                                        startActivity(intent);

                                /*}
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(UpdateUsernameActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });

                                 */

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateUsernameActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }


            }

        });
    }

    private void getData() {
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("username")) {
            id = getIntent().getStringExtra("id");
            username = getIntent().getStringExtra("username");
        } else {
            Toast.makeText(this, "Error Retrieving data.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        etUsername.setText(username);
    }

}
