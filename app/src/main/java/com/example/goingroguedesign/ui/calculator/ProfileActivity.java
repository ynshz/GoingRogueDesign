package com.example.goingroguedesign.ui.calculator;

import androidx.appcompat.app.AppCompatActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.home.RequestInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivBack;
    EditText etFirstName, etLastName, etEmail, etMessage;
    Button submit;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ((AppCompatActivity) ProfileActivity.this).getSupportActionBar().hide();

        ivBack = findViewById(R.id.ivBack);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etMessage = findViewById(R.id.etMessage);
        submit = findViewById(R.id.button);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Bundle extras = getIntent().getExtras();
        final int sqft = extras.getInt("sqft");
        final int floor = extras.getInt("floor");
        final int room = extras.getInt("room");
        final int bathroom = extras.getInt("bathroom");
        final boolean kitchen = extras.getBoolean("kitchen");
        final String fixture = extras.getString("fixture");
        final String type = extras.getString("type");
        Toast.makeText(ProfileActivity.this, "sqft " + sqft + " floor " + floor + " room "+ room
                + " bathroom " + bathroom + " kitchen " + kitchen + " fixture " + fixture, Toast.LENGTH_SHORT).show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> quoteRequest = new HashMap<>();
                quoteRequest.put("squareFootage", sqft);
                quoteRequest.put("floor", floor);
                quoteRequest.put("room", room);
                quoteRequest.put("bathroom", bathroom);
                quoteRequest.put("kitchen", kitchen);
                quoteRequest.put("fixture", fixture);
                quoteRequest.put("customerFirstName", etFirstName.getText().toString().trim());
                quoteRequest.put("customerLastName", etLastName.getText().toString().trim());
                quoteRequest.put("customerEmail", etEmail.getText().toString().trim());
                quoteRequest.put("customerMessage", etMessage.getText().toString().trim());

                if(mUser != null) {
                    quoteRequest.put("hasAccount", true);
                } else {
                    quoteRequest.put("hasAccount", false);
                }
                db.collection(type).add(quoteRequest);
            }
        });


    }
}
