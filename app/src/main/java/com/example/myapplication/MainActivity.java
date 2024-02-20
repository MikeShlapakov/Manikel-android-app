package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // link xml
        setContentView(R.layout.activity_main);
        // we want the button to redirect us to sign_up page
        Button signup_button = findViewById(R.id.signup_button);
        LinearLayout form_layout = findViewById(R.id.form_layout);
        Button login_button = form_layout.findViewById(R.id.login_button);

        // use lambda instead of full abstract class implementation
        signup_button.setOnClickListener(v -> {
            Intent i = new Intent(this, SignupPage.class);
            // if button pressed start SignupPage
            startActivity(i);
        });

        login_button.setOnClickListener(v -> {
            Intent i = new Intent(this, FeedPage.class);
            startActivity(i);
        });
    }
}