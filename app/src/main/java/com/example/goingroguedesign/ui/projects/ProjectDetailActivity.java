package com.example.goingroguedesign.ui.projects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.account.UpdateEmailActivity;
import com.example.goingroguedesign.ui.account.UpdateUsernameActivity;
import com.google.android.material.tabs.TabLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectDetailActivity extends AppCompatActivity {

    String id, title, address, status, date, lead;
    TextView tvTitle, tvAddress, tvStatus, tvDate, tvLead;
    ImageView ivDocument, ivCalendar, ivInvoice, ivTask, ivBack, ivNote, ivMap, ivMore, ivLess;
    CardView cvTitle, cvDetail;
    boolean showDetail = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        ((AppCompatActivity) ProjectDetailActivity.this).getSupportActionBar().hide();



        /*
        ivDocument = findViewById(R.id.ivDocument);
        ivCalendar = findViewById(R.id.ivCalendar);
        ivInvoice = findViewById(R.id.ivInvoice);
        ivTask = findViewById(R.id.ivTask);
        ivNote = findViewById(R.id.ivNote);
         */
        ivBack = findViewById(R.id.ivBack);
        ivMap = findViewById(R.id.ivMap);
        tvTitle = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvStatus = findViewById(R.id.tvStatus);
        tvDate = findViewById(R.id.tvDate);
        tvLead = findViewById(R.id.tvLead);
        cvDetail = findViewById(R.id.cvDetail);
        cvTitle = findViewById(R.id.cvTitle);
        ivMore = findViewById(R.id.ivMore);
        ivLess = findViewById(R.id.ivLess);
        getData();
        setData();
        //Toast.makeText(this, "Retrieving data: "+id, Toast.LENGTH_SHORT).show();

        cvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showDetail) {
                    showDetail = false;
                    ivMore.setVisibility(View.VISIBLE);
                    ivLess.setVisibility(View.INVISIBLE);
                    cvDetail.setVisibility(View.GONE);
                } else {
                    showDetail = true;
                    ivMore.setVisibility(View.INVISIBLE);
                    ivLess.setVisibility(View.VISIBLE);
                    cvDetail.setVisibility(View.VISIBLE);
                }

            }
        });
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), id);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectDetailActivity.this, MapsActivity.class);
                intent.putExtra("lat", 44.564568f);
                intent.putExtra("lng", -123.262047f);
                intent.putExtra("title", title);
                startActivity(intent);
                /*
                Uri locationUri = Uri.parse("http://maps.google.co.in/maps?q=" + address);
                Intent I = new Intent(Intent.ACTION_VIEW, locationUri);
                if (I.resolveActivity(getPackageManager()) != null) {
                    startActivity(I);
                }

                 */
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

        /*tabs.getTabAt(0).setIcon(R.drawable.ic_document);
        tabs.getTabAt(1).setIcon(R.drawable.ic_calendar);
        tabs.getTabAt(2).setIcon(R.drawable.ic_invoice);
        tabs.getTabAt(3).setIcon(R.drawable.ic_task);
        tabs.getTabAt(4).setIcon(R.drawable.ic_note);
*/

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
