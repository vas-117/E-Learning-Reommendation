package com.elearning.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String preferences;

    public User() {}

    public User(int id, String name, String email, String preferences) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.preferences = preferences;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }

    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", preferences=" + preferences + "]";
    }
}
