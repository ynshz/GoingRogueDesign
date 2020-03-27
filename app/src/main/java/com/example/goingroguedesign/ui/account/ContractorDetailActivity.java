package com.example.goingroguedesign.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.projects.note.NoteDetailActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;

public class ContractorDetailActivity extends AppCompatActivity {
    String firstName, lastName, phone, email, address, id, type;
    String newFirstName, newLastName, newPhone, newEmail, newAddress, newType;
    EditText etFirstName, etLastName, etAddress, etEmail, etPhone;
    TextView tvConfirm, tvCancel, tvDelete;
    ImageView ivBack;
    Spinner spinType;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    androidx.appcompat.app.AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_detail);
        ((AppCompatActivity) ContractorDetailActivity.this).getSupportActionBar().hide();

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhoneNumber);
        tvConfirm = findViewById(R.id.tvSubmit);
        tvCancel = findViewById(R.id.tvCancelAdd);
        spinType = findViewById(R.id.spinType);
        tvDelete = findViewById(R.id.tvDelete);

        getData();
        setData();

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingAnimation();
                newFirstName = etFirstName.getText().toString().trim();
                newLastName = etLastName.getText().toString().trim();
                newAddress = etAddress.getText().toString().trim();
                newPhone = etPhone.getText().toString().trim();
                newEmail = etEmail.getText().toString().trim();
                newType = spinType.getSelectedItem().toString();

                if (newFirstName.isEmpty() || newLastName.isEmpty() || newAddress.isEmpty() || newPhone.isEmpty() || newEmail.isEmpty() || newType.isEmpty()){
                    Toast.makeText(ContractorDetailActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();

                } else {

                    //TODO: not good, use batch update
                    final DocumentReference contractorRef = db.collection("Contractor").document(id);
                    contractorRef.update("contractorFirstName", newFirstName)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    contractorRef.update("contractorLastName", newLastName)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    contractorRef.update("contractorAddress", newAddress)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    contractorRef.update("contractorPhoneNumber", newPhone)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    contractorRef.update("contractorEmail", newEmail)
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {
                                                                                                    contractorRef.update("contractorType", newType)
                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(Void aVoid) {
                                                                                                                    dialog.dismiss();
                                                                                                                    finish();
                                                                                                                }
                                                                                                            });
                                                                                                }
                                                                                            });
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                }
                                            });
                                }
                            });
                }


            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ContractorDetailActivity.this)
                        .setTitle("Permanently Deleting a Contractor")
                        .setMessage("Are you sure you want to permanently delete contractor " + firstName + " " + lastName + "?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Contractor").document(id)
                                        .delete();
                                finish();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }

    private void getData() {
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("firstName") &&
                getIntent().hasExtra("lastName") &&
                getIntent().hasExtra("address") &&
                getIntent().hasExtra("email") &&
                getIntent().hasExtra("phone") &&
                getIntent().hasExtra("type")) {
            id = getIntent().getStringExtra("id");
            firstName = getIntent().getStringExtra("firstName");
            lastName = getIntent().getStringExtra("lastName");
            address = getIntent().getStringExtra("address");
            email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone");
            type = getIntent().getStringExtra("type");
        } else {
            Toast.makeText(this, "Error Retrieving data.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etEmail.setText(email);
        etPhone.setText(phone);
        etAddress.setText(address);
        spinType.setSelection(getTypeIndex(type));

    }

    //TODO: not good, change after updating type values!!
    private int getTypeIndex(String s) {
        char c = s.charAt(s.length()-1);
        return (Integer.parseInt(String.valueOf(c))-1);
    }

    public void loadingAnimation() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ContractorDetailActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
}
