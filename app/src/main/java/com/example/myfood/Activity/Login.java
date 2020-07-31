package com.example.myfood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfood.Fragment.FoodStock;
import com.example.myfood.R;

public class Login extends AppCompatActivity {
    private EditText emailET;
    private EditText passwordlET;
    private EditText firstNameET;
    private EditText lastNameET;
    public static EditText birthDayET;
    private TextView firstNameTV;
    private TextView lastNameTV;
    private TextView birthDayTV;
    private TextView emailTV;
    private TextView passwordlTV;
    private TextView teamTV;
    private EditText teamET;
    private TextView creatAccountTV;
    private Button loginBtn;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        teamET = findViewById(R.id.teamET);
        teamTV = findViewById(R.id.teamTV);
        emailTV = findViewById(R.id.emailTV);
        emailET = findViewById(R.id.email);
        passwordlTV = findViewById(R.id.passwordTV);
        passwordlET = findViewById(R.id.password);
        firstNameET = findViewById(R.id.firstName);
        lastNameET = findViewById(R.id.lastName);
        birthDayET = findViewById(R.id.birthDayET);
        firstNameTV = findViewById(R.id.firstNameTV);
        lastNameTV = findViewById(R.id.lastNameTV);
        birthDayTV = findViewById(R.id.birthDayTV);
        creatAccountTV = findViewById(R.id.creatAccountTV);
        loginBtn = findViewById(R.id.login);

        firstNameET.setVisibility(View.GONE);
        firstNameTV.setVisibility(View.GONE);

        lastNameET.setVisibility(View.GONE);
        lastNameTV.setVisibility(View.GONE);

        birthDayET.setVisibility(View.GONE);
        birthDayTV.setVisibility(View.GONE);

        teamTV.setVisibility(View.GONE);
        teamET.setVisibility(View.GONE);

        creatAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (creatAccountTV.getText().toString()) {
                    case "עדיין אין לך חשבון? תלחץ כאן!":
                        firstNameET.setVisibility(View.VISIBLE);
                        firstNameTV.setVisibility(View.VISIBLE);

                        lastNameET.setVisibility(View.VISIBLE);
                        lastNameTV.setVisibility(View.VISIBLE);

                        birthDayET.setVisibility(View.VISIBLE);
                        birthDayTV.setVisibility(View.VISIBLE);

                        loginBtn.setText("הבא");
                        creatAccountTV.setText("חזור");
                        break;
                    case "חזור":
                        firstNameET.setVisibility(View.GONE);
                        firstNameTV.setVisibility(View.GONE);

                        lastNameET.setVisibility(View.GONE);
                        lastNameTV.setVisibility(View.GONE);

                        birthDayET.setVisibility(View.GONE);
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
                        if (!loginUiCheck()) {
                            Toast.makeText(context, "האיימל או הסיסמא שגויים", Toast.LENGTH_LONG).show();

                        }
                        break;
                    case "הבא":
                        if (!signUiCheck()) {
                            emailET.setVisibility(View.GONE);
                            emailTV.setVisibility(View.GONE);
                            passwordlTV.setVisibility(View.GONE);
                            passwordlET.setVisibility(View.GONE);
                            firstNameET.setVisibility(View.GONE);
                            lastNameET.setVisibility(View.GONE);
                            birthDayET.setVisibility(View.GONE);
                            firstNameTV.setVisibility(View.GONE);
                            lastNameTV.setVisibility(View.GONE);
                            birthDayTV.setVisibility(View.GONE);
                            creatAccountTV.setVisibility(View.GONE);

                            teamET.setVisibility(View.VISIBLE);
                            teamTV.setVisibility(View.VISIBLE);

                            loginBtn.setText("הירשם");
                        }
                        break;
                    case "הירשם":
                        if(!groupUiCheck()){
                            Toast.makeText(context, "נרשמת בהצלחה", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, ManageFood.class);
                            startActivity(intent);
                            finish();
                        }

                        break;

                }


            }
        });
    }

    public boolean loginUiCheck() {
        boolean flag = false;

        if (emailET.getText().toString().equals("")) {
            emailET.setError("הכנס איימל");
            flag = true;
        }
        if (passwordlET.getText().toString().equals("")) {
            passwordlET.setError("הכנס סיסמא");
            flag = true;
        }

        return flag;

    }

    public boolean signUiCheck() {
        boolean flag = false;

        if (emailET.getText().toString().equals("")) {
            emailET.setError("הכנס איימל");
            flag = true;
        }
        if (firstNameET.getText().toString().equals("")) {
            firstNameET.setError("הכנס שם פרטי");
            flag = true;
        }
        if (lastNameET.getText().toString().equals("")) {
            lastNameET.setError("הכנס שם משפחה");
            flag = true;
        }
        if (birthDayET.getText().toString().equals("")) {
            birthDayET.setError("הכנס גיל");
            flag = true;
        }
        return flag;

    }
    public boolean groupUiCheck() {
        boolean flag = false;

        if (teamET.getText().toString().equals("")) {
            teamET.setError("הכנס קוד קבוצה");
            flag = true;
        }

        return flag;

    }
}
