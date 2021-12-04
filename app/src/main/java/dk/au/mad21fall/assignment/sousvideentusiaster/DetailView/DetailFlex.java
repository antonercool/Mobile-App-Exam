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
import dk.au.mad21fall.assignment.sousvideentusiaster.ListView.CommentAdapter;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.Fragments.FlexViewModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;

public class DetailFlex extends AppCompatActivity implements CommentAdapter.ICommentItemClickedListener {

      //widgets
    private RecyclerView rcvList;
    private CommentAdapter adapter;
    ImageAdaptor imageAdaptor;

    private DetailFlexViewModel detailFlexViewModel;
    private String ID;

    EditText addComment;
    TextView title, description, temperature, timeCoocked,
            numberOfComments, commentDate, commentUser, postedAtTime, postedUsername;
    ViewPager postedImage;
    Chip chip01, chip02;
    RatingBar stars;

    ImageView image_profile, cancel_cross;
    TextView post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_flex);

        ID = getIntent().getStringExtra("ID");
        detailFlexViewModel = new ViewModelProvider(this).get(DetailFlexViewModel.class);
        detailFlexViewModel.Init(ID);

        setupUIElements();
        updateUI(ID);
    }

    @Override
    public void onPostClicked(int index) {

    }

    public void updateUI(String ID){
        detailFlexViewModel.getFlexPostComments(ID).observe(this, new Observer<ArrayList<CommentModel>>() {
            @Override
            public void onChanged(ArrayList<CommentModel> commentModels) {
                adapter.updateCommentList(commentModels);

                // Increment number of comments
                numberOfComments.setText(commentModels.size() + " Comment(s).");
            }
        });

        detailFlexViewModel.getFlexPostModels(ID).observe(this, new Observer<FlexPostModel>() {
            @Override
            public void onChanged(FlexPostModel flexPostModel) {
                if(flexPostModel.title == null){
                    return;
                }
                title.setText(flexPostModel.title);
                description.setText(flexPostModel.description);

                // Load userprofile for comment section
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Uri userPhotoUri = firebaseUser.getPhotoUrl();
                Glide.with(getApplicationContext()).load(userPhotoUri).into(image_profile);

                chip01.setText(flexPostModel.labels.get(0));
                chip02.setText(flexPostModel.labels.get(0));
                timeCoocked.setText(Integer.toString(flexPostModel.hoursCooked) + " Hour(s).");
                temperature.setText(Integer.toString(flexPostModel.temp) + " Degrees.");
                title.setText(flexPostModel.title);
                postedUsername.setText(flexPostModel.owner);
                stars.setNumStars(flexPostModel.stars);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M hh:mm");
                postedAtTime.setText(simpleDateFormat.format(flexPostModel.created));
                numberOfComments.setText(Integer.toString(flexPostModel.numberOfComments) + " Comment(s).");

                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                for (PictureModel pictureModel: flexPostModel.pictures){
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
                                    if (bitmaps.size() == flexPostModel.pictures.size())
                                    {
                                        // request this on ui thread
                                        Runnable toMainUi = new Runnable() {
                                            @Override
                                            public void run() {
                                                imageAdaptor.initImages(bitmaps);
                                                postedImage.setAdapter(imageAdaptor);
                                            }
                                        };
                                        DetailFlex.this.runOnUiThread(toMainUi);
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
        stars = findViewById(R.id.list_item_ratingbar2);
        stars.setMax(5);

        postedAtTime = findViewById(R.id.details_time);
        postedUsername = findViewById(R.id.details_username);

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
       detailFlexViewModel.postFlexComment(ID, commentModel);
    }
}
