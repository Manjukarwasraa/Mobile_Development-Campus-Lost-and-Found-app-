package com.example.project1;

public class Item {
    public int id;
    public String type;
    public String title;
    public String description;
    public String category;
    public String date;
    public String imageUri;
    public String phone;
    public String email;
    public int resolved;

    public Item() {}

    public Item(int id, String type, String title, String description, String category,
                String date, String imageUri, String phone, String email, int resolved) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
        this.imageUri = imageUri;
        this.phone = phone;
        this.email = email;
        this.resolved = resolved;
    }
}
