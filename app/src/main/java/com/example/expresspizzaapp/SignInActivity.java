package com.example.expresspizzaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    Button btnSignIn;
    Context self = this;
    private DBManagerUsers dbManagerUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignIn = findViewById(R.id.btnAddAddress);

        dbManagerUsers = new DBManagerUsers(this);
        dbManagerUsers.open();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting input
                String email;
                String password;

                EditText inputEmail = findViewById(R.id.inputAddress);
                EditText inputPassword = findViewById(R.id.inputPasswordSignIn);

                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();

                // validating input
                Boolean emailValid = true;
                Boolean passwordValid = true;

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

                // -- password validation
                String passwordRegex = "^(?=.*\\d.*\\d)(?=.*[A-Z])(?=.*[@#$%^&+=-]).{8,20}$";
                Pattern passwordPattern = Pattern.compile(passwordRegex);
                Matcher passwordMatcher = passwordPattern.matcher(password);

                if (!passwordMatcher.matches() || password.trim().length() == 0) {
                    passwordValid = false;
                    inputPassword.setTextColor(ContextCompat.getColor(self, R.color.red));
                    Toast.makeText(self, "Your passowrd should have:" +
                                    "\n* at least two numerical character" +
                                    "\n* at least one uppercase character" +
                                    "\n* at least one special character (@#$%^&+=-)" +
                                    "\n* length between 8 and 20",
                            Toast.LENGTH_SHORT).show();
                } else {
                    passwordValid = true;
                    inputPassword.setTextColor(ContextCompat.getColor(self, R.color.black));
                }

                Boolean valid = emailValid && passwordValid;

                if (valid) {
                    // user authorization
                    if (dbManagerUsers.authorizeUserPassword(email, password)) {
                        Toast.makeText(self, "You were successfully logged in",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(self, MenuActivity.class);

                        // getting user data
                        String[] userData = dbManagerUsers.getUserInfo(email);
                        Log.d("POINTS ARE: ", userData[1] + "");

                        intent.putExtra("name", userData[0]);
                        intent.putExtra("points", Double.parseDouble(userData[1]));
                        intent.putExtra("discount", Integer.parseInt(userData[2]));
                        intent.putExtra("email", email);

                        startActivity(intent);

                        finish();
                    } else {
                        Toast.makeText(self, "Error. User not found",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void goBack(View v) {
        finish();
    }
}