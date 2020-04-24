package com.example.goingroguedesign.ui.account;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

public class ValidateInput {
    private Context context;
    private EditText email, password, repeatPassword, username, firstname, lastname, phoneNumber, oldPassword;

    String emailInput, passwordInput, repeatPasswordInput, oldPasswordInput, usernameInput, firstnameInput, lastnameInput, phoneInput;

    ValidateInput(Context myContext, EditText myEmail, EditText myPassword) {
        context = myContext;
        email = myEmail;
        password = myPassword;
    }

    public ValidateInput(Context myContext, EditText myEmail, EditText myPassword, EditText myRepeatPassword, EditText myUsername, EditText myFirstname, EditText myLastName) {
        context = myContext;
        email = myEmail;
        password = myPassword;
        repeatPassword = myRepeatPassword;
        username = myUsername;
        firstname = myFirstname;
        lastname = myLastName;

    }
    //bad practice, assign everything with something to check, use one of the validate function below
    ValidateInput (Context myContext, EditText somethingToCheck) {
        context = myContext;
        email = somethingToCheck;
        password = somethingToCheck;
        repeatPassword = somethingToCheck;
        username = somethingToCheck;
        firstname = somethingToCheck;
        lastname = somethingToCheck;
        phoneNumber = somethingToCheck;
    }

    ValidateInput(Context myContext, EditText op, EditText p, EditText rp) {
        context = myContext;
        oldPassword = op;
        password = p;
        repeatPassword = rp;
    }

    public boolean validatePhoneNumber() {
        phoneInput = phoneNumber.getText().toString().trim();

        if(phoneInput.isEmpty()) {
            Toast.makeText(context, "Please enter your Phone Number.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!Patterns.PHONE.matcher(phoneInput).matches()) {
            Toast.makeText(context, "Invalid Phone Number.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    public boolean validateEmail() {
        emailInput = email.getText().toString().trim();

        if(emailInput.isEmpty()) {
            Toast.makeText(context, "Please enter your Email Address.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            Toast.makeText(context, "Invalid Email Address.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    public boolean validatePassword() {
        passwordInput = password.getText().toString().trim();

        if(passwordInput.isEmpty()) {
            Toast.makeText(context, "Please enter your Password.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(passwordInput.length() < 8) {
            Toast.makeText(context, "Password too short.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    public boolean validateRepeatPassword() {
        repeatPasswordInput = repeatPassword.getText().toString().trim();

        if(repeatPasswordInput.isEmpty()) {
            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!repeatPasswordInput.equals(passwordInput)) {
            Toast.makeText(context, "Passwords don't match.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }

    }

    public boolean validateUpdatePassword() {
        oldPasswordInput = oldPassword.getText().toString().trim();
        repeatPasswordInput = repeatPassword.getText().toString().trim();
        passwordInput = password.getText().toString().trim();

        if (oldPasswordInput.isEmpty() || repeatPasswordInput.isEmpty() || passwordInput.isEmpty()) {
            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return false;
        } else if(passwordInput.equals(oldPasswordInput)) {
            Toast.makeText(context, "New password cannot be the same as the old password.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!repeatPasswordInput.equals(passwordInput)) {
            Toast.makeText(context, "Passwords don't match.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(passwordInput.length() < 8) {
            Toast.makeText(context, "Password too short.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }

    }
    public boolean validateUsername() {
        usernameInput = username.getText().toString().trim();

        if(usernameInput.isEmpty()) {
            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }

    }

    public boolean validateFirstName() {
        firstnameInput = firstname.getText().toString().trim();

        if(firstnameInput.isEmpty()) {
            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }

    }

    public boolean validateLastName() {
        lastnameInput = lastname.getText().toString().trim();

        if(lastnameInput.isEmpty()) {
            Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }

    }

}
