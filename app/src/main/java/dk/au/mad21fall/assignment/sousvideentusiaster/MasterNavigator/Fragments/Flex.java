package dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.FlexPost;
import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.PostFlexAdapter;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.INavigator;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class Flex extends Fragment implements PostFlexAdapter.IPostItemClickedListener {

    //How many items to generate
    public static final int NUM_ITEMS = 50;
    private static final String TAG = "FLEX";

    //widgets
    private RecyclerView rcvList;
    private PostFlexAdapter adapter;
    private SousVideRepository sousVideRepository;

    //data (should come from Firebase
    private ArrayList<FlexPost> flexPostArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.flex_fragment, container, false);
        setupUIElements(view);

        sousVideRepository = SousVideRepository.getSousVideRepositoryInstance();
        String currentDate =  "Fri Dec 03 10:12:32 GMT+01:00 2021";
        Date fetchFrom = new Date();

        // FETCH EXAMLE
        /*
        sousVideRepository.getNewestBatchFromTime(fetchFrom, 3)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<FlexPostModel> fetchedModels = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fetchedModels.add(document.toObject(FlexPostModel.class));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
         */

        return view;
    }


    private void setupUIElements(View view){
        //set up recyclerview with adapter and layout manager
        adapter = new PostFlexAdapter(this);
        rcvList = view.findViewById(R.id.recyclerView_help_fragment);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton postNewFlex = view.findViewById(R.id.flex_postBttn);
        postNewFlex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((INavigator)getActivity()).onFlexPostClicked();
            }
        });

        rcvList.setAdapter(adapter);

        //create data and update adapter/recyclerview
        createData();
        adapter.updateFlexPostList(flexPostArrayList);
    }

    private void createData() {
        flexPostArrayList = new ArrayList<FlexPost>();
        Random r = new Random();
        for(int i = 0; i < NUM_ITEMS; i++){
            flexPostArrayList.add(new FlexPost("Matias munkeskider " + i,
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
    }

    @Override
    public void onPostClicked(int index) {
        //todo
    }
}
