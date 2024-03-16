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
    }

    public void goRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goFeed(View view) {

        EditText username = findViewById(R.id.usernameLogin);
        EditText pass = findViewById(R.id.passwordLogin);

        if (checkCredentials(username.getText().toString(), pass.getText().toString())) {

            tokenAPI.createToken(MyApplication.registeredUser.getValue().id(), MyApplication.token);
            Intent intent = new Intent(this, FeedActivity.class);
            startActivity(intent);

        } else {
            alertWrongUserInfo(view);
        }
    }

    // CHECK
    private boolean checkCredentials (String username, String password) {

        // this will give us the correct user that is on the local database,
        // if not registered, registeredUser will hold the default user
        // if registered, registeredUser will hold a true user.
        // *registeredUser is never null*
        User user = MyApplication.userDao.get(MyApplication.registeredUser.getValue().lid());

        // if not found in local db, check with server
        if (user == null) {

            // create a local Mutable user that holds the entered username
            MutableLiveData<User> userFromServer = new MutableLiveData<>();
            User temp = new User(); temp.setUsername(username);
            userFromServer.setValue(temp);

            final boolean[] stop = {false};
            int timeoutLoopCount = 200;

            userFromServer.observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    stop[0] = true;
                }
            });

            // find user in server with same username,
            // and update our user (we want its newly set password)
            userAPI.updateUser(userFromServer);

            for (int i = 0; i < timeoutLoopCount; i++) {

                if (stop[0]) { break; }

                if (i == timeoutLoopCount-1) {
                    // TIMEOUT
                    Toast.makeText(MyApplication.context, "server request for user timed out", Toast.LENGTH_SHORT).show();
                    return false;
                }

                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // if not in server, then doesn't exist at all
            if (userFromServer.getValue() == null) { return false; }

            // check if passwords match
            return userFromServer.getValue().password().equals(password);
        }

        // if exists in local db
        return user.password().equals(password);
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