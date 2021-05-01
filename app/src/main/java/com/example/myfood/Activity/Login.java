package com.example.myfood.Activity;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfood.Class.FirebaseManager;
import com.example.myfood.Class.User;
import com.example.myfood.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Login extends AppCompatActivity {
    public static final String CHANNEL_1_ID = "channel1";
    private TextInputLayout emailET;
    private TextInputLayout passwordET;
    private TextInputLayout firstNameET;
    private TextInputLayout lastNameET;
    private TextView birthDayEnter;
    private TextView birthDayTV;
    private TextInputLayout familyCodeET;
    private TextView creatAccountTV;
    private Button loginBtn;
    private Context context;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ProgressBar progressBar;
    private boolean doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     /*   ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.CAMERA
                        , Manifest.permission.READ_EXTERNAL_STORAGE},
                STORGE_PERMISSION_CODE);
*/
        context = this;
        familyCodeET = findViewById(R.id.familyCodeET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);
        birthDayEnter = findViewById(R.id.birthDayEnter);
        birthDayTV = findViewById(R.id.birthDayTV);
        creatAccountTV = findViewById(R.id.creatAccountTV);
        loginBtn = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);

        firstNameET.setVisibility(View.GONE);

        lastNameET.setVisibility(View.GONE);

        birthDayEnter.setVisibility(View.GONE);
        birthDayTV.setVisibility(View.GONE);

        familyCodeET.setVisibility(View.GONE);

        creatNotificationChannels();

        birthDayEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        context,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String pattern = "dd/MM/yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

                Date currentTime = Calendar.getInstance().getTime();
                try {
                    Date chooseDate = dateFormat.parse(dayOfMonth + "/" + (month + 1) + "/" + year);

                    if (chooseDate.before(currentTime)) {
                        birthDayEnter.setTextSize(20);
                        birthDayEnter.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    } else
                        Toast.makeText(getApplicationContext(), "התאריך כניסה שגוי!", Toast.LENGTH_LONG).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        };
        creatAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (creatAccountTV.getText().toString()) {
                    case "עדיין אין לך חשבון? תלחץ כאן!":
                        firstNameET.setVisibility(View.VISIBLE);
                        lastNameET.setVisibility(View.VISIBLE);

                        birthDayEnter.setVisibility(View.VISIBLE);
                        birthDayTV.setVisibility(View.VISIBLE);

                        loginBtn.setText("הבא");
                        creatAccountTV.setText("חזור");
                        break;
                    case "חזור":
                        firstNameET.setVisibility(View.GONE);
                        lastNameET.setVisibility(View.GONE);

                        birthDayEnter.setVisibility(View.GONE);
                        birthDayTV.setVisibility(View.GONE);

                        loginBtn.setText("התחבר");
                        creatAccountTV.setText("עדיין אין לך חשבון? תלחץ כאן!");
                        break;
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (loginBtn.getText().toString()) {
                    case "התחבר":
                        if (loginUiIsFull()) {
                            progressBar.setVisibility(View.VISIBLE);
                            String email = emailET.getEditText().getText().toString();
                            String password = passwordET.getEditText().getText().toString();
                            FirebaseManager.getInstance().login(Login.this, email, password, new FirebaseManager.FirebaseCallBack() {
                                @Override
                                public void onCallback(FirebaseManager.FirebaseResult result) {
                                    if (result.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(context, ManageFood.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        progressBar.setVisibility(View.GONE);

                                    }

                                }

                            });

                        }
                        break;
                    case "הבא":
                        if (signUiIsFull()) {
                            emailET.setVisibility(View.GONE);
                            passwordET.setVisibility(View.GONE);
                            firstNameET.setVisibility(View.GONE);
                            lastNameET.setVisibility(View.GONE);
                            birthDayEnter.setVisibility(View.GONE);
                            birthDayTV.setVisibility(View.GONE);
                            creatAccountTV.setVisibility(View.GONE);
                            familyCodeET.setVisibility(View.VISIBLE);
                            loginBtn.setText("הירשם");
                        }
                        break;
                    case "הירשם":
                        progressBar.setVisibility(View.VISIBLE);
                        User.initUser(emailET.getEditText().getText().toString()
                                , firstNameET.getEditText().getText().toString()
                                , lastNameET.getEditText().getText().toString()
                                , birthDayTV.getText().toString()
                                , null,String.valueOf(0),String.valueOf(0),String.valueOf(0));
                        User.getInstance().setFamilyCode(familyCodeET.getEditText().getText().toString());
                        String password = passwordET.getEditText().getText().toString();

                        FirebaseManager.getInstance().signIn(Login.this, password, new FirebaseManager.FirebaseCallBack() {
                            @Override
                            public void onCallback(FirebaseManager.FirebaseResult result) {
                                if (result.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(context, ManageFood.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    familyCodeET.setError("לא קיימת קבוצה עם אותו הקוד!");
                                }
                            }
                        });
                        break;

                }


            }
        });
    }

    private void creatNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This channel 1");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        }
    }

    public boolean loginUiIsFull() {
        boolean flag = true;

        if (emailET.getEditText().getText().toString().equals("")) {
            emailET.setError("הכנס איימל");
            flag = false;
        }
        if (passwordET.getEditText().getText().toString().equals("")) {
            passwordET.setError("הכנס סיסמא");
            flag = false;
        }

        return flag;

    }

    public boolean signUiIsFull() {
        boolean flag = true;

        if (emailET.getEditText().getText().toString().equals("")) {
            emailET.setError("הכנס איימל");
            flag = false;
        }
        if (firstNameET.getEditText().getText().toString().equals("")) {
            firstNameET.setError("הכנס שם פרטי");
            flag = false;
        }
        if (lastNameET.getEditText().getText().toString().equals("")) {
            lastNameET.setError("הכנס שם משפחה");
            flag = false;
        }
        if (birthDayEnter.getText().toString().equals("לחץ כאן לבחירת תאריך")) {
            flag = false;
            birthDayEnter.setError("נדרש למלא את השדה");

        }
        return flag;

    }
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "לחץ עוד פעם כדי לצאת!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }
}
