package com.example.goingroguedesign.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goingroguedesign.R;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListServicesActivity extends AppCompatActivity {

    int[] serviceImages = {R.drawable.ic_interior_design, R.drawable.ic_exterior_design, R.drawable.ic_conceptual_design,
                            R.drawable.ic_purchasing, R.drawable.ic_project_management, R.drawable.ic_owner_representative};
    int[] servicesResources = {R.array.interior_design_services,
                                R.array.exterior_design_services,
                                R.array.conceptual_design_services,
                                R.array.purchasing_services,
                                R.array.project_management_services,
                                R.array.owner_representative_services};
    String[] serviceTitles;
    String[][] serviceDetail;
    ImageView ivBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_services);
        ((AppCompatActivity) ListServicesActivity.this).getSupportActionBar().hide();

        serviceTitles = getResources().getStringArray(R.array.service_titles);
        ListView lvServiceList = (ListView)findViewById(R.id.lvServiceList);
        serviceDetail = new String[serviceImages.length][];

        ivBackButton = findViewById(R.id.ivFinishListService);

        for (int i = 0; i < serviceImages.length; i++) {
            serviceDetail[i] = getResources().getStringArray(servicesResources[i]);
        }

        ServiceListAdapter serviceListAdapter = new ServiceListAdapter();
        lvServiceList.setAdapter(serviceListAdapter);

        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    class ServiceListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return serviceImages.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.service_item, null);
            ImageView imageView = convertView.findViewById(R.id.ivServiceImage);
            TextView textView = convertView.findViewById(R.id.tvServiceTitle);
            LinearLayout linearLayout = convertView.findViewById(R.id.llServiceDetailList);

            for (int i=0; i<serviceDetail[position].length; i++){
                View child = getLayoutInflater().inflate(R.layout.service_detail_item, null);
                TextView tvDetailItem = child.findViewById(R.id.tvServiceDetailItem);
                tvDetailItem.setText(serviceDetail[position][i]);
                linearLayout.addView(child);
            }

            imageView.setImageResource(serviceImages[position]);
            textView.setText(serviceTitles[position]);

            return convertView;
        }
    }



}
