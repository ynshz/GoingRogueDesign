package com.example.goingroguedesign.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.utils.LoadingAnimation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePhoneActivity extends AppCompatActivity {

    String number, id;
    EditText etPhoneNumber;
    TextView confirmBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ValidateInput validateInput;
    LoadingAnimation loadingAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);
        ((AppCompatActivity) UpdatePhoneActivity.this).getSupportActionBar().hide();
        TextView tvCancelUpdate = findViewById(R.id.tvCancelAdd);
        confirmBtn = findViewById(R.id.tvSubmit);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        validateInput = new ValidateInput(UpdatePhoneActivity.this, etPhoneNumber);
        loadingAnimation = new LoadingAnimation(UpdatePhoneActivity.this);

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
                DocumentReference addressRef = db.collection("Customer").document(id);
                if (validateInput.validatePhoneNumber()){
                    addressRef
                            .update("customerPhoneNumber", etPhoneNumber.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loadingAnimation.closeLoadingAnimation();
                                    Toast.makeText(UpdatePhoneActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UpdatePhoneActivity.this, MainActivity.class);
                                    intent.putExtra("id", 3);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdatePhoneActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    finish();
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
                getIntent().hasExtra("phoneNumber")) {
            id = getIntent().getStringExtra("id");
            number = getIntent().getStringExtra("phoneNumber");
        } else {
            Toast.makeText(this, "Error Retrieving data.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        etPhoneNumber.setText(number);
    }
}
