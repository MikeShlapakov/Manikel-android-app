package com.example.project_part2.entities;

import android.content.ContentResolver;
import android.net.Uri;

import com.example.project_part2.MainActivity;
import com.example.project_part2.R;

import org.json.JSONException;
import org.json.JSONObject;


public class User {

    // name shown in app
    private final String firstName;
    private final String lastName;

    // name used in login
    private String username;
    private String password;

    // pfp
    private Uri pfp;



    public User() {
        this("foo", "bar", "foobar", "foobar", null);
    }


    public User(String firstName, String lastName, String username, String password, Uri pfp) {
        this.firstName = firstName;
        this.lastName= lastName;
        this.username = username;
        this.password = password;

        if (pfp == null) {
            setDefaultPfp();
        } else { this.pfp = pfp; }
    }


    public User(JSONObject userJson) throws JSONException {

        this.firstName = userJson.getString("first_name");
        this.lastName = userJson.getString("last_name");

        // set pfp from json
        if (userJson.has("pfp") && !userJson.getString("pfp").equals("")) {
            this.pfp = Uri.parse(userJson.getString("pfp"));
        } else {
            setDefaultPfp();
        }
    }

    private void setDefaultPfp() {
        this.pfp = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.drawable.ddog1);
    }
    public String getDisplayName() {
        return this.firstName + " " + this.lastName;
    }

    public Uri getPfp() {
        return pfp;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPass() {
        return this.password;
    }
}
