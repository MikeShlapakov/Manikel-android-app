package com.example.project_part2.entities;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * the class representation of a user in the app
 */
public class User {
    private final String id;
    private String user_pass;
    private String firstName;
    private String lastName;
    private Uri profilePicture;


    /**
     * default constructor
     */
    public User() {
        this("foo", "bar", null, "admin", "admin");
    }

    /**
     * 
     * @param firstName user's first name
     * @param lastName  user's last name
     * @param profilePicture user's profile picture
     */
    public User(String firstName, String lastName, Uri profilePicture, String id, String pass) {
        this.id = id; // change when implementing a DB
        this.user_pass = pass;
        this.firstName = firstName;
        this.lastName= lastName;
        this.profilePicture = profilePicture;
    }

    /**
     * Json constructor
     * @param userJson - the jsonObject holding the user Info
     */
    public User(JSONObject userJson) throws JSONException {
        // id and pass don't get passed in .json
        this.id = null;
        user_pass = null;
        this.firstName = userJson.getString("firstName");
        this.lastName = userJson.getString("lastName");
        if (userJson.getString("imageUri").equals("")) {
            this.profilePicture = null;
        } else {
            this.profilePicture = Uri.parse(userJson.getString("imageUri"));
        }
    }

    /**
     * first name getter
     * @return a copy of the firstName String
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * last name getter
     * @return a copy of the lastName String
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * profile picture getter
     * @return a copy of the profile picture's Uri object
     */
    public Uri getProfilePictureUri() {
        if (this.profilePicture == null) {
            return null;
        }
        String uriString = this.profilePicture.toString();
        return Uri.parse(uriString);
    }

    public String getId() {
        return this.id;
    }

    public String getPass() {
        return  this.user_pass;
    }

    public void setProfilePicture(Uri profilePicture) {
        this.profilePicture = profilePicture;
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
}
