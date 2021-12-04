package dk.au.mad21fall.assignment.sousvideentusiaster.DetailView;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.CommentModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.QuestionPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class DetailHelpViewModel extends ViewModel {
    private SousVideRepository sousVideRepository;
    private ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();
    private QuestionPostModel helpPostModel = new QuestionPostModel();
    private MutableLiveData<ArrayList<CommentModel>> mutableLiveData;
    private MutableLiveData<QuestionPostModel> helpPostModelMutableLiveData;


    public LiveData<QuestionPostModel> getHelpPostModels(String ID) {
        sousVideRepository.fetchHelpPostByID(ID).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    QuestionPostModel questionPostModel = task.getResult().toObject(QuestionPostModel.class);
                    helpPostModelMutableLiveData.setValue(questionPostModel);
                }
            }
        });
        return helpPostModelMutableLiveData;
    }

    private void subscribeToHelpComments(String ID){
        sousVideRepository.subscribeToHelpComments(ID)
                .orderBy("created", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<CommentModel> newComments = new ArrayList<>();
                        for(QueryDocumentSnapshot doc : value){
                            CommentModel newBroadcastedComment = doc.toObject(CommentModel.class);
                            newComments.add(newBroadcastedComment);
                        }
                        mutableLiveData.setValue(newComments);
                    }
                });
    }

    public void postHelpComment(String ID, CommentModel commentModel){
        sousVideRepository.postHelpComment(ID,commentModel);
    }

    public LiveData<ArrayList<CommentModel>> getHelpPostComments(String ID){
        sousVideRepository.fetchHelpCommentsByPostID(ID).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CommentModel currentObject = document.toObject(CommentModel.class);
                        commentModelArrayList.add(currentObject);
                    }
                    mutableLiveData.setValue(commentModelArrayList);
                } else {
                    Log.d("DETAILS HELP VIEW MODEL", "Error getting documents: ", task.getException());
                }
            }
        });
        return mutableLiveData;
    }

    public void Init(String ID){
        mutableLiveData = new MutableLiveData<ArrayList<CommentModel>>();
        helpPostModelMutableLiveData = new MutableLiveData<>();

        mutableLiveData.setValue(commentModelArrayList);
        helpPostModelMutableLiveData.setValue(helpPostModel);

        sousVideRepository = SousVideRepository.getSousVideRepositoryInstance();

        subscribeToHelpComments(ID);

    }
}
