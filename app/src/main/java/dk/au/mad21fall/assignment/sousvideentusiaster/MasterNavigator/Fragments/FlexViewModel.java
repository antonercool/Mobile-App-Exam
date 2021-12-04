package dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.Fragments;

import android.util.Log;

import androidx.annotation.NonNull;
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

import javax.annotation.Nullable;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class FlexViewModel extends ViewModel {
    private SousVideRepository sousVideRepository;
    private ArrayList<FlexPostModel> flexPostModels = new ArrayList<>();
    private MutableLiveData<ArrayList<FlexPostModel>> mutableLiveData;





    public LiveData<ArrayList<FlexPostModel>> getFlexPostModels() {
            sousVideRepository.fetchNewestFlex(20)
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<FlexPostModel> flexPostModels = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FlexPostModel currentObject = document.toObject(FlexPostModel.class);
                                    currentObject.id = document.getId();
                                    flexPostModels.add(currentObject);
                                }
                                mutableLiveData.setValue(flexPostModels);
                            } else {
                                Log.d("FLEX VIEW MODEL", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        return mutableLiveData;
    }

    private void subscribeToFlexPost(){
        // Subribe to posts, when any is edited update comments
        sousVideRepository.subscribeToFlexPosts()
                .orderBy("created", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<FlexPostModel> flexPostModels = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            FlexPostModel currentObject = document.toObject(FlexPostModel.class);
                            currentObject.id = document.getId();
                            flexPostModels.add(currentObject);
                        }
                            mutableLiveData.setValue(flexPostModels);
                    }
                });
    }

    public void setFlexPostModels(ArrayList<FlexPostModel> flexPostModels) {
        this.flexPostModels = flexPostModels;
    }

    public void Init(){
        mutableLiveData = new MutableLiveData<ArrayList<FlexPostModel>>();
        mutableLiveData.setValue(flexPostModels);
        sousVideRepository = SousVideRepository.getSousVideRepositoryInstance();

        subscribeToFlexPost();
    }
}
