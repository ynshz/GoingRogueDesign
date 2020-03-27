package com.example.goingroguedesign.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goingroguedesign.R;
import com.example.goingroguedesign.utils.GetRandString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
    AlertDialog dialog;

    ImageView backBtn, addBtn;

    private static final String TAG = ContractorActivity.class.getSimpleName();
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
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetRandString g = new GetRandString(ContractorActivity.this);

                Map<String, Object> dummy = new HashMap<>();
                dummy.put("contractorFirstName", g.getFirstName());
                dummy.put("contractorLastName", g.getLastName());
                dummy.put("contractorPhoneNumber", g.getPhoneNumber());
                dummy.put("contractorEmail", g.getEmail());
                dummy.put("contractorAddress", g.getRandAddress());
                dummy.put("contractorType", g.getContractorType());
                dummy.put("customerID", mAuth.getUid());

                db.collection("Contractor").add(dummy);
            }
        });

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

        loadContractor();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContractor();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    private void loadContractor () {
        final ArrayList<String> contractorFirstName = new ArrayList<String>();
        final ArrayList<String> contractorLastName = new ArrayList<String>();

        final ArrayList<String> contractorID = new ArrayList<String>();
        final ArrayList<String> contractorPhoneNumber = new ArrayList<String>();
        final ArrayList<String> contractorEmail = new ArrayList<String>();
        final ArrayList<String> contractorAddress = new ArrayList<String>();
        final ArrayList<String> contractorType = new ArrayList<String>();
        db.collection("Contractor")
                .whereEqualTo("customerID", mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                contractorFirstName.add(document.getString("contractorFirstName"));
                                contractorLastName.add(document.getString("contractorLastName"));
                                contractorID.add(document.getId());
                                contractorPhoneNumber.add(document.getString("contractorPhoneNumber"));
                                contractorEmail.add(document.getString("contractorEmail"));
                                contractorAddress.add(document.getString("contractorAddress"));
                                contractorType.add(document.getString("contractorType"));
                            }
                        } else {
                            Toast.makeText(ContractorActivity.this, "Failed to read posts", Toast.LENGTH_SHORT).show();
                        }
                        ContractorAdapter contractorAdapter = new ContractorAdapter(ContractorActivity.this, contractorID, contractorFirstName, contractorLastName, contractorPhoneNumber, contractorEmail, contractorAddress, contractorType);
                        recyclerView.setAdapter(contractorAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ContractorActivity.this));
                    }
                });
    }
    public void loadingAnimation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContractorActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
}
