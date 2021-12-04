package dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.Fragments;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.QuestionPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class QuestionViewModel extends ViewModel {
    private SousVideRepository sousVideRepository;
    private ArrayList<QuestionPostModel> questionPostModels = new ArrayList<>();
    private MutableLiveData<ArrayList<QuestionPostModel>> mutableLiveData;

    private int upload_counter = 0;
    private ImageView[] imageArray = new ImageView[5];
    private String[] imagePaths = new String[5];


    public LiveData<ArrayList<QuestionPostModel>> getHelpPostModels() {
            sousVideRepository.fetchNewestHelp(20)
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<QuestionPostModel> questionPostModels = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    QuestionPostModel currentObject = document.toObject(QuestionPostModel.class);
                                    currentObject.id = document.getId();
                                    questionPostModels.add(currentObject);
                                }
                                mutableLiveData.setValue(questionPostModels);
                            } else {
                                Log.d("QUESTION VIEW MODEL", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        return mutableLiveData;
    }

    private void subscribeToHelpPost(){
        // Subribe to posts, when any is edited update comments
        sousVideRepository.subscribeToHelpPosts()
                .orderBy("created", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<QuestionPostModel> questionPostModels = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            QuestionPostModel currentObject = document.toObject(QuestionPostModel.class);
                            currentObject.id = document.getId();
                            questionPostModels.add(currentObject);
                        }
                            mutableLiveData.setValue(questionPostModels);
                    }
                });
    }

    public void setHelpPostModels(ArrayList<QuestionPostModel> questionPostModels) {
        this.questionPostModels = questionPostModels;
    }

    public void Init(){
        mutableLiveData = new MutableLiveData<ArrayList<QuestionPostModel>>();
        mutableLiveData.setValue(questionPostModels);
        sousVideRepository = SousVideRepository.getSousVideRepositoryInstance();

        subscribeToHelpPost();
    }
}
