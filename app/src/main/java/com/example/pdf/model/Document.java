package com.example.pdf.model;

import java.io.Serializable;

public class Document implements Serializable {
    private String documentUri;
    private String documentName;
    private String documentAuthor;
    private String documentRelease;
    private String documentDescription;
    private double documentPrice;
    private String documentImage;

    public Document(){

    }

    public Document(String documentUri, String documentName, String documentAuthor, String documentRelease, String documentDescription, double documentPrice, String documentImage) {
        this.documentUri = documentUri;
        this.documentName = documentName;
        this.documentAuthor = documentAuthor;
        this.documentRelease = documentRelease;
        this.documentDescription = documentDescription;
        this.documentPrice = documentPrice;
        this.documentImage = documentImage;
    }

    public String getDocumentUri() {
        return documentUri;
    }

    public void setDocumentUri(String documentUri) {
        this.documentUri = documentUri;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentAuthor() {
        return documentAuthor;
    }

    public void setDocumentAuthor(String documentAuthor) {
        this.documentAuthor = documentAuthor;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public double getDocumentPrice() {
        return documentPrice;
    }

    public void setDocumentPrice(double documentPrice) {
        this.documentPrice = documentPrice;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

    public String getDocumentRelease() {
        return documentRelease;
    }

    public void setDocumentRelease(String documentRelease) {
        this.documentRelease = documentRelease;
    }
}
