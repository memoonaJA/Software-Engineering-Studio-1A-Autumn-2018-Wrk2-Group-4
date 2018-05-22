package com.example.softwaregroup4.group4_fitnessapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends AppCompatActivity {
    private Button loginbutt;
    private EditText username;
    private EditText password;
    private TextView wrongDEETs;
    private Button registerInitialButton;
    UserAccountDatabase Database = new UserAccountDatabase(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbutt = (Button) findViewById(R.id.LoginButton);
        username = (EditText) findViewById(R.id.UsernameText);
        password = (EditText) findViewById(R.id.PasswordText);
        wrongDEETs = (TextView) findViewById(R.id.LOLDEETS);
        registerInitialButton = (Button) findViewById(R.id.InitialRegisterButton);

    }

    public void onClick(View view) {
        String usernameString;
        String passwordString;
        usernameString = username.getText().toString();
        passwordString = password.getText().toString();

        if(view.getId() == R.id.LoginButton){
            if(TextUtils.isEmpty(usernameString) && TextUtils.isEmpty(passwordString)) {
                username.setError("Enter a username");
                password.setError("Enter a password");
                return;
            }
            else if(TextUtils.isEmpty(passwordString)){
                password.setError("Enter a password");
                return;
            }
            else if(TextUtils.isEmpty(usernameString)) {
                username.setError("Enter a username");
                return;
            }
            else if(!(TextUtils.isEmpty(usernameString) && TextUtils.isEmpty(passwordString))){
                String pass = Database.searchForPassword(usernameString);
                if(pass.equals(passwordString)){
                    Intent intent1 = new Intent(Login.this, UserScreen.class);
                    intent1.putExtra("Username1", usernameString);
                    startActivity(intent1);

                }
                else{
                    Toast.makeText(this, "USERNAME & PASSWORD DON'T MATCH", Toast.LENGTH_LONG).show();
                }

            }
        }

        //testvalidate(usernameString, passwordString);


        if(view.getId() == R.id.InitialRegisterButton){
            Intent intent2 = new Intent(Login.this, Register.class);
            startActivity(intent2);
        }


    }

    // public void testvalidate(String username, String password){
    //     String pass = Database.searchForPassword(username);
    //     if(pass.equals("")){ //FIX????

    //     }
    //         Intent intent1 = new Intent(this, UserScreen.class);
    //         intent1.putExtra("Username1", username);
    //         startActivity(intent1);
    //   finish();
    //    }


}