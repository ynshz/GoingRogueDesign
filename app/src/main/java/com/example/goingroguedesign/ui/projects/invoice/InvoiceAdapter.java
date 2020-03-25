package com.example.goingroguedesign.ui.projects.invoice;

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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.account.UpdateAddressActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> {

    ArrayList<String> id, name, url;
    ArrayList<Date> date;
    ArrayList<Boolean> paid;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public InvoiceAdapter(Context ct,  ArrayList<String> s1,  ArrayList<String> s2, ArrayList<Date> s3, ArrayList<String> s4, ArrayList<Boolean> s5){
        context = ct;
        id = s1;
        name = s2;
        date = s3;
        url = s4;
        paid = s5;

    }
    @NonNull
    @Override
    public InvoiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.invoice_item, parent, false);
        return new InvoiceAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InvoiceAdapter.MyViewHolder holder, final int position) {
        holder.text1.setText(name.get(position));
        holder.text2.setText(date.get(position).toString());

        holder.checkBox.setChecked(paid.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    final DocumentReference paidRef = db.collection("Invoice").document(id.get(position));
                    paidRef
                            .update("invoicePaid", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    paidRef
                                            .update("invoicePaidAt", FieldValue.serverTimestamp())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Invoice: " + name.get(position) + "has been marked as PAID.", Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Couldn't update PaidAt timestamp.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle("Changing Invoice Status")
                            .setMessage("Are you sure you want to mark invoice: " + name.get(position) + " as Unpaid?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final DocumentReference paidRef = db.collection("Invoice").document(id.get(position));
                                    paidRef
                                            .update("invoicePaid", false)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Invoice: " + name.get(position) + "has been marked as Unpaid.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    holder.checkBox.setChecked(true);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        if (url.get(position).length() != 0) {
            DrawableCompat.setTint(holder.image.getDrawable(), ContextCompat.getColor(context, R.color.black));
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InvoiceImageActivity.class);
                    intent.putExtra("imageUrl", url.get(position));
                    intent.putExtra("name", name.get(position));
                    context.startActivity(intent);
                }
            });
        } else {
            DrawableCompat.setTint(holder.image.getDrawable(), ContextCompat.getColor(context, R.color.light_grey));
        }

    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{

        TextView text1, text2;
        ImageView image;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.tvName);
            text2 = itemView.findViewById(R.id.tvDate);
            image = itemView.findViewById(R.id.ivLink);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

}

