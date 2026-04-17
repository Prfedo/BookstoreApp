package com.bookstore.model;

public class User {
    private int id;
    private String name;
    private String username;
    private String email;
    private String password;
    private boolean isAdmin;

    public User(int id, String name,String username, String email, String password, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.username=username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public int getId()          { return id; }
    public String getName()     { return name; }
    public String getUsername() { return username; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public boolean isAdmin()    { return isAdmin; }

    public void setName(String name)       { this.name = name; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email)     { this.email = email; }
    public void setPassword(String pass)   { this.password = pass; }
    public void setAdmin(boolean isAdmin)  { this.isAdmin = isAdmin; }
}
