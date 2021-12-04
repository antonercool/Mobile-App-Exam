package dk.au.mad21fall.assignment.sousvideentusiaster.DetailView;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
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
import java.util.Date;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.CommentModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.PictureModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.QuestionPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.CommentAdapter;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class DetailHelp extends AppCompatActivity implements CommentAdapter.ICommentItemClickedListener {

    //widgets
    private RecyclerView rcvList;
    private CommentAdapter adapter;
    ImageAdaptor imageAdaptor;
    private DetailHelpViewModel detailHelpViewModel;
    private String ID;

    EditText addComment;
    TextView title, description,
            numberOfComments, postedAtTime, postedUsername;
    ViewPager postedImage;
    Chip chip01, chip02;
    RatingBar stars;

    ImageView image_profile, cancel_cross;
    TextView post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_help);

        ID = getIntent().getStringExtra("ID");
        detailHelpViewModel = new ViewModelProvider(this).get(DetailHelpViewModel.class);
        detailHelpViewModel.Init(ID);

        //create data and update adapter/recyclerview
        setupUIElements();
        updateUI(ID);
    }

    @Override
    public void onPostClicked(int index) {

    }

    public void updateUI(String ID){
        detailHelpViewModel.getHelpPostComments(ID).observe(this, new Observer<ArrayList<CommentModel>>() {
            @Override
            public void onChanged(ArrayList<CommentModel> commentModels) {
                adapter.updateCommentList(commentModels);

                // Increment number of comments
                numberOfComments.setText(commentModels.size() + " Comments(s)");
            }
        });


        detailHelpViewModel.getHelpPostModels(ID).observe(this, new Observer<QuestionPostModel>() {
            @Override
            public void onChanged(QuestionPostModel questionPostModel) {
                if(questionPostModel.title == null){
                    return;
                }
                title.setText(questionPostModel.title);
                description.setText(questionPostModel.description);

                // Load userprofile for comment section
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Uri userPhotoUri = firebaseUser.getPhotoUrl();
                Glide.with(getApplicationContext()).load(userPhotoUri).into(image_profile);

                chip01.setText(questionPostModel.labels.get(0));
                chip02.setText(questionPostModel.labels.get(0));
                title.setText(questionPostModel.title);
                postedUsername.setText(questionPostModel.owner);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M hh:mm");
                postedAtTime.setText(simpleDateFormat.format(questionPostModel.created));
                numberOfComments.setText(Integer.toString(questionPostModel.numberOfComments) + " Comment(s).");

                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                for (PictureModel pictureModel: questionPostModel.pictures){
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(pictureModel.url)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    bitmaps.add(resource);
                                    if (bitmaps.size() == questionPostModel.pictures.size())
                                    {
                                        // request this on ui thread
                                        Runnable toMainUi = new Runnable() {
                                            @Override
                                            public void run() {
                                                imageAdaptor.initImages(bitmaps);
                                                postedImage.setAdapter(imageAdaptor);
                                            }
                                        };
                                        DetailHelp.this.runOnUiThread(toMainUi);
                                    }
                                    return false;
                                }
                            }).submit();
                }
            }
        });
    }

    public void setupUIElements(){
        imageAdaptor = new ImageAdaptor(this);

        adapter = new CommentAdapter(this);
        rcvList = findViewById(R.id.help_commentSection_recyclerView);
        rcvList.setLayoutManager(new LinearLayoutManager(this));

        addComment = findViewById(R.id.add_comment_help);
        image_profile = findViewById(R.id.profile_help_image);
        cancel_cross = findViewById(R.id.cancel_cross_help);
        title = findViewById(R.id.activity_details_help_title);
        description = findViewById(R.id.activity_details_help_content);
        postedImage = findViewById(R.id.activity_details_help_image);
        chip01 = findViewById(R.id.activity_details_help_chip01);
        chip02 = findViewById(R.id.activity_details_help_chip02);
        numberOfComments = findViewById(R.id.activity_details_help_numberOfComments);

        postedAtTime = findViewById(R.id.details_time_help);
        postedUsername = findViewById(R.id.details_username_help);

        adapter.addContext(getApplicationContext());

        post = findViewById(R.id.post_help);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addComment.getText().toString().equals("")){
                    Toast.makeText(DetailHelp.this, "You can't send and empty comment", Toast.LENGTH_SHORT).show();
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
    }

    private void addComment() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = firebaseUser.getDisplayName();
        Uri userPhotoUri = firebaseUser.getPhotoUrl();
        CommentModel commentModel = new CommentModel(
                new Date(),
                addComment.getText().toString(),
                username,
                userPhotoUri.toString());

        // Update firestore and broadcast other view with update
        detailHelpViewModel.postHelpComment(ID, commentModel);
    }
}
