package dk.au.mad21fall.assignment.sousvideentusiaster.ListView;

//simple data class (DTO)
public class HelpPost {
    public String userName;
    public String timePosted;
    public String chipText01;
    public String chipText02;
    public String postText;

    public String chipStatus;
    public int numberOfComments;

    public HelpPost(String userName, String timePosted, String chipText01, String chipText02, String postText, String chipStatus, int numberOfComments) {
        this.userName = userName;
        this.timePosted = timePosted;
        this.chipText01 = chipText01;
        this.chipText02 = chipText02;
        this.postText = postText;
        this.chipStatus = chipStatus;
        this.numberOfComments = numberOfComments;
    }
}
