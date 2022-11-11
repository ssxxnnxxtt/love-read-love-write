package com.example.pdf.model;

import java.io.Serializable;

public class DataBox implements Serializable {
    private Document document;
    private User user;


    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
