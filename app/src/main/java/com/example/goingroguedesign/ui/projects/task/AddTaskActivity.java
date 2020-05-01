package com.example.goingroguedesign.ui.projects.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.account.SignInActivity;
import com.example.goingroguedesign.ui.projects.ProjectDetailActivity;
import com.example.goingroguedesign.utils.DatePickerFragment;
import com.example.goingroguedesign.utils.LoadingAnimation;
import com.example.goingroguedesign.utils.TimePickerFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView tvCancel, tvSubmit, tvTime, tvDate;
    EditText etTitle, etDescription;
    String id;
    String title, description, dateString, timeString;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LoadingAnimation loadingAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ((AppCompatActivity) AddTaskActivity.this).getSupportActionBar().hide();
        tvCancel = findViewById(R.id.tvCancelAdd);
        tvSubmit = findViewById(R.id.tvSubmit);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadingAnimation = new LoadingAnimation(AddTaskActivity.this);
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

        if(getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        } else {
            Toast.makeText(AddTaskActivity.this, "Error reading Project", Toast.LENGTH_SHORT).show();
            finish();
        }
        tvSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingAnimation.openLoadingAnimation();
                title= etTitle.getText().toString().trim();
                description = etDescription.getText().toString().trim();
                dateString = tvDate.getText().toString().trim();
                timeString = tvTime.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty() || dateString.isEmpty() || timeString.isEmpty() || dateString.equals("Choose a date...") || timeString.equals("Choose a time...")) {
                    Toast.makeText(AddTaskActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    loadingAnimation.closeLoadingAnimation();
                } else {
                    SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH : mm:ss");
                    try {
                        Date date = formatter.parse(dateString + " " + timeString +":00");
                        System.out.println(date);
                        Map<String, Object> task = new HashMap<>();
                        task.put("taskName", title);
                        task.put("taskDescription", description);
                        task.put("taskCreatedDate", FieldValue.serverTimestamp());
                        task.put("taskResolved", false);
                        task.put("taskResolvedDate", FieldValue.serverTimestamp());
                        task.put("projectID", id);
                        task.put("taskDueDate", date);

                        //not going to use, here for keeping consistent with web/ios
                        task.put("customerEmail", "customerEmail");
                        task.put("taskType", "ongoing");
                        task.put("taskID", "thisShouldBeTaskID");


                        db.collection("Task").add(task)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        loadingAnimation.closeLoadingAnimation();
                                        finish();
                                    }
                                });
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    loadingAnimation.closeLoadingAnimation();



                }
            }
        });
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
