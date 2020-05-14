package com.example.goingroguedesign.ui.projects.task;

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

public class TaskFragment extends Fragment {
    RecyclerView recyclerView;
    private static final String TAG = "TaskFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    String id = "";
    CardView cvAddTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task, container, false);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        recyclerView = root.findViewById(R.id.recyclerViewDocument);
        cvAddTask = root.findViewById(R.id.cvAddTask);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("projectID");
            loadTask(id);

            FloatingActionButton fab = root.findViewById(R.id.fab);
            fab.setVisibility(View.GONE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetRandString g = new GetRandString(getActivity());

                    Map<String, Object> dummy = new HashMap<>();

                    dummy.put("taskName", g.getLastName()+" "+ g.getHotel());
                    dummy.put("taskDescription", g.getContent());
                    dummy.put("taskCreatedDate", FieldValue.serverTimestamp());
                    dummy.put("taskResolved", false);
                    dummy.put("taskResolvedDate", FieldValue.serverTimestamp());
                    dummy.put("projectID", id);
                    dummy.put("taskDueDate", FieldValue.serverTimestamp());

                    //not going to use, here for keeping consistent with web/ios
                    dummy.put("customerEmail", mUser.getEmail());
                    dummy.put("taskType", "ongoing");
                    dummy.put("taskID", "thisShouldBeTaskID");

                    db.collection("Task").add(dummy)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    loadTask(id);

                                }
                            });
                }
            });

            cvAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

        }


        return root;
    }

    public void loadTask (String s) {
        final ArrayList<String> name = new ArrayList<String>();
        final ArrayList<String> description = new ArrayList<String>();
        final ArrayList<Date> date = new ArrayList<java.util.Date>();
        final ArrayList<String> id = new ArrayList<String>();
        final ArrayList<Boolean> resolved = new ArrayList<Boolean>();
        final ArrayList<Date> dueDate = new ArrayList<Date>();

        db.collection("Task").whereEqualTo("projectID", s).orderBy("taskCreatedDate", Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                name.add(document.getString("taskName"));
                                date.add(document.getDate("taskCreatedDate"));
                                description.add(document.getString("taskDescription"));
                                id.add(document.getId());
                                resolved.add(document.getBoolean("taskResolved"));
                                dueDate.add(document.getDate("taskDueDate"));
                            }
                        } else {
                            Toast.makeText(getActivity(), "Failed to read task", Toast.LENGTH_SHORT).show();

                        }
                        TaskAdapter taskAdapter = new TaskAdapter(getActivity(), id, name, description, date, resolved, dueDate);
                        recyclerView.setAdapter(taskAdapter);
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
        loadTask(id);
    }
}
