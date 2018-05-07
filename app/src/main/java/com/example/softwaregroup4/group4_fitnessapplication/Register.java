package com.example.softwaregroup4.group4_fitnessapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Manny on 12/4/18.
 */



public class Register extends AppCompatActivity {

    private EditText FirstNameField;
    private EditText LastNameField;
    private EditText EmailField;
    private EditText PasswordField;
    private EditText ConfirmPassField;
    private EditText UsernameField;
    UserAccountDatabase database = new UserAccountDatabase(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerscreen);

        FirstNameField = (EditText) findViewById(R.id.FirstNameInput);
        LastNameField = (EditText) findViewById(R.id.LastNameInput);
        EmailField = (EditText) findViewById(R.id.EmailInput);
        PasswordField = (EditText) findViewById(R.id.IntialPasswordInput);
        ConfirmPassField = (EditText) findViewById(R.id.ConfirmPasswordInput);
        UsernameField = (EditText) findViewById(R.id.UsernameInput);
    }

    public void onRegisterPressed(View view){
        if(view.getId() == R.id.RegisterButton) {

            String FirstNameField1 = FirstNameField.getText().toString();
            String LastNameField1 = LastNameField.getText().toString();
            String EmailField1 = EmailField.getText().toString();
            String PasswordField1 = PasswordField.getText().toString();
            String ConfirmPassField1 = ConfirmPassField.getText().toString();
            String UsernameField1 = UsernameField.getText().toString();

            if (!PasswordField1.equals(ConfirmPassField1)) {
                Toast.makeText(this, "Passwords are NOT matching!", Toast.LENGTH_LONG).show();

            } else {
                //insert details in the database
                User user = new User();
                user.setFirstName(FirstNameField1);
                user.setLastName(LastNameField1);
                user.setEmail(EmailField1);
                user.setPassword(PasswordField1);
                user.setUsername(UsernameField1);

                database.addUser(user);

            }
        }

    }



}