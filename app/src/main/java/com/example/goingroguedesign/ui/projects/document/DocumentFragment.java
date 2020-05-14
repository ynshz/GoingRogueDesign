package com.example.goingroguedesign.ui.projects.document;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.goingroguedesign.R;
import com.example.goingroguedesign.utils.GetRandString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DocumentFragment extends Fragment {
    RecyclerView recyclerView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_document, container, false);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        recyclerView = root.findViewById(R.id.recyclerViewDocument);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            final String id = bundle.getString("projectID");

            loadDocument(id);
            FloatingActionButton fab = root.findViewById(R.id.fab);
            fab.setVisibility(View.GONE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetRandString g = new GetRandString(getActivity());

                    Map<String, Object> dummyDocument = new HashMap<>();
                    dummyDocument.put("documentName", g.getLastName()+g.getLastName());
                    dummyDocument.put("documentDueDate", FieldValue.serverTimestamp());
                    dummyDocument.put("documentCompleted", false);
                    dummyDocument.put("documentCompletedAt", FieldValue.serverTimestamp());
                    dummyDocument.put("documentLink", "");
                    dummyDocument.put("projectID", id);
                    dummyDocument.put("customerEmail",mUser.getEmail());
                    dummyDocument.put("documentID","thisShouldBeDocumentID");
                    dummyDocument.put("documentType","doc");
                    dummyDocument.put("userID",mUser.getUid());

                    db.collection("Document").add(dummyDocument)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    loadDocument(id);

                                }
                            });
                }
            });
        } else {
            Toast.makeText(getActivity(), "Failed to retrieve documents.", Toast.LENGTH_SHORT).show();
        }


        return root;
    }

    public void loadDocument (String s) {
        final ArrayList<String> name = new ArrayList<String>();
        final ArrayList<Date> date = new ArrayList<java.util.Date>();
        final ArrayList<String> url = new ArrayList<String>();
        final ArrayList<String> id = new ArrayList<String>();
        final ArrayList<Boolean> complete = new ArrayList<Boolean>();
        db.collection("Document").whereEqualTo("projectID", s)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                name.add(document.getString("documentName"));
                                date.add(document.getDate("documentCreatedTime"));
                                url.add(document.getString("documentLink"));
                                id.add(document.getId());
                                complete.add(document.getBoolean("documentCompleted"));

                            }
                        } else {
                            Toast.makeText(getActivity(), "Failed to read document", Toast.LENGTH_SHORT).show();
                        }
                        DocumentAdapter documentAdapter = new DocumentAdapter(getActivity(), id, name, date, url, complete);
                        recyclerView.setAdapter(documentAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                });
    }


}