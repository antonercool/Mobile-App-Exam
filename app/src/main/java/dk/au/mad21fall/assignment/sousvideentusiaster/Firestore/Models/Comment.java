package dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models;

import java.util.Date;

public class Comment {
    public Date created;
    public String description;
    public String from;
    public String url;

    public Comment(Date created, String description, String from, String url) {
        this.created = created;
        this.description = description;
        this.from = from;
        this.url = url;
    }

    public Comment(){}
}
