package com.example.goingroguedesign.ui.projects.note;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.account.UpdateEmailActivity;
import com.example.goingroguedesign.ui.projects.invoice.InvoiceAdapter;
import com.example.goingroguedesign.ui.projects.invoice.InvoiceImageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    ArrayList<String> id, name, content, url, filePath;
    ArrayList<Date> createdAt, modifiedAt;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public NoteAdapter(Context ct,  ArrayList<String> s1,  ArrayList<String> s2, ArrayList<String> s3, ArrayList<Date> s4, ArrayList<Date> s5, ArrayList<String> s6, ArrayList<String> s7){
        context = ct;
        id = s1;
        name = s2;
        content = s3;
        createdAt = s4;
        modifiedAt = s5;
        url = s6;
        filePath = s7;
    }
    @NonNull
    @Override
    public NoteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note_item, parent, false);
        return new NoteAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteAdapter.MyViewHolder holder, final int position) {
        holder.text1.setText(name.get(position));
        holder.text2.setText(content.get(position));
        holder.text3.setText(createdAt.get(position).toString());
        if(!url.get(position).equals("null")) {
            Glide.with(context).load(url.get(position)).centerCrop().into(holder.picture);

            holder.picture.setVisibility(View.VISIBLE);
        } else {
            holder.picture.setVisibility(View.GONE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoteDetailActivity.class);
                intent.putExtra("name", name.get(position));
                intent.putExtra("id", id.get(position));
                intent.putExtra("content", content.get(position));
                intent.putExtra("url", url.get(position));
                intent.putExtra("filePath", filePath.get(position));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{

        TextView text1, text2, text3;
        ImageView detail, picture;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.tvName);
            text2 = itemView.findViewById(R.id.tvDescription);
            text3 = itemView.findViewById(R.id.tvDate);
            //detail = itemView.findViewById(R.id.ivDetails);
            cardView = itemView.findViewById(R.id.cvNote);
            picture = itemView.findViewById(R.id.ivPicture);
        }
    }

}

