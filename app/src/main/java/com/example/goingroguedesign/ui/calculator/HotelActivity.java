package com.example.goingroguedesign.ui.calculator;

import androidx.appcompat.app.AppCompatActivity;
import com.example.goingroguedesign.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.goingroguedesign.ui.home.RequestInfoActivity;

public class HotelActivity extends AppCompatActivity {
    TextView cancel, next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        ((AppCompatActivity) HotelActivity.this).getSupportActionBar().hide();
        cancel = findViewById(R.id.tvCancelAdd);
        next = findViewById(R.id.tvSubmit);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelActivity.this, RequestInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
