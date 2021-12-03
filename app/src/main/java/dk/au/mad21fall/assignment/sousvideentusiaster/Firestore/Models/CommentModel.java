package dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models;

import java.util.Date;

public class CommentModel {
    public Date created;
    public String description;
    public String from;
    public String url;

    public CommentModel(Date created, String description, String from, String url) {
        this.created = created;
        this.description = description;
        this.from = from;
        this.url = url;
    }

    public CommentModel(){}

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
