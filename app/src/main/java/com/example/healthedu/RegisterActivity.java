package com.example.healthedu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthedu.Database.DBHelper;

public class RegisterActivity extends AppCompatActivity {
    TextView signUpName, signUpEmail, signUpUserName, signUpPassword, signUpConfirmPassword,btn_register,link_signin;
    String name, email, userName, password, confirmPassword;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signUpName = findViewById(R.id.signup_name);
        signUpEmail = findViewById(R.id.signup_email);
        signUpUserName = findViewById(R.id.signup_usrname);
        signUpPassword = findViewById(R.id.signup_password);
        signUpConfirmPassword = findViewById(R.id.signup_confirm_pwd);
        btn_register =  findViewById(R.id.btn_register);
        link_signin = findViewById(R.id.link_signin);
        db = new DBHelper(this);


        link_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = signUpName.getText().toString().trim();
                email = signUpEmail.getText().toString().trim();
                userName = signUpUserName.getText().toString().trim();
                password = signUpPassword.getText().toString().trim();
                confirmPassword = signUpConfirmPassword.getText().toString().trim();

                if(name.equals("")|| email.equals("")|| userName.equals("")|| password.equals("")|| confirmPassword.equals("")){
                    Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    if(password.equals(confirmPassword))
                    {
                        Boolean checkusername = db.checkUsername(userName);
                        if (checkusername==false) {
                            Boolean insert = db.insertUser(userName, name, email, password);
                            if (insert == true) {
                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registered Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Password and Confirm Password fields are not matching!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}