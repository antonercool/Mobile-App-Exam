package dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.PostFlexAdapter;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.INavigator;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class Flex extends Fragment implements PostFlexAdapter.IPostItemClickedListener {

    //How many items to generate
    public static final int NUM_ITEMS = 20;
    private static final String TAG = "FLEX";

    //widgets
    private RecyclerView rcvList;
    private PostFlexAdapter adapter;
    private SousVideRepository sousVideRepository;

    //data (should come from Firebase
    private ArrayList<FlexPostModel> flexPostArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.flex_fragment, container, false);
        setupUIElements(view);

        sousVideRepository = SousVideRepository.getSousVideRepositoryInstance();

        updateUi();

        return view;
    }

    private void updateUi() {
        sousVideRepository.fetchNewestFlex(NUM_ITEMS)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FlexPostModel currentObject = document.toObject(FlexPostModel.class);
                                currentObject.id = document.getId();
                                flexPostArrayList.add(currentObject);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        adapter.updateFlexPostList(flexPostArrayList);
                    }
                });

        // Subribe to posts, when any is edited update comments
        sousVideRepository.subscribeToFlexPosts()
                .orderBy("created", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<FlexPostModel> newFlexModels = new ArrayList<FlexPostModel>();
                        for (DocumentSnapshot doc : value){
                            FlexPostModel changedObject = doc.toObject(FlexPostModel.class);
                            changedObject.setId(doc.getId());

                            newFlexModels.add(changedObject);
                        }
                        adapter.updateFlexPostList(newFlexModels);
                    }
                });
    }


    private void setupUIElements(View view){
        //set up recyclerview with adapter and layout manager
        adapter = new PostFlexAdapter(this);
        adapter.addContext(getContext());
        rcvList = view.findViewById(R.id.recyclerView_flex_fragment);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageView postNewFlex = view.findViewById(R.id.flex_postBttn);
        postNewFlex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((INavigator)getActivity()).onFlexPostClicked();
            }
        });

        rcvList.setAdapter(adapter);

        //create data and update adapter/recyclerview
    }

    /*
    private void createData() {
        flexPostArrayList = new ArrayList<FlexPostModel>();
        Random r = new Random();
        for(int i = 0; i < NUM_ITEMS; i++){
            flexPostArrayList.add(new FlexPostModel("Matias munkeskider " + i,
                    i + " Minutes ago.",
                    "Mørbrad " + i,
                    "Svinekam " + i,
                    "Lorem Ipsum is simply dummy text of the printing" +
                            " and typesetting industry. Lorem Ipsum has been the" +
                            " industry's standard dummy text ever since the 1500s," +
                            " when an unknown printer took a galley of type and scrambled" +
                            " it to make a",
                    i ,
                    i,
                    i,
                    i
                    ));
        }
    }*/

    @Override
    public void onPostClicked(String ID) {
        ((INavigator)getActivity()).onDetailFlexClicked(ID);
    }
}
