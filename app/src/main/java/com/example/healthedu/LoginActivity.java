package com.example.healthedu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthedu.Database.DBHelper;

public class LoginActivity extends AppCompatActivity {
    TextView linkSignUp, btn_login;
    EditText login_username, login_password;
    String _username, _password;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);
        linkSignUp = findViewById(R.id.link_signup);
        login_password = findViewById(R.id.login_password);
        login_username = findViewById(R.id.login_username);
        btn_login = findViewById(R.id.btn_login);

        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _username = login_username.getText().toString().trim();
                _password = login_password.getText().toString().trim();

                if (_username.equals("") || _password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Boolean checkUsernameandPassword = db.checkUsernameAndPassword(_username, _password);
                    if (checkUsernameandPassword == true) {
                        Toast.makeText(LoginActivity.this, "Signed in Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    else
                    {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}