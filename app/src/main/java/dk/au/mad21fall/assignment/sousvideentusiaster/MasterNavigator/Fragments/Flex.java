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
import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.PostFlexAdapter;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.INavigator;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class Flex extends Fragment implements PostFlexAdapter.IPostItemClickedListener {
    
    //widgets
    private RecyclerView rcvList;
    private PostFlexAdapter adapter;
    private FlexViewModel flexViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.flex_fragment, container, false);
        setupUIElements(view);
        flexViewModel = new ViewModelProvider(getActivity()).get(FlexViewModel.class);
        flexViewModel.Init();
        updateUi();

        return view;
    }

    private void updateUi() {
        flexViewModel.getFlexPostModels().observe(getActivity(), new Observer<ArrayList<FlexPostModel>>() {
            @Override
            public void onChanged(ArrayList<FlexPostModel> flexPostModels) {
                adapter.updateFlexPostList(flexPostModels);
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
    }

    @Override
    public void onPostClicked(String ID) {
        ((INavigator)getActivity()).onDetailFlexClicked(ID);
    }
}
