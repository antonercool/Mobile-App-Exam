package dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

//simple data class (DTO)
@IgnoreExtraProperties
public class FlexPostModel {

    public String title;
    public Date created;
    public String description;
    public int hoursCooked;
    public int temp;
    public ArrayList<String> labels;
    public String owner;
    public int stars;
    public String url;
    public int numberOfComments;
    @Exclude
    public String ID;

    public ArrayList<PictureModel> pictures;
    public ArrayList<CommentModel> comments;


    public FlexPostModel(){};

    public FlexPostModel(Date created, String description, int hoursCooked, int temp, ArrayList<String> labels, String owner, int stars, String url, ArrayList<PictureModel> pictures, ArrayList<CommentModel> comments, int numberOfComments, String title) {
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
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public int getHoursCooked() {
        return hoursCooked;
    }

    public void setHoursCooked(int hoursCooked) {
        this.hoursCooked = hoursCooked;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public ArrayList<PictureModel> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<PictureModel> pictures) {
        this.pictures = pictures;
    }

    public ArrayList<CommentModel> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentModel> comments) {
        this.comments = comments;
    }
}



