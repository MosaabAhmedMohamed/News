package com.example.mosaab.news.Model;

public class User_AUTH {

    private Object id;
    private Object name;
    private Object email;
    private Object email_verified_at = null;
    private Object created_at;
    private Object updated_at;


    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(Object email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public Object getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Object created_at) {
        this.created_at = created_at;
    }

    public Object getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Object updated_at) {
        this.updated_at = updated_at;
    }
}