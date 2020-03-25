package com.example.goingroguedesign.ui.projects.invoice;

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
import com.example.goingroguedesign.ui.projects.invoice.InvoiceAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class InvoiceFragment extends Fragment {
    RecyclerView recyclerView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_invoice, container, false);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        recyclerView = root.findViewById(R.id.recyclerViewInvoice);
        loadInvoice();

        return root;
    }

    public void loadInvoice () {
        final ArrayList<String> name = new ArrayList<String>();
        final ArrayList<Date> date = new ArrayList<java.util.Date>();
        final ArrayList<String> url = new ArrayList<String>();
        final ArrayList<String> id = new ArrayList<String>();
        final ArrayList<Boolean> paid = new ArrayList<Boolean>();
        db.collection("Invoice").whereEqualTo("customerID", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                name.add(document.getString("invoiceName"));
                                date.add(document.getDate("invoiceDueDate"));
                                url.add(document.getString("invoiceImageUrl"));
                                id.add(document.getString("invoiceID"));
                                paid.add(document.getBoolean("invoicePaid"));

                            }
                        } else {
                            Toast.makeText(getActivity(), "Failed to read invoice", Toast.LENGTH_SHORT).show();
                        }
                        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(getActivity(), id, name, date, url, paid);
                        recyclerView.setAdapter(invoiceAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                });
    }
}
