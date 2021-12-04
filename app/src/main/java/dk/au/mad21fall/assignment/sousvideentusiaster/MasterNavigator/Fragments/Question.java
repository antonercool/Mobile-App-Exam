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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.QuestionPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.PostHelpAdapter;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.INavigator;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class Question extends Fragment implements PostHelpAdapter.IPostItemClickedListener {

    //How many items to generate
    public static final int NUM_ITEMS = 20;
    private static final String TAG = "QUESTION";

    //widgets
    private RecyclerView rcvList;
    private PostHelpAdapter adapter;
    private SousVideRepository sousVideRepository;
    private QuestionViewModel questionViewModel;

    //data (should come from Firebase
    private ArrayList<QuestionPostModel> questionPostArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.help_fragment, container, false);
        setupUIElements(view);

        questionViewModel = new ViewModelProvider(getActivity()).get(QuestionViewModel.class);
        questionViewModel.Init();

        updateUi();

        return view;
    }

    private void updateUi() {
        questionViewModel.getHelpPostModels().observe(getActivity(), new Observer<ArrayList<QuestionPostModel>>() {
            @Override
            public void onChanged(ArrayList<QuestionPostModel> questionPostModels) {
                adapter.updateHelpPostList(questionPostModels);
            }
        });
    }


    private void setupUIElements(View view){
        //set up recyclerview with adapter and layout manager
        adapter = new PostHelpAdapter(this);
        adapter.addContext(getContext());
        rcvList = view.findViewById(R.id.recyclerView_help_fragment);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageView postNewHelp = view.findViewById(R.id.help_postBttn);
        postNewHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((INavigator)getActivity()).onHelpPostClicked();
            }
        });

        rcvList.setAdapter(adapter);
    }


    @Override
    public void onPostClicked(String ID) {
        ((INavigator)getActivity()).onDetailHelpClicked(ID);
    }
}
