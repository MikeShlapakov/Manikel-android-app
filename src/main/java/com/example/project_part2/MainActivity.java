package com.example.project_part2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.example.project_part2.apis.TokenAPI;
import com.example.project_part2.apis.UserAPI;
import com.example.project_part2.entities.User;
import com.example.project_part2.entities.UserDao;
import com.example.project_part2.util.MyApplication;

public class MainActivity extends AppCompatActivity {

    public static String PACKAGE_NAME = "com.example.project_part2";
    UserAPI userAPI;
    private TokenAPI tokenAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set the logo color to purple
        ImageView logo = findViewById(R.id.loginIcon);
        logo.setColorFilter(R.color.AppPrimary);

        // init myApplication's fields
        MyApplication.initLocals();

//      PACKAGE_NAME = getApplicationContext().getPackageName();
        tokenAPI = new TokenAPI();
        userAPI = new UserAPI();
    }

    public void goRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goFeed(View view) {

        EditText username = findViewById(R.id.usernameLogin);
        EditText password = findViewById(R.id.passwordLogin);

        MutableLiveData<Boolean> response = new MutableLiveData<>();

        userAPI.checkCredentials(username.getText().toString(), password.getText().toString(), response);
        response.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (response.getValue()) {
                    gotoFeed();
                } else {
                    alertWrongUserInfo(view);
                }
            }
        });
    }

    private void gotoFeed () {
        tokenAPI.createToken(MyApplication.registeredUser.getValue().id(), MyApplication.token);
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
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