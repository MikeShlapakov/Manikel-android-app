package com.example.project_part2.entities;

import android.net.Uri;

import com.example.project_part2.MainActivity;
import com.example.project_part2.R;


public class User {

    // name shown in app
    private final String firstname;
    private final String lastname;

    // Uri as string
    private String pfp;
    private String username;


    public User() {
        this("foo", "bar", null);
    }


    public User(String firstName, String lastName, String pfp) {
        this.firstname = firstName;
        this.lastname = lastName;

        if (pfp == null) {
            setDefaultPfp();
        } else { this.pfp = pfp; }
    }


    private void setDefaultPfp() {
        this.pfp = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.drawable.ddog1).toString();
    }
    public String getDisplayName() {
        return this.firstname + " " + this.lastname;
    }

    public Uri getPfp() {
        return Uri.parse(pfp);
    }

}
