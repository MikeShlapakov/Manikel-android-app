package com.example.project_part2.entities;

import android.net.Uri;

import com.example.project_part2.MainActivity;
import com.example.project_part2.R;

import java.util.List;


public class User {

    private String _id;

    private final String displayName;

    private String username;

    private String password;

    // Uri as b64 string
    private String pfp;

//
//    private List<String> friends;
//
//    private List<String> friendRequests;

    public User() {
        this("foobar", "foobar", "123", null);
    }

    public User(String displayName, String username, String password, String pfp) {
        this.displayName = displayName;
        this._id = "";
        this.username = username;
        this.password = password;
        // TODO: b64
        this.pfp = pfp;
    }

//    private void setDefaultPfp() {
//        this.pfp = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.drawable.ddog1).toString();
//    }
    public String getDisplayName() {
        return this.displayName;
    }

    public Uri getPfp() {
        return Uri.parse(pfp);
    }

    public String id() { return _id; }

    public String username() { return username; }

    public String password() { return password; }

}
