package com.example.veloxstudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddReminder extends AppCompatActivity {
    EditText className,tutorName;
    DatePicker datePicker;
    Button saveBtn;
    NestedScrollView scrollView;
    TimePicker timePicker;
    FirebaseUser user;
    FirebaseFirestore fireStore;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        FirebaseApp.initializeApp(getApplicationContext());

        className = findViewById(R.id.classNameInput);
        tutorName = findViewById(R.id.tutorNameInput);
        datePicker = findViewById(R.id.dateInput);
        timePicker = findViewById(R.id.timePicker);
        scrollView = findViewById(R.id.scrollViewReminder);
        saveBtn = findViewById(R.id.saveButton);

        Calendar minDateCalendar = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        Calendar maxDate = (Calendar) currentDate.clone();
        maxDate.add(Calendar.YEAR, 1);

        datePicker.setMinDate(minDateCalendar.getTimeInMillis());
        datePicker.setMaxDate(maxDate.getTimeInMillis());

        fireStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        saveBtn.setOnClickListener(v -> {
            String classname = className.getText().toString();
            String tutorName = this.tutorName.getText().toString();
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int dayOfMonth = datePicker.getDayOfMonth();

            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = dateFormat.format(selectedDate.getTime());

            int hourOfDay = timePicker.getHour();
            int minute = timePicker.getMinute();

            Calendar selectedTime = Calendar.getInstance();
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String time = timeFormat.format(selectedTime.getTime());


            if (classname.isEmpty() || tutorName.isEmpty() || date.isEmpty() || time.isEmpty())
                Toast.makeText(getApplicationContext(), "Please add data correctly", Toast.LENGTH_SHORT).show();

            else
                storeData(classname,tutorName,date,time);

        });

    }

    private void storeData(String classname,String tutorName,String date,String time){


        if (user != null) {
            String uid = user.getUid();
             documentReference = fireStore.collection("reminders").document(uid).collection("data").document(classname + tutorName);
        }

        documentReference.get().addOnCompleteListener(task -> {

                 Map<String, Object> usermap;


                usermap = new HashMap<>();
                usermap.put("Classname", classname);
                usermap.put("Tutorname", tutorName);
                usermap.put("Time", time);
                usermap.put("Date", date);
                usermap.put("createdAt", FieldValue.serverTimestamp());


                documentReference.set(usermap).addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Reminder set", Toast.LENGTH_SHORT).show();
                    finish();

                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        );


    }
}