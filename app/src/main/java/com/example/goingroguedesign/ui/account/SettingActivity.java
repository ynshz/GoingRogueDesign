package com.example.goingroguedesign.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import com.example.goingroguedesign.R;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    TextView cancel, save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ((AppCompatActivity) SettingActivity.this).getSupportActionBar().hide();
        cancel = findViewById(R.id.tvCancelAdd);
        save = findViewById(R.id.tvSubmit);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save setting to shared preferences
                finish();
            }
        });
    }
}
