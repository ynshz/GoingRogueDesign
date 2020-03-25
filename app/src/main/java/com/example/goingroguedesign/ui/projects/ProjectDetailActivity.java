package com.example.goingroguedesign.ui.projects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.goingroguedesign.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.example.goingroguedesign.ui.projects.SectionsPagerAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectDetailActivity extends AppCompatActivity {

    String id, title, address, status, date, lead;
    TextView tvTitle, tvAddress, tvStatus, tvDate, tvLead;
    ImageView ivDocument, ivCalendar, ivInvoice, ivTask, ivBack, ivNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        ((AppCompatActivity) ProjectDetailActivity.this).getSupportActionBar().hide();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        /*
        ivDocument = findViewById(R.id.ivDocument);
        ivCalendar = findViewById(R.id.ivCalendar);
        ivInvoice = findViewById(R.id.ivInvoice);
        ivTask = findViewById(R.id.ivTask);
        ivNote = findViewById(R.id.ivNote);
         */
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvAddress = findViewById(R.id.tvAddress);
        tvStatus = findViewById(R.id.tvStatus);
        tvDate = findViewById(R.id.tvDate);
        tvLead = findViewById(R.id.tvLead);
        getData();
        setData();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*
        ivDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout.Tab tab = tabs.getTabAt(0);
                tab.select();
            }
        });

        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout.Tab tab = tabs.getTabAt(1);
                tab.select();
            }
        });

        ivInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout.Tab tab = tabs.getTabAt(2);
                tab.select();
            }
        });

        ivTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout.Tab tab = tabs.getTabAt(3);
                tab.select();
            }
        });

        ivNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout.Tab tab = tabs.getTabAt(4);
                tab.select();
            }
        });

         */

        /*
        BadgeDrawable badge = tabs.getTabAt(0).getOrCreateBadge();
        badge.setVisible(true);
// Optionally show a number.
        badge.setNumber(99);

         */
        tabs.getTabAt(0).setIcon(R.drawable.ic_document);
        tabs.getTabAt(1).setIcon(R.drawable.ic_calendar);
        tabs.getTabAt(2).setIcon(R.drawable.ic_invoice);
        tabs.getTabAt(3).setIcon(R.drawable.ic_task);
        tabs.getTabAt(4).setIcon(R.drawable.ic_note);


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
        tvTitle.setText(title);
        tvAddress.setText(address);
        tvStatus.setText(status);
        tvDate.setText(date);
        tvLead.setText(lead);


    }
}
