package com.example.project_part2.util;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.project_part2.entities.User;
import com.example.project_part2.entities.UserDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MyApplication extends Application {

    public static Context context;
    public static MutableLiveData<String> token;

    public static MutableLiveData<User> registeredUser;

    public static MutableLiveData<List<String>> friends;

    public static MutableLiveData<List<String>> incoming;

    public static UserDao.UserDB db;

    public static UserDao userDao;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        initLocals();
    }

    public static void initLocals () {

        if (token == null) {
            token = new MutableLiveData<>();
        }

        if (db == null) {
            db = Room.databaseBuilder(context, UserDao.UserDB.class, "UsersDao")
                    .allowMainThreadQueries().build();
        }

        if (userDao == null) {
            userDao = db.userDao();
        }

        if (registeredUser == null) {
//            // init registeredUser with default user
            MyApplication.registeredUser = new MutableLiveData<>();
//            MyApplication.registeredUser.setValue(new User());
//
//            // wont be null cuz we just instantiated it
//            MyApplication.insertRegisteredUserToLocalDB();
        }
    }


    // adds it to local db, and sets its lid
    public static void insertRegisteredUserToLocalDB () {

        if (MyApplication.registeredUser.getValue() == null) { return; }

        MyApplication.registeredUser.getValue().set_lid(MyApplication.userDao.insert(MyApplication.registeredUser.getValue())[0]);
    }

    public static String getCurrentDate () {
        // Get the current date
        Date currentDate = new Date();

        // Define the desired date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());

        // Set the timezone to UTC
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(currentDate);
    }
}
