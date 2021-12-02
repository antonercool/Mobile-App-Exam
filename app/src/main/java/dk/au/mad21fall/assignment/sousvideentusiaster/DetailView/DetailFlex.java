package dk.au.mad21fall.assignment.sousvideentusiaster.DetailView;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.Comment;
import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.CommentAdapter;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;

public class DetailFlex extends AppCompatActivity implements CommentAdapter.ICommentItemClickedListener {

    private ArrayList<Comment> commentArrayList;

    public static final int NUM_COMMENTS = 5;

    //widgets
    private RecyclerView rcvList;
    private CommentAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_flex);

        //create data and update adapter/recyclerview
        setupUIElements();

        adapter.updateCommentList(commentArrayList);
    }

    @Override
    public void onPostClicked(int index) {

    }

    public void setupUIElements(){
        adapter = new CommentAdapter(this);
        rcvList = findViewById(R.id.flex_commentSection_recyclerView);
        rcvList.setLayoutManager(new LinearLayoutManager(this));

        rcvList.setAdapter(adapter);
        createData();
        adapter.updateCommentList(commentArrayList);
    }

    private void createData() {
        commentArrayList = new ArrayList<Comment>();
        Random r = new Random();
        for(int i = 0; i < NUM_COMMENTS; i++){
            commentArrayList.add(new Comment("Matias munkeskider " + i,
                    i + " Anton Slimo"
            ));
        }
    }
}
