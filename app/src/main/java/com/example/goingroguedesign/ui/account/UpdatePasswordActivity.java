package com.example.goingroguedesign.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import com.example.goingroguedesign.R;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UpdatePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ((AppCompatActivity) UpdatePasswordActivity.this).getSupportActionBar().hide();

        TextView tvCancelUpdate = findViewById(R.id.tvCancelAdd);

        tvCancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
