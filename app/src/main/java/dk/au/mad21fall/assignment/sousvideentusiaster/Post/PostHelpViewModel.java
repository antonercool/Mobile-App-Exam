package dk.au.mad21fall.assignment.sousvideentusiaster.Post;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.QuestionPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class PostHelpViewModel extends ViewModel {

    public int uploadCounter;
    public String[] imagePaths;
    private SousVideRepository sousVideRepository;


    public void postNewQuestion(QuestionPostModel questionPostModel, String[] imagePaths, int uploadCounter, Activity activity){
        sousVideRepository.postNewQuestionPostAsync(questionPostModel, imagePaths, uploadCounter, activity);
    }

    public void init(){
        uploadCounter = 0;
        imagePaths = new String[5];
        sousVideRepository = SousVideRepository.getSousVideRepositoryInstance();
    }
}
