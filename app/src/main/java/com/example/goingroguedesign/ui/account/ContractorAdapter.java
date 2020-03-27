package com.example.goingroguedesign.ui.account;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContractorAdapter extends RecyclerView.Adapter<ContractorAdapter.MyViewHolder> {

    ArrayList<String> firstName, lastName, phone, email, address, id, type;
    Context context;

    public ContractorAdapter(Context ct,  ArrayList<String> s1,  ArrayList<String> s2First, ArrayList<String> s2Last, ArrayList<String> s3, ArrayList<String> s4, ArrayList<String> s5, ArrayList<String> s6){
        context = ct;
        id = s1;
        firstName = s2First;
        lastName = s2Last;
        phone = s3;
        email = s4;
        address = s5;
        type = s6;
    }
    @NonNull
    @Override
    public ContractorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contractor_item, parent, false);
        return new ContractorAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractorAdapter.MyViewHolder holder, final int position) {
        holder.text1.setText(firstName.get(position) + " " + lastName.get(position));
        holder.text2.setText(phone.get(position));
        holder.text3.setText(email.get(position));
        holder.text4.setText(address.get(position));
        holder.text5.setText(type.get(position));

        holder.contractorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContractorDetailActivity.class);
                intent.putExtra("id", id.get(position));
                intent.putExtra("firstName", firstName.get(position));
                intent.putExtra("lastName", lastName.get(position));
                intent.putExtra("address", address.get(position));
                intent.putExtra("phone", phone.get(position));
                intent.putExtra("email", email.get(position));
                intent.putExtra("type", type.get(position));
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
        CardView contractorCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.tvName);
            text2 = itemView.findViewById(R.id.tvPhoneNumber);
            text3 = itemView.findViewById(R.id.tvEmail);
            text4 = itemView.findViewById(R.id.tvAddress);
            text5 = itemView.findViewById(R.id.tvTypeOfWork);
            contractorCard = itemView.findViewById(R.id.cvContractor);
        }
    }
}
