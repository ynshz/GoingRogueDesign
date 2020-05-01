package com.example.goingroguedesign.ui.projects.document;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.projects.invoice.InvoiceAdapter;
import com.example.goingroguedesign.ui.projects.invoice.InvoiceImageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {

    ArrayList<String> id, name, url;
    ArrayList<Date> date;
    ArrayList<Boolean> completed;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DocumentAdapter(Context ct,  ArrayList<String> s1,  ArrayList<String> s2, ArrayList<Date> s3, ArrayList<String> s4, ArrayList<Boolean> s5){
        context = ct;
        id = s1;
        name = s2;
        date = s3;
        url = s4;
        completed = s5;

    }
    @NonNull
    @Override
    public DocumentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.document_item, parent, false);
        return new DocumentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentAdapter.MyViewHolder holder, final int position) {
        holder.text1.setText(name.get(position));
        holder.text2.setText(date.get(position).toString());

        holder.checkBox.setChecked(completed.get(position));

        if (completed.get(position)) {
            holder.text3.setText("Completed");
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    final DocumentReference paidRef = db.collection("Document").document(id.get(position));
                    paidRef
                            .update("documentCompleted", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    paidRef
                                            .update("documentCompletedAt", FieldValue.serverTimestamp())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    holder.text3.setText("Completed");
                                                    Toast.makeText(context, "Document: " + name.get(position) + "has been marked as COMPLETED.", Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Couldn't update CompletedAt timestamp.", Toast.LENGTH_SHORT).show();
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
                            .setTitle("Changing Document Status")
                            .setMessage("Are you sure you want to mark document: " + name.get(position) + " as inCompleted?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final DocumentReference paidRef = db.collection("Document").document(id.get(position));
                                    paidRef
                                            .update("documentCompleted", false)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    holder.text3.setText("");
                                                    Toast.makeText(context, "Document: " + name.get(position) + "has been marked as inCompleted.", Toast.LENGTH_SHORT).show();
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
                    //TODO Link to google doc
                    Intent googleDriveIntent;
                    try {
                        googleDriveIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("com.google.android.apps.docs.DRIVE_OPEN"));
                        context.startActivity(googleDriveIntent);
                    } catch (ActivityNotFoundException e) {
                        googleDriveIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.get(position)));
                        context.startActivity(googleDriveIntent);
                    }
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

        TextView text1, text2, text3;
        ImageView image;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.tvName);
            text2 = itemView.findViewById(R.id.tvDate);
            text3 = itemView.findViewById(R.id.tvComplete);
            image = itemView.findViewById(R.id.ivLink);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

}