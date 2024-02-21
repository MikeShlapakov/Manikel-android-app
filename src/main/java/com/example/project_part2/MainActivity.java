package com.example.project_part2;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_part2.entities.User;

public class MainActivity extends AppCompatActivity {
    public static User registeredUser = new User();

    int magic (int n) {
        return n;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set the logo color to purple
        ImageView logo = findViewById(R.id.loginIcon);
        logo.setColorFilter(R.color.AppPrimary);

        registeredUser.setProfilePicture(Uri.parse(Integer.toString(R.drawable.dog)));
    }

    public void goRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goFeed(View view) {
        EditText id = findViewById(R.id.usernameLogin);
        EditText pass = findViewById(R.id.passwordLogin);
        if (id.getText().toString().equals(registeredUser.getId()) && pass.getText().toString().equals(registeredUser.getPass())) {
            Intent intent = new Intent(this, FeedActivity.class);
            startActivity(intent);
        } else {
            alertWrongUserInfo(view);
        }
    }

    private void alertWrongUserInfo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("oops");
        builder.setMessage("You've entered the wrong username or password");
        builder.setNegativeButton("Try Again", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("Create an account", (dialog, which) -> goRegister(view));

        builder.show();
    }
}