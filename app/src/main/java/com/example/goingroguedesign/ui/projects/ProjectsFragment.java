package com.example.goingroguedesign.ui.projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.account.SignInActivity;
import com.example.goingroguedesign.utils.GetRandString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProjectsFragment extends Fragment {
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> id = new ArrayList<String>();
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> address = new ArrayList<String>();
    ArrayList<String> status = new ArrayList<String>();
    ArrayList<Date> date = new ArrayList<java.util.Date>();
    ArrayList<String> lead = new ArrayList<String>();
    ArrayList<String> contractor = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_projects, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        recyclerView = root.findViewById(R.id.rvProject);
        FloatingActionButton fab = root.findViewById(R.id.fab);
        if(mUser != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetRandString g = new GetRandString(getActivity());

                    Map<String, Object>dummyProject = new HashMap<>();
                    dummyProject.put("projectMainContractorID", g.getRandAddress());
                    dummyProject.put("projectAddress", g.getRandAddress());
                    dummyProject.put("projectName", g.getHotel());
                    dummyProject.put("projectStartDate", FieldValue.serverTimestamp());
                    dummyProject.put("projectType", g.getStatus());
                    dummyProject.put("managerName", "Shize Yuan");
                    dummyProject.put("customerID", mAuth.getUid());
                    dummyProject.put("projectMainContractorName", g.getFirstName() + " " +g.getLastName());
                    dummyProject.put("customerEmail", mUser.getEmail());
                    dummyProject.put("managerID", "eUG5EtwnxPRBA2JZU9rp");
                    dummyProject.put("projectDescription", g.getHotel()+" "+g.getRandAddress());
                    dummyProject.put("projectID", "thisShouldBeProjectID");
                    dummyProject.put("projectLatitude", 50.88);
                    dummyProject.put("projectLongitude", -125.88);
                    dummyProject.put("projectCreatedDate", FieldValue.serverTimestamp());



                    db.collection("Project").add(dummyProject);
                }
            });
            db.collection("Project")
                    .whereEqualTo("customerID", mUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    id.add(document.getId());
                                    title.add(document.getString("projectName"));
                                    address.add(document.getString("projectAddress"));
                                    status.add(document.getString("projectType"));
                                    date.add(document.getDate("projectStartDate"));
                                    lead.add(document.getString("managerName"));
                                    contractor.add(document.getString("projectMainContractorName"));

                                }
                            } else {
                                Toast.makeText(getActivity(), "Failed to read projects", Toast.LENGTH_SHORT).show();
                            }
                            ProjectAdapter projectAdapter = new ProjectAdapter(getActivity(), id, title, address, status, date, lead, contractor);
                            recyclerView.setAdapter(projectAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    });
        } else {
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            startActivity(intent);
        }

        return root;
    }
}
