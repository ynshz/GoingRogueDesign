package com.example.goingroguedesign.ui.account;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountFragment extends Fragment {

    private static final String TAG = "MyActivity";
    CardView cvAccountUsername, cvAccountName, cvAccountAddress, cvAccountEmail, cvAccountPhoneNumber, cvAccountPassword, cvAccountContractor, cvAccountSignOut, cvSetting;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    TextView tvUsername, tvID, tvName, tvPhoneNumber, tvAddress, tvEmail;
    String username, id, firstName, lastName, phoneNumber, address, email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AlertDialog dialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            View root = inflater.inflate(R.layout.fragment_account, container, false);

            cvAccountUsername = root.findViewById(R.id.cvAccountUsername);
            cvAccountName = root.findViewById(R.id.cvAccountName);
            cvAccountAddress = root.findViewById(R.id.cvAccountAddress);
            cvAccountEmail = root.findViewById(R.id.cvAccountEmail);
            cvAccountPhoneNumber = root.findViewById(R.id.cvAccountPhoneNumber);
            cvAccountPassword = root.findViewById(R.id.cvAccountPassword);
            cvAccountContractor = root.findViewById(R.id.cvAccountContractors);
            cvAccountSignOut = root.findViewById(R.id.cvAccountSignOut);
            cvSetting = root.findViewById(R.id.cvSettings);
            tvUsername = root.findViewById(R.id.tvAccountCurrentUsername);
            tvID = root.findViewById(R.id.tvAccountCurrentId);
            tvName = root.findViewById(R.id.tvAccountCurrentName);
            tvPhoneNumber = root.findViewById(R.id.tvAccountCurrentPhoneNumber);
            tvAddress = root.findViewById(R.id.tvAccountCurrentAddress);
            tvEmail = root.findViewById(R.id.tvAccountCurrentEmail);


            initDBView();

            cvAccountUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UpdateUsernameActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

            cvAccountName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UpdateNameActivity.class);
                    intent.putExtra("firstName", firstName);
                    intent.putExtra("lastName", lastName);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

            cvAccountAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UpdateAddressActivity.class);
                    intent.putExtra("address", address);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

            cvAccountEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UpdateEmailActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

            cvAccountPhoneNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UpdatePhoneActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                }
            });

            cvAccountPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UpdatePasswordActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            });

            cvAccountContractor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ContractorActivity.class);
                    startActivity(intent);
                }
            });

            cvAccountSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();
                    Toast.makeText(getActivity(), "You have signed out successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });

            cvSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    startActivity(intent);
                }
            });

            return root;
        } else {
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            startActivity(intent);
        }

        return null;
    }

    /*
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(hidden) {

        } else {
            initDBView();
        }
    }
*/
    public void initDBView() {

        loadingAnimation();
        DocumentReference docRef = db.collection("Customer").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        username = document.getString("customerUsername");
                        id = document.getString("customerID");
                        firstName = document.getString("customerFirstName");
                        lastName = document.getString("customerLastName");
                        phoneNumber = document.getString("customerPhoneNumber");
                        address = document.getString("customerAddress");
                        email = document.getString("customerEmail");
                        tvUsername.setText(username);
                        tvID.setText(id);
                        tvName.setText(firstName + " " + lastName);
                        tvPhoneNumber.setText(phoneNumber);
                        tvAddress.setText(address);
                        tvEmail.setText(email);
                        dialog.dismiss();
                    } else {
                        mAuth.signOut();
                        Toast.makeText(getActivity(), "Cannot find user.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    mAuth.signOut();
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void loadingAnimation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

}
