package com.example.goingroguedesign.ui.projects.task;

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

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    ArrayList<String> id, name, description;
    ArrayList<Date> date;
    ArrayList<Boolean> resolved;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TaskAdapter(Context ct,  ArrayList<String> s1,  ArrayList<String> s2, ArrayList<String> s3, ArrayList<Date> s4, ArrayList<Boolean> s5){
        context = ct;
        id = s1;
        name = s2;
        description = s3;
        date = s4;
        resolved = s5;

    }
    @NonNull
    @Override
    public TaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_item, parent, false);
        return new TaskAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskAdapter.MyViewHolder holder, final int position) {
        holder.text1.setText(name.get(position));
        holder.text2.setText(date.get(position).toString());
        holder.text3.setText(description.get(position));
        if (resolved.get(position)) {
            holder.checkBox.setChecked(true);
            holder.checkBox.setText("Resolved");
            holder.checkBox.setTextColor(context.getResources().getColor(R.color.green));
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    final DocumentReference paidRef = db.collection("Task").document(id.get(position));
                    paidRef
                            .update("taskResolved", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    paidRef
                                            .update("taskResolvedAt", FieldValue.serverTimestamp())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Toast.makeText(context, "Task: " + name.get(position) + "has been marked as RESOLVED.", Toast.LENGTH_SHORT).show();
                                                    holder.checkBox.setText("Resolved");
                                                    holder.checkBox.setTextColor(context.getResources().getColor(R.color.green));

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Couldn't update ResolvedAt timestamp.", Toast.LENGTH_SHORT).show();
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
                            .setTitle("Changing Task Status")
                            .setMessage("Are you sure you want to mark task: " + name.get(position) + " as Resolved?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final DocumentReference paidRef = db.collection("Task").document(id.get(position));
                                    paidRef
                                            .update("taskResolved", false)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Toast.makeText(context, "Task: " + name.get(position) + "has been marked as not Resolved.", Toast.LENGTH_SHORT).show();
                                                    holder.checkBox.setText("Created");
                                                    holder.checkBox.setTextColor(context.getResources().getColor(R.color.dark_grey));

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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskDetailActivity.class);
                intent.putExtra("name", name.get(position));
                intent.putExtra("id", id.get(position));
                intent.putExtra("description", description.get(position));
                context.startActivity(intent);

                /*
                new AlertDialog.Builder(context)
                        .setTitle("Permanently Deleting a Task")
                        .setMessage("Are you sure you want to permanently delete task: " + name.get(position) + " ?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Task").document(id.get(position))
                                        .delete();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                 */
            }
        });

    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{

        TextView text1, text2, text3;
        CheckBox checkBox;
        ImageView detail;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.tvName);
            text2 = itemView.findViewById(R.id.tvDate);
            text3 = itemView.findViewById(R.id.tvDescription);
            checkBox = itemView.findViewById(R.id.checkBox);
            //detail = itemView.findViewById(R.id.ivDetails);
            cardView = itemView.findViewById(R.id.cvTask);
        }
    }

}

