package dk.au.mad21fall.assignment.sousvideentusiaster.DetailView;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.CommentModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.PictureModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.Fragments.Flex;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class DetailFlexViewModel extends ViewModel {
    private SousVideRepository sousVideRepository;
    private ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();
    private FlexPostModel flexPostModel = new FlexPostModel();
    private MutableLiveData<ArrayList<CommentModel>> mutableLiveData;
    private MutableLiveData<FlexPostModel> flexPostModelMutableLiveData;


    public LiveData<FlexPostModel> getFlexPostModels(String ID) {
        sousVideRepository.fetchFlexPostByID(ID).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    FlexPostModel flexPostModel = task.getResult().toObject(FlexPostModel.class);
                    flexPostModelMutableLiveData.setValue(flexPostModel);
                }
            }
        });
        return flexPostModelMutableLiveData;
    }

    private void subscribeToFlexComments(String ID){
        sousVideRepository.subscribeToFlexComments(ID)
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
    public void postFlexComment(String ID, CommentModel commentModel){
        sousVideRepository.postFlexComment(ID,commentModel);
    }

    public LiveData<ArrayList<CommentModel>> getFlexPostComments(String ID){
        sousVideRepository.fetchFlexCommentsByPostID(ID).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    Log.d("DETAILS FLEX VIEW MODEL", "Error getting documents: ", task.getException());
                }
            }
        });
        return mutableLiveData;
    }

    public void Init(String ID){
        mutableLiveData = new MutableLiveData<ArrayList<CommentModel>>();
        flexPostModelMutableLiveData = new MutableLiveData<>();

        mutableLiveData.setValue(commentModelArrayList);
        flexPostModelMutableLiveData.setValue(flexPostModel);

        sousVideRepository = SousVideRepository.getSousVideRepositoryInstance();

        subscribeToFlexComments(ID);

    }
}
