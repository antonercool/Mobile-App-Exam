package dk.au.mad21fall.assignment.sousvideentusiaster.ListView;

//simple data class (DTO)
public class FlexPost {
    public String userName;
    public String timePosted;
    public String chipText01;
    public String chipText02;
    public String postText;

    public int numberOfComments;
    public int rating;
    public int hoursCooked;
    public int degrees;

    public FlexPost(String userName, String timePosted, String chipText01, String chipText02, String postText, int numberOfComments, int rating, int hoursCooked, int degrees) {
        this.userName = userName;
        this.timePosted = timePosted;
        this.chipText01 = chipText01;
        this.chipText02 = chipText02;
        this.postText = postText;
        this.numberOfComments = numberOfComments;
        this.rating = rating;
        this.hoursCooked = hoursCooked;
        this.degrees = degrees;
    }
}
