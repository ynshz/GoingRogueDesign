package com.example.goingroguedesign.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.goingroguedesign.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddContractorActivity extends AppCompatActivity {

    TextView cancel, submit;
    EditText firstName, lastName, email, phone, address;
    Spinner type;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AlertDialog dialog;
    private static final String TAG = "AddContractorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contractor);
        ((AppCompatActivity) AddContractorActivity.this).getSupportActionBar().hide();
        cancel = findViewById(R.id.tvCancelAdd);
        submit = findViewById(R.id.tvSubmit);
        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);
        email = findViewById(R.id.etEmail);
        phone = findViewById(R.id.etPhoneNumber);
        address = findViewById(R.id.etAddress);
        type = findViewById(R.id.spinType);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddContractorActivity.this, ContractorActivity.class);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingAnimation();
                Map<String, Object> contractor = new HashMap<>();
                contractor.put("contractorFirstName", firstName.getText().toString());
                contractor.put("contractorLastName", lastName.getText().toString());
                contractor.put("contractorPhoneNumber", phone.getText().toString());
                contractor.put("contractorEmail", email.getText().toString());
                contractor.put("contractorAddress", address.getText().toString());
                contractor.put("contractorType", type.getSelectedItem().toString());
                contractor.put("customerID", mAuth.getUid());

                db.collection("Contractor").add(contractor)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                dialog.dismiss();
                                Toast.makeText(AddContractorActivity.this, "Successfully created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddContractorActivity.this, ContractorActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });



            }
        });
    }
    public void loadingAnimation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddContractorActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
}
