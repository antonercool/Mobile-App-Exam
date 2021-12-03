package dk.au.mad21fall.assignment.sousvideentusiaster.DetailView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.CommentModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.CommentAdapter;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class DetailFlex extends AppCompatActivity implements CommentAdapter.ICommentItemClickedListener {

    private static final String TAG = "DETAIL FLEX VIEW";
    private ArrayList<CommentModel> commentArrayList = new ArrayList<>();

    public static final int NUM_COMMENTS = 1;

    //widgets
    private RecyclerView rcvList;
    private CommentAdapter adapter;
    private SousVideRepository sousVideRepository;
    private String ID;

    EditText addComment;
    TextView title, description, temperature, timeCoocked, numberOfComments, commentDate, commentUser;
    ImageView postedImage;
    Chip chip01, chip02;

    ImageView image_profile, cancel_cross;
    TextView post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_flex);
        sousVideRepository = SousVideRepository.getSousVideRepositoryInstance();

         ID = getIntent().getStringExtra("ID"); //TODO VM

        //create data and update adapter/recyclerview
        setupUIElements();
        updateUI(ID);


        adapter.updateCommentList(commentArrayList);
    }

    @Override
    public void onPostClicked(int index) {

    }

    public void updateUI(String ID){
        sousVideRepository.fetchCommentsByPostID(ID).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CommentModel currentObject = document.toObject(CommentModel.class);
                        commentArrayList.add(currentObject);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                adapter.updateCommentList(commentArrayList);
            }
        });


        sousVideRepository.fetchFlexPostByID(ID).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    FlexPostModel flexPostModel = task.getResult().toObject(FlexPostModel.class);
                    title.setText(flexPostModel.title);
                    description.setText(flexPostModel.description);
                    Glide.with(getApplicationContext()).load(flexPostModel.pictures.get(0).url).into(postedImage);
                    chip01.setText(flexPostModel.labels.get(0));
                    chip01.setText(flexPostModel.labels.get(0));
                    timeCoocked.setText(Integer.toString(flexPostModel.hoursCooked));
                    temperature.setText(Integer.toString(flexPostModel.temp));
                    title.setText(flexPostModel.title);
                    numberOfComments.setText(Integer.toString(flexPostModel.numberOfComments) + " Comment(s)."); //TODO Fetch number of comments

                }
            }
        });
    }
    public void setupUIElements(){
        adapter = new CommentAdapter(this);
        rcvList = findViewById(R.id.flex_commentSection_recyclerView);
        rcvList.setLayoutManager(new LinearLayoutManager(this));

        addComment = findViewById(R.id.add_comment);
        image_profile = findViewById(R.id.image_profile);
        cancel_cross = findViewById(R.id.cancel_cross);
        title = findViewById(R.id.activity_details_flex_title);
        description = findViewById(R.id.activity_details_flex_content);
        postedImage = findViewById(R.id.activity_details_flex_image);
        chip01 = findViewById(R.id.activity_details_flex_chip01);
        chip02 = findViewById(R.id.activity_details_flex_chip02);
        temperature = findViewById(R.id.activity_details_flex_temperature);
        timeCoocked = findViewById(R.id.activity_details_flex_hoursCooked);
        numberOfComments = findViewById(R.id.activity_details_flex_numberOfComments);
        commentDate = findViewById(R.id.commentCreated);
        commentUser = findViewById(R.id.username);

        adapter.addContext(getApplicationContext());

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
        adapter.updateCommentList(commentArrayList);
    }


/*
    private void createData() {
        commentArrayList = new ArrayList<Comment>();
        Random r = new Random();
        for(int i = 0; i < NUM_COMMENTS; i++){
            commentArrayList.add(new Comment("With an extra ImageView we can set the TextView to be baseline aligned with the ImageView and set the android:baselineAlignBottom on the ImageView to true, which will make the baseline of ImageView to bottom." + i,
                    i + " Anton Slimo"
            ));
        }
    }
*/
    private void addComment() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = firebaseUser.getDisplayName();
        Uri userPhotoUri = firebaseUser.getPhotoUrl();
        CommentModel commentModel = new CommentModel(
                new Date(),
                addComment.getText().toString(),
                username,
                userPhotoUri.toString());

        commentArrayList.add(commentModel);

        adapter.updateCommentList(commentArrayList);
        sousVideRepository.postComment(ID, commentModel);
    }
}
