package com.example.goingroguedesign.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.goingroguedesign.R;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {

    ArrayList<String> id, title, address, status, date, lead;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ProjectAdapter(Context ct,  ArrayList<String> s1,  ArrayList<String> s2, ArrayList<String> s3, ArrayList<String> s4, ArrayList<String> s5, ArrayList<String> s6){
        context = ct;
        id = s1;
        title = s2;
        address = s3;
        status = s4;
        date = s5;
        lead = s6;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.project_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.text1.setText(title.get(position));
        holder.text2.setText(address.get(position));
        holder.text3.setText(status.get(position));
        holder.text4.setText(date.get(position));
        holder.text5.setText(lead.get(position));

        holder.projectItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProjectDetailActivity.class);
                intent.putExtra("id", id.get(position));
                intent.putExtra("title", title.get(position));
                intent.putExtra("address", address.get(position));
                intent.putExtra("status", status.get(position));
                intent.putExtra("date", date.get(position));
                intent.putExtra("lead", lead.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{

        TextView text1, text2, text3, text4, text5;
        CardView projectItemCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.tvTitle);
            text2 = itemView.findViewById(R.id.tvAddress);
            text3 = itemView.findViewById(R.id.tvStatus);
            text4 = itemView.findViewById(R.id.tvStartDate);
            text5 = itemView.findViewById(R.id.tvLeadName);
            projectItemCard = itemView.findViewById(R.id.cvProjectItem);
        }
    }

}
