package com.example.expresspizzaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Button btnDate;
    private Button btnSignUp;
    private DBManagerUsers dbManagerUsers;
    private Context self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // db initialization
        dbManagerUsers = new DBManagerUsers(this);
        dbManagerUsers.open();

        // date picking handling
        initDatePicker();
        btnDate = findViewById(R.id.inputDateBirthSignUp);
        btnDate.setText(getToday());

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        // signing up handling
        btnSignUp = findViewById(R.id.btnProceedSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting and transforming the input
                String name;
                String email;
                String password;
                String dateBirth;
                double points = 0;
                int discount = 0;

                EditText inputName = findViewById(R.id.inputNameSignUp);
                EditText inputEmail = findViewById(R.id.inputEmailSignUp);
                EditText inputPassword = findViewById(R.id.inputPasswordSignUp);
                Button inputDateBirth = findViewById(R.id.inputDateBirthSignUp);

                name = inputName.getText().toString();
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                dateBirth = inputDateBirth.getText().toString();

                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int userYear = Integer.parseInt(dateBirth.split(" ")[0]);

                if (currentYear - userYear >= 60) {
                    discount = 1;
                }

                // validating the input
                Boolean nameValid = true;
                Boolean emailValid = true;
                Boolean passwordValid = true;

                // name validation
                if (name.trim().length() == 0) {
                    nameValid = false;
                    inputName.setTextColor(ContextCompat.getColor(self, R.color.red));
                } else {
                    nameValid = true;
                    inputName.setTextColor(ContextCompat.getColor(self, R.color.black));
                }

                // -- email validation
                String emailRegex = "^(.+)@(.+)$";
                Pattern emailPattern = Pattern.compile(emailRegex);
                Matcher emailMatcher = emailPattern.matcher(email);

                if (!emailMatcher.matches() || email.trim().length() == 0) {
                    emailValid = false;
                    inputEmail.setTextColor(ContextCompat.getColor(self, R.color.red));
                } else {
                    emailValid = true;
                    inputEmail.setTextColor(ContextCompat.getColor(self, R.color.black));
                }

                // password validation
                String passwordRegex = "^(?=.*\\d.*\\d)(?=.*[A-Z])(?=.*[@#$%^&+=-]).{8,20}$";
                Pattern passwordPattern = Pattern.compile(passwordRegex);
                Matcher passwordMatcher = passwordPattern.matcher(password);

                if (!passwordMatcher.matches() || password.trim().length() == 0) {
                    passwordValid = false;
                    inputPassword.setTextColor(ContextCompat.getColor(self, R.color.red));
                    Toast.makeText(SignUpActivity.this, "Your passowrd should have:" +
                                    "\n* at least two numerical character" +
                                    "\n* at least one uppercase character" +
                                    "\n* at least one special character (@#$%^&+=-)" +
                            "\n* length between 8 and 20",
                            Toast.LENGTH_SHORT).show();
                } else {
                    passwordValid = true;
                    inputPassword.setTextColor(ContextCompat.getColor(self, R.color.black));
                }

                Boolean valid = nameValid && emailValid && passwordValid;

                // checking if the user is not registered already
                if (dbManagerUsers.authorizeUser(email)) {
                    Toast.makeText(SignUpActivity.this, "This user already exists",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // inserting new user to the database
                if (valid) {
                    dbManagerUsers.insertUser(name, email, password, points, discount);

                    Intent intent = new Intent(self, MenuActivity.class);

                    // getting user data
                    String[] userData = dbManagerUsers.getUserInfo(email);

                    intent.putExtra("name", userData[0]);
                    intent.putExtra("points", Double.parseDouble(userData[1]));
                    intent.putExtra("discount", Integer.parseInt(userData[2]));
                    intent.putExtra("email", email);

                    startActivity(intent);

                    finish();

                    Toast.makeText(SignUpActivity.this, "You were succesfully registered",
                            Toast.LENGTH_SHORT).show();


                    Log.d("SIGN UP NAME", name);
                    Log.d("SIGN UP EMAIL", email);
                    Log.d("SIGN UP PASSWORD", password);
                    Log.d("SIGN UP DISCOUNT", discount + "");

                    dbManagerUsers.close();
                } else {
                    Toast.makeText(SignUpActivity.this, "Error, your information is not valid",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goBack(View v) {
        finish();
    }


    // Date Picker
    private String getToday() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String monthStr, dayStr;

        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = month + "";
        }

        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = day + "";
        }

        String str = year + " " + monthStr + " " + dayStr;
        Log.d("TEST", str);
        return str;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;

                String monthStr, dayStr;

                if (month < 10) {
                    monthStr = "0" + month;
                } else {
                    monthStr = month + "";
                }

                if (day < 10) {
                    dayStr = "0" + day;
                } else {
                    dayStr = day + "";
                }

                String str = year + " " + monthStr + " " + dayStr;

                btnDate.setText(str);
            }
        };

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                dateSetListener, year, month, day);
    }
}