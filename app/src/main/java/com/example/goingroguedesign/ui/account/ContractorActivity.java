package com.example.goingroguedesign.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goingroguedesign.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContractorActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    ArrayList<String> contractorName = new ArrayList<String>();
    ArrayList<String> contractorID = new ArrayList<String>();
    ArrayList<String> contractorPhoneNumber = new ArrayList<String>();
    ArrayList<String> contractorEmail = new ArrayList<String>();
    ArrayList<String> contractorAddress = new ArrayList<String>();
    ArrayList<String> contractorType = new ArrayList<String>();
    ImageView backBtn, addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor);
        ((AppCompatActivity) ContractorActivity.this).getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        recyclerView = findViewById(R.id.recyclerViewContractor);
        backBtn = findViewById(R.id.ivBackButton);
        addBtn = findViewById(R.id.ivAddContractor);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContractorActivity.this, AddContractorActivity.class);
                startActivity(intent);
                finish();
            }
        });
        db.collection("Contractor")
                .whereEqualTo("customerID", mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                contractorName.add(document.getString("contractorFirstName") + " " + document.getString("contractorLastName"));
                                contractorID.add(document.getString("contractorID"));
                                contractorPhoneNumber.add(document.getString("contractorPhoneNumber"));
                                contractorEmail.add(document.getString("contractorEmail"));
                                contractorAddress.add(document.getString("contractorAddress"));
                                contractorType.add(document.getString("contractorType"));
                            }
                        } else {
                            Toast.makeText(ContractorActivity.this, "Failed to read posts", Toast.LENGTH_SHORT).show();
                        }
                        ContractorAdapter contractorAdapter = new ContractorAdapter(ContractorActivity.this, contractorID, contractorName, contractorPhoneNumber, contractorEmail, contractorAddress, contractorType);
                        recyclerView.setAdapter(contractorAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ContractorActivity.this));
                    }
                });

    }
}
