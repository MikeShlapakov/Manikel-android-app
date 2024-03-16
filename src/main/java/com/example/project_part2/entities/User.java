package com.example.project_part2.entities;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.project_part2.MainActivity;
import com.example.project_part2.R;

import java.util.List;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private long _lid;

    private String _id;

    private String displayName;

    private String username;

    private String password;

    // Uri as b64 string
    private String pfp;

    @Ignore
    private List<String> friends;

    @Ignore
    private List<String> friendRequests;

    @Ignore
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

//    public Uri getPfp() {
//        return Uri.parse(pfp);
//    }
    public String getPfp() {
        return this.pfp;
    }

    public String id() { return _id; }

    public long lid() { return _lid; }

    public String username() { return username; }

    public String password() { return password; }


    public void set_lid(long _lid) {
        this._lid = _lid;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public void setDisplayName (String displayName) {
        this.displayName = displayName;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<String> friendRequests) {
        this.friendRequests = friendRequests;
    }
}
