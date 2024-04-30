package com.example.project_part2.entities;

import android.content.Context;
import android.net.Uri;

import com.example.project_part2.R;

import org.json.JSONException;
import org.json.JSONObject;


public class User {
    private String id;
    private String user_pass;
    private String firstName;
    private String lastName;
    private String login_username;
    private Uri pfp;



    public User() {
        this("foo", "bar", null, "admin", "admin");
        login_username = "admin";
    }


    public User(String firstName, String lastName, Uri pfp, String id, String pass) {
        this.id = id; // change when implementing a DB
        this.user_pass = pass;
        this.firstName = firstName;
        this.lastName= lastName;
        this.pfp = pfp;
    }


    public User(JSONObject userJson) throws JSONException {

        this.firstName = userJson.getString("first_name");

        this.lastName = userJson.getString("last_name");


        if (userJson.has("pfp")) {
            if (userJson.getString("pfp").equals("")) {
                this.pfp = null;
            } else {
                this.pfp = Uri.parse(userJson.getString("pfp"));
            }
        } else {
            this.pfp = null;
        }


        if (userJson.has("user_pass")) {
            if (userJson.getString("user_pass").equals("")) {
                this.user_pass = null;
            } else {
                this.user_pass = userJson.getString("user_pass");
            }
        } else {
            this.user_pass = null;
        }


        if (userJson.has("id")) {
            if (userJson.getString("id").equals("")) {
                this.id = null;
            } else {
                this.id = userJson.getString("id");
            }
        } else {
            this.id = null;
        }
    }


    public String getFirstName() {
        return this.firstName;
    }


    public String getLastName() {
        return this.lastName;
    }


    public Uri getProfilePictureUri() {
        if (this.pfp == null) {
            return null;
        }
        String uriString = this.pfp.toString();
        return Uri.parse(uriString);
    }

    public String getId() {
        return this.id;
    }

    public String getPass() {
        return  this.user_pass;
    }

    public void setId (String id) {
        this.id = id;
    }
    public void setProfilePicture(Uri pfp) {
        this.pfp = pfp;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setUser_pass(String pass) {
        this.user_pass = pass;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin_username() {
        return login_username;
    }

    public void setLogin_username(String login_username) {
        this.login_username = login_username;
    }
}
