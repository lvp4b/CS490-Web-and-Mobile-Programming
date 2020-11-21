package com.github.lvp4b.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void redirectToSignUpPage(View v) {
        Intent redirect = new Intent(com.github.lvp4b.firebase.WelcomeActivity.this, SignUpActivity.class);
        startActivity(redirect);
    }

    public void redirectToSignInPage(View v) {
        Intent redirect = new Intent(com.github.lvp4b.firebase.WelcomeActivity.this, LoginActivity.class);
        startActivity(redirect);
    }
}