package dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

//simple data class (DTO)
@IgnoreExtraProperties
public class FlexPostModel {

    public Date created;
    public String description;
    public int hoursCooked;
    public int temp;
    public ArrayList<String> labels;
    public String owner;
    public int stars;
    public String url;

    public ArrayList<PictureModel> pictures;
    public ArrayList<CommentModel> comments;

    public FlexPostModel(){};

    public FlexPostModel(Date created, String description, int hoursCooked, int temp, ArrayList<String> labels, String owner, int stars, String url, ArrayList<PictureModel> pictures, ArrayList<CommentModel> comments, int numberOfComments) {
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



