package com.example.goingroguedesign.ui.projects.note;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.projects.invoice.InvoiceAdapter;
import com.example.goingroguedesign.utils.GetRandString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query.Direction;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NoteFragment extends Fragment {
    RecyclerView recyclerView;
    private static final String TAG = "NoteFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    String id = "";
    CardView cvAddNote;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        recyclerView = root.findViewById(R.id.recyclerViewDocument);
        cvAddNote = root.findViewById(R.id.cvAddTask);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("projectID");
            loadNote(id);

            FloatingActionButton fab = root.findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetRandString g = new GetRandString(getActivity());

                    Map<String, Object> dummy = new HashMap<>();
                    dummy.put("noteTitle", g.getLastName()+g.getLastName());
                    dummy.put("noteContent", g.getLastName()+g.getLastName()+g.getLastName()+g.getLastName()+g.getLastName()+g.getLastName()+g.getLastName()+g.getLastName()+g.getLastName()+g.getLastName()+g.getLastName()+g.getLastName()+g.getLastName());
                    dummy.put("noteCreatedAt", FieldValue.serverTimestamp());
                    dummy.put("noteModifiedAt", FieldValue.serverTimestamp());
                    dummy.put("projectID", id);

                    db.collection("Note").add(dummy)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    loadNote(id);

                                }
                            });
                }
            });

            cvAddNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddNoteActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

        }


        return root;
    }

    public void loadNote (String s) {
        final ArrayList<String> name = new ArrayList<String>();
        final ArrayList<String> content = new ArrayList<String>();
        final ArrayList<Date> createdAt = new ArrayList<java.util.Date>();
        final ArrayList<Date> modifiedAt = new ArrayList<java.util.Date>();
        final ArrayList<String> id = new ArrayList<String>();
        db.collection("Note").whereEqualTo("projectID", s).orderBy("noteModifiedAt", Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                name.add(document.getString("noteTitle"));
                                createdAt.add(document.getDate("noteCreatedAt"));
                                modifiedAt.add(document.getDate("noteModifiedAt"));
                                content.add(document.getString("noteContent"));
                                id.add(document.getId());
                            }
                        } else {
                            Toast.makeText(getActivity(), "Failed to read note", Toast.LENGTH_SHORT).show();

                        }
                        NoteAdapter noteAdapter = new NoteAdapter(getActivity(), id, name, content, createdAt, modifiedAt);
                        recyclerView.setAdapter(noteAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNote(id);
    }
}
