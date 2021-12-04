package dk.au.mad21fall.assignment.sousvideentusiaster.Post;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

import dk.au.mad21fall.assignment.sousvideentusiaster.DetailView.DetailFlexViewModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.CommentModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.PictureModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.INavigator;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;
import dk.au.mad21fall.assignment.sousvideentusiaster.Utils.Permissions;

public class PostFlex extends Fragment {

    private static int GALLERY_ID = 999;
    private ImageView[] imageArray = new ImageView[5];
    private PostFlexViewModel postFlexViewModel;

    Button postBttn, cancelBttn, uploadBttn;
    TextView title, content, temperature, time, uploadCounter, coockedMeat;
    ImageView img01, img02, img03, img04, img05;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_flex_fragment, container, false);
        InitUIElements(view);

        postFlexViewModel = new ViewModelProvider(this).get(PostFlexViewModel.class);
        postFlexViewModel.init();

        return view;
    }


    private FlexPostModel generatePostableModelFromUi() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = firebaseUser.getDisplayName();
        Uri userPhotoUri = firebaseUser.getPhotoUrl();

        ArrayList<PictureModel> pictures = new ArrayList<>();

        // Empty at creation
        ArrayList<CommentModel> comments = new ArrayList<>();

        // TODO is hardcoded atm
        int stars = 4;

        ArrayList<String> labels = new ArrayList();
        labels.add(coockedMeat.getText().toString());

        FlexPostModel flexPost = new FlexPostModel(new Date(), content.getText().toString(),
                Integer.parseInt(time.getText().toString()), Integer.parseInt(temperature.getText().toString()), labels,
                username, stars, userPhotoUri.toString(),
                pictures, comments,
                comments.size(),
                title.getText().toString());


        return flexPost;
    }

    private Boolean validateReqForPost() {
        Boolean isPostable = true;

        // Check fields requriments
        if (TextUtils.isEmpty(title.getText())) {
            title.setError("Title is required!");
            isPostable = false;
        }
        if (TextUtils.isEmpty(content.getText())) {
            content.setError("Please provide some description!");
            isPostable = false;
        }
        if (TextUtils.isEmpty(coockedMeat.getText())) {
            coockedMeat.setError("Please type your cooked meat");
            isPostable = false;
        }
        if (TextUtils.isEmpty(temperature.getText())) {
            temperature.setError("Temperature missing!");
            isPostable = false;
        }
        if (TextUtils.isEmpty(time.getText())) {
            time.setError("Time cooked missing!");
            isPostable = false;
        }

        return isPostable;
    }


    private View.OnClickListener postFlexButtonOnClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (validateReqForPost()) {
                FlexPostModel fromUiModel = generatePostableModelFromUi();
                postFlexViewModel.postNewQuestion(fromUiModel, postFlexViewModel.imagePaths, postFlexViewModel.uploadCounter, getActivity());
                ((INavigator) getActivity()).onFlexPostCancelClicked();
            }
        }
    };



    private String convertUriToAndroidGalleryPath(Uri uri){
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        return picturePath;
    }

    private View.OnClickListener cancelButtonOnClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((INavigator) getActivity()).onFlexPostCancelClicked();
        }
    };

    private View.OnClickListener uploadButtonOnClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Permissions.verifyStoragePermissions(getActivity());
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GALLERY_ID);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_ID) {
            postFlexViewModel.uploadCounter++;
            uploadCounter.setText(String.valueOf(postFlexViewModel.uploadCounter) + "/5");
            imageArray[postFlexViewModel.uploadCounter - 1].setVisibility(View.VISIBLE);
            postFlexViewModel.imagePaths[postFlexViewModel.uploadCounter - 1] = convertUriToAndroidGalleryPath(data.getData());

            if (postFlexViewModel.uploadCounter == 5) {
                uploadBttn.setEnabled(false);
            }


        }
    }


    public void InitUIElements(View view) {

        postBttn = view.findViewById(R.id.post_flex_postBttn);
        cancelBttn = view.findViewById(R.id.post_flex_cancelBttn);
        uploadBttn = view.findViewById(R.id.post_flex_uploadBttn);

        title = view.findViewById(R.id.post_flex_title);
        content = view.findViewById(R.id.post_flex_content_text);
        coockedMeat = view.findViewById(R.id.post_flex_meatCooked);
        temperature = view.findViewById(R.id.post_flex_temperature);
        time = view.findViewById(R.id.post_flex_time);
        uploadCounter = view.findViewById(R.id.post_flex_imageUploadCounter);

        img01 = view.findViewById(R.id.post_flex_image01);
        img02 = view.findViewById(R.id.post_flex_image02);
        img03 = view.findViewById(R.id.post_flex_image03);
        img04 = view.findViewById(R.id.post_flex_image04);
        img05 = view.findViewById(R.id.post_flex_image05);

        img01.setVisibility(View.INVISIBLE);
        img02.setVisibility(View.INVISIBLE);
        img03.setVisibility(View.INVISIBLE);
        img04.setVisibility(View.INVISIBLE);
        img05.setVisibility(View.INVISIBLE);

        imageArray[0] = img01;
        imageArray[1] = img02;
        imageArray[2] = img03;
        imageArray[3] = img04;
        imageArray[4] = img05;

        // SetUp Button clicked Listeners
        uploadBttn.setOnClickListener(uploadButtonOnClickedListener);
        postBttn.setOnClickListener(postFlexButtonOnClickedListener);
        cancelBttn.setOnClickListener(cancelButtonOnClickedListener);
    }


}

