package com.example.goingroguedesign.ui.projects;

import androidx.appcompat.app.AppCompatActivity;
import com.example.goingroguedesign.R;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectDetailActivity extends AppCompatActivity {

    String id, title, address, status, date, lead;
    TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        ((AppCompatActivity) ProjectDetailActivity.this).getSupportActionBar().hide();

        tvTitle = findViewById(R.id.tvTitle);

    }

    private void getData() {
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("title") &&
                getIntent().hasExtra("address") &&
                getIntent().hasExtra("status") &&
                getIntent().hasExtra("date") &&
                getIntent().hasExtra("lead")) {
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            address = getIntent().getStringExtra("address");
            status = getIntent().getStringExtra("status");
            date = getIntent().getStringExtra("date");
            lead = getIntent().getStringExtra("lead");
        } else {
            Toast.makeText(this, "Error Retrieving data.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        tvTitle.setText(address);
    }
}
