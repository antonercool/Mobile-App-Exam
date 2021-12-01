package dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

//simple data class (DTO)
@IgnoreExtraProperties
public class FlexPost {

    public Date created;
    public String description;
    public int hoursCooked;
    public int temp;
    ArrayList<String> labels;
    public String owner;
    public int stars;
    public String url;

    public ArrayList<Picture> pictures;
    public ArrayList<Comment> comments;

    public FlexPost(){};

    public FlexPost(Date created, String description, int hoursCooked, int temp, ArrayList<String> labels, String owner, int stars, String url, ArrayList<Picture> pictures, ArrayList<Comment> comments, int numberOfComments) {
        this.created = created;
        this.description = description;
        this.hoursCooked = hoursCooked;
        this.temp = temp;
        this.labels = labels;
        this.owner = owner;
        this.stars = stars;
        this.url = url;
        this.pictures = pictures;
        this.comments = comments;
        this.numberOfComments = numberOfComments;
    }

    @Exclude
    public int numberOfComments;



}



