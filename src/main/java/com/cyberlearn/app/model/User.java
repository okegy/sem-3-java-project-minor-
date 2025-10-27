package com.cyberlearn.app.model;

public class User {
    private String username;
    private String passwordHash;
    private String role; // "ADMIN" or "STUDENT"
    private boolean active = true;

    public User(){}
    public User(String username, String passwordHash, String role){
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = true;
    }

    public User(String username, String passwordHash, String role, boolean active){
        this.username = username; this.passwordHash=passwordHash; this.role=role; this.active=active;
    }
    public String getUsername(){ return username; }
    public String getPasswordHash(){ return passwordHash; }
    public String getRole(){ return role; }
    public void setUsername(String v){ username=v; }
    public void setPasswordHash(String v){ passwordHash=v; }
    public void setRole(String v){ role=v; }

    public boolean isActive(){ return active; }
    public void setActive(boolean v){ active=v; }
}
