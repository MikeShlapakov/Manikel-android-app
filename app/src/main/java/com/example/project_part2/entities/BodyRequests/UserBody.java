package com.example.project_part2.entities.BodyRequests;

public class UserBody {

    String displayName;
    String username;
    String password;
    String pfp;

    public UserBody(String displayName, String username, String password, String pfp) {
        this.displayName = displayName;
        this.username = username;
        this.password = password;
        this.pfp = pfp;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }
}
