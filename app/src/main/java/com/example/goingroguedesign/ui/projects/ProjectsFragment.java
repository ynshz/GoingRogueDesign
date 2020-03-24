package com.example.goingroguedesign.ui.projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.account.ContractorActivity;
import com.example.goingroguedesign.ui.account.ContractorAdapter;
import com.example.goingroguedesign.ui.account.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProjectsFragment extends Fragment {
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> id = new ArrayList<String>();
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> address = new ArrayList<String>();
    ArrayList<String> status = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> lead = new ArrayList<String>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_projects, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        recyclerView = root.findViewById(R.id.rvProject);

        if(mUser != null){
            db.collection("Project")
                    .whereEqualTo("customerID", mUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    id.add(document.getString("customerID"));
                                    title.add(document.getString("projectName"));
                                    address.add(document.getString("projectAddress"));
                                    status.add(document.getString("projectStatus"));
                                    date.add(document.getString("projectStartDate"));
                                    lead.add(document.getString("managerID"));
                                }
                            } else {
                                Toast.makeText(getActivity(), "Failed to read projects", Toast.LENGTH_SHORT).show();
                            }
                            ProjectAdapter projectAdapter = new ProjectAdapter(getActivity(), id, title, address, status, date, lead);
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
