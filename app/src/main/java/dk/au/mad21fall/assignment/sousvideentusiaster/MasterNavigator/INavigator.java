package dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator;

public interface INavigator {
    void onFlexPostClicked();
    void onFlexPostCancelClicked();

    void onHelpPostClicked();
    void onHelpPostCancelClicked();

    void onDetailClicked(String ID);
}
