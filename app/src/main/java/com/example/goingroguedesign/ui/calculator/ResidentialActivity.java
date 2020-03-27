package com.example.goingroguedesign.ui.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.goingroguedesign.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.goingroguedesign.ui.home.ListServicesActivity;
import com.example.goingroguedesign.ui.calculator.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ResidentialActivity extends AppCompatActivity {
    private static final String TAG = "ResidentialActivity";
    TextView cancel, next;
    int sqft, floor, room, bathroom;
    boolean kitchen;
    String fixture;
    EditText etSqft, etFloor, etRoom, etBathroom;
    RadioGroup radioGroup;
    Spinner spinner;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> fixtureOptions = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residential);
        ((AppCompatActivity) ResidentialActivity.this).getSupportActionBar().hide();
        cancel = findViewById(R.id.tvCancelAdd);
        next = findViewById(R.id.tvSubmit);
        etSqft = findViewById(R.id.etSqft);
        etFloor = findViewById(R.id.etFloor);
        etRoom = findViewById(R.id.etRoom);
        etBathroom = findViewById(R.id.etBathroom);
        radioGroup = findViewById(R.id.radioGroup);
        spinner = findViewById(R.id.spinnerFixture);
        db.collection("Fixture")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fixtureOptions.add(document.getId());
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            String[] array = fixtureOptions.toArray(new String[0]);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ResidentialActivity.this, android.R.layout.simple_spinner_dropdown_item, array);
//set the spinners adapter to the previously created one.
                            spinner.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqft = Integer.parseInt(etSqft.getText().toString());
                floor = Integer.parseInt(etFloor.getText().toString());
                room = Integer.parseInt(etRoom.getText().toString());
                bathroom = Integer.parseInt(etBathroom.getText().toString());
                kitchen = (radioGroup.getCheckedRadioButtonId() == R.id.rbYes);
                fixture = spinner.getSelectedItem().toString();
                Intent intent = new Intent(ResidentialActivity.this, ProfileActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("sqft", sqft);
                extras.putInt("floor", floor);
                extras.putInt("room", room);
                extras.putInt("bathroom", bathroom);
                extras.putBoolean("kitchen", kitchen);
                extras.putString("fixture", fixture);
                extras.putString("type", "ResidentialQuoteRequest");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }
}
