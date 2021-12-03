package dk.au.mad21fall.assignment.sousvideentusiaster.DetailView;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.Comment;
import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.CommentAdapter;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.INavigator;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;

public class DetailFlex extends AppCompatActivity implements CommentAdapter.ICommentItemClickedListener {

    private ArrayList<Comment> commentArrayList;

    public static final int NUM_COMMENTS = 1;

    //widgets
    private RecyclerView rcvList;
    private CommentAdapter adapter;

    EditText addComment;
    ImageView image_profile, cancel_cross;
    TextView post;

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

        addComment = findViewById(R.id.add_comment);
        image_profile = findViewById(R.id.image_profile);
        cancel_cross = findViewById(R.id.cancel_cross);
        post = findViewById(R.id.post);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addComment.getText().toString().equals("")){
                    Toast.makeText(DetailFlex.this, "You can't send and empty comment", Toast.LENGTH_SHORT).show();
                }else{
                    addComment();
                    addComment.setText("");
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    adapter.updateCommentList(commentArrayList);
                }
            }
        });

        cancel_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rcvList.setAdapter(adapter);
        createData();
        adapter.updateCommentList(commentArrayList);
    }



    private void createData() {
        commentArrayList = new ArrayList<Comment>();
        Random r = new Random();
        for(int i = 0; i < NUM_COMMENTS; i++){
            commentArrayList.add(new Comment("With an extra ImageView we can set the TextView to be baseline aligned with the ImageView and set the android:baselineAlignBottom on the ImageView to true, which will make the baseline of ImageView to bottom." + i,
                    i + " Anton Slimo"
            ));
        }
    }

    private void addComment() {
        commentArrayList.add(new Comment(
                addComment.getText().toString(),
                "Test publisher"
        ));
    }
}
