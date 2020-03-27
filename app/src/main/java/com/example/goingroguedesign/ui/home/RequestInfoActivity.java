package com.example.goingroguedesign.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.goingroguedesign.R;

public class RequestInfoActivity extends AppCompatActivity {

    ImageView ivBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_info);
        ((AppCompatActivity) RequestInfoActivity.this).getSupportActionBar().hide();


        ivBackButton = findViewById(R.id.ivBack);

        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
