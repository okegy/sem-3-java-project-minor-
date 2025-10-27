package com.cyberlearn.app.model;

public class PasswordEntry {
    private String site;
    private String username;
    private String password;
    private String note;

    public PasswordEntry() {}

    public PasswordEntry(String site, String username, String password, String note) {
        this.site = site;
        this.username = username;
        this.password = password;
        this.note = note;
    }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
