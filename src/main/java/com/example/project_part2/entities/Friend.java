package com.example.project_part2.entities;

public class Friend {

    private String displayName;
    private String pfp;
    private String id;

    public Friend(String displayName, String pfp, String id) {
        this.displayName = displayName;
        this.pfp = pfp;
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
