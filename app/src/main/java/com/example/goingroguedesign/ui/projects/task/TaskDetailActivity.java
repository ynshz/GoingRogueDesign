package com.example.goingroguedesign.ui.projects.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.projects.ProjectDetailActivity;
import com.example.goingroguedesign.utils.DatePickerFragment;
import com.example.goingroguedesign.utils.LoadingAnimation;
import com.example.goingroguedesign.utils.TimePickerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaskDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    String id, name, description, due, newTitle, newDes, dateString, timeString;
    TextView textView, cancel, submit, delete, tvDue, tvDate, tvTime;
    EditText etTitle, etDescription;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean modified;
    LoadingAnimation loadingAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        ((AppCompatActivity) TaskDetailActivity.this).getSupportActionBar().hide();

        textView = findViewById(R.id.textView);
        cancel = findViewById(R.id.tvCancelAdd);
        submit = findViewById(R.id.tvSubmit);
        delete = findViewById(R.id.tvDelete);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        tvDue = findViewById(R.id.tvOldDue);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);

        loadingAnimation = new LoadingAnimation(TaskDetailActivity.this);

        getData();
        setData();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TaskDetailActivity.this)
                        .setTitle("Permanently Deleting a Task")
                        .setMessage("Are you sure you want to permanently delete task: " + name + " ?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                loadingAnimation.openLoadingAnimation();
                                db.collection("Task").document(id)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                loadingAnimation.closeLoadingAnimation();
                                                finish();
                                            }
                                        });
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingAnimation.openLoadingAnimation();
                newTitle= etTitle.getText().toString().trim();
                newDes = etDescription.getText().toString().trim();
                dateString = tvDate.getText().toString().trim();
                timeString = tvTime.getText().toString().trim();

                if (newTitle.isEmpty() || newDes.isEmpty() || (!dateString.equals("Choose a new date...") && timeString.equals("Choose a new time...")) || (dateString.equals("Choose a new date...") && !timeString.equals("Choose a new time..."))) {
                    loadingAnimation.closeLoadingAnimation();
                    Toast.makeText(TaskDetailActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();

                } else {

                    // Get a new write batch
                    final WriteBatch batch = db.batch();
                    final DocumentReference taskRef = db.collection("Task").document(id);

                    if (!dateString.equals("Choose a new date...")) {
                        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH : mm:ss");
                        Date date = null;
                        try {
                            date = formatter.parse(dateString + " " + timeString +":00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        System.out.println(date);

                        assert date != null;
                        if (!date.toString().equals(due)) {
                            modified = true;
                            batch.update(taskRef, "taskDueDate", date);
                        }
                    }

                    if (!newTitle.equals(name)) {
                        modified = true;
                        batch.update(taskRef, "taskName", newTitle);
                    }

                    if (!newDes.equals(description)) {
                        modified = true;
                        batch.update(taskRef, "taskDescription", newDes);
                    }

                    if (modified) {
                        // Commit the batch
                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingAnimation.closeLoadingAnimation();
                                finish();
                            }
                        });
                    } else {
                        loadingAnimation.closeLoadingAnimation();
                        finish();
                    }

                }
            }
        });
    }

    private void getData() {
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("name") &&
                getIntent().hasExtra("description") &&
                getIntent().hasExtra("due")) {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            description = getIntent().getStringExtra("description");
            due = getIntent().getStringExtra("due");
        } else {
            Toast.makeText(this, "Error Retrieving data.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        //textView.setText("Update " + name);
        etTitle.setText(name);
        etDescription.setText(description);
        tvDue.setText(due);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        tvDate.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String h, m;
        if (hourOfDay < 10) {
            h = "0" + hourOfDay;
        } else {
            h = "" + hourOfDay;
        }

        if (minute < 10) {
            m = "0" + minute;
        } else {
            m = "" + minute;
        }

        tvTime.setText(h + " : " + m);
    }
}
