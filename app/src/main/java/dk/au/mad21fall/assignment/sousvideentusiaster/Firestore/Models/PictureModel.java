package dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models;

public class PictureModel {
    public String url;

    public PictureModel(String url) {
        this.url = url;
    }
    public PictureModel(){}


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
