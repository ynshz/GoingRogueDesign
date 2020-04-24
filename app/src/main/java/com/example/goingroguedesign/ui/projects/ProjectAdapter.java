package com.example.goingroguedesign.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.goingroguedesign.R;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Date;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {

    ArrayList<String> id, title, address, status, lead, contractor;
    ArrayList<Date> date;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ProjectAdapter(Context ct, ArrayList<String> s1, ArrayList<String> s2, ArrayList<String> s3, ArrayList<String> s4, ArrayList<Date> s5, ArrayList<String> s6, ArrayList<String> s7){
        context = ct;
        id = s1;
        title = s2;
        address = s3;
        status = s4;
        date = s5;
        lead = s6;
        contractor = s7;
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
        if(status != null) {
            switch (status.get(position)) {
                case "Closed":
                    holder.text3.setTextColor(context.getResources().getColor(R.color.black));
                    break;
                case "New":
                    holder.text3.setTextColor(context.getResources().getColor(R.color.red));
                    break;
                case "Started":
                    holder.text3.setTextColor(context.getResources().getColor(R.color.yellow));
                    break;
                case "Completed":
                    holder.text3.setTextColor(context.getResources().getColor(R.color.green));
                    break;
                default:
                    break;
            }
        }


        holder.text4.setText(date.get(position).toString());
        holder.text5.setText(lead.get(position));
        holder.text6.setText(contractor.get(position));

        holder.projectItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProjectDetailActivity.class);
                intent.putExtra("id", id.get(position));
                intent.putExtra("title", title.get(position));
                intent.putExtra("address", address.get(position));
                intent.putExtra("status", status.get(position));
                intent.putExtra("date", date.get(position).toString());
                intent.putExtra("lead", lead.get(position));
                context.startActivity(intent);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Project").document(id.get(position))
                        .delete();
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{

        TextView text1, text2, text3, text4, text5, text6;
        CardView projectItemCard;
        ImageView ivDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.tvName);
            text2 = itemView.findViewById(R.id.tvAddress);
            text3 = itemView.findViewById(R.id.tvStatus);
            text4 = itemView.findViewById(R.id.tvStartDate);
            text5 = itemView.findViewById(R.id.tvLeadName);
            text6 = itemView.findViewById(R.id.tvContractor);
            projectItemCard = itemView.findViewById(R.id.cvProjectItem);

            //delete this button before publish
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

}
