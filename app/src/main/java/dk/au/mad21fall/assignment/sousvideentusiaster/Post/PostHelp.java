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

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.CommentModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.PictureModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.QuestionPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.INavigator;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;
import dk.au.mad21fall.assignment.sousvideentusiaster.Utils.Permissions;

public class PostHelp extends Fragment {

    private static int GALLERY_ID = 999;
    private ImageView[] imageArray = new ImageView[5];
    private PostHelpViewModel postHelpViewModel;

    Button postBttn, cancelBttn, uploadBttn;
    TextView title, content, uploadCounter, coockedMeat;
    ImageView img01, img02, img03, img04, img05;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_help_fragment, container, false);
        InitUIElements(view);

        postHelpViewModel = new ViewModelProvider(this).get(PostHelpViewModel.class);
        postHelpViewModel.init();

        return view;
    }


    private QuestionPostModel generatePostableModelFromUi() {
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

        QuestionPostModel questionPost = new QuestionPostModel(new Date(), content.getText().toString(),
               labels, username, userPhotoUri.toString(),
                pictures, comments,
                comments.size(),
                title.getText().toString());

        return questionPost;
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

        return isPostable;
    }


    private View.OnClickListener postFlexButtonOnClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (validateReqForPost()) {
                QuestionPostModel fromUiModel = generatePostableModelFromUi();
                postHelpViewModel.postNewQuestion(fromUiModel, postHelpViewModel.imagePaths, postHelpViewModel.uploadCounter, getActivity());
                ((INavigator) getActivity()).onHelpPostCancelClicked();
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
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GALLERY_ID);
        }
    };

    // TODO use new way to doing this.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_ID) {
            postHelpViewModel.uploadCounter++;
            uploadCounter.setText(String.valueOf(postHelpViewModel.uploadCounter) + "/5");
            imageArray[postHelpViewModel.uploadCounter - 1].setVisibility(View.VISIBLE);
            postHelpViewModel.imagePaths[postHelpViewModel.uploadCounter - 1] = convertUriToAndroidGalleryPath(data.getData());

            if (postHelpViewModel.uploadCounter == 5) {
                uploadBttn.setEnabled(false);
            }


        }
    }


    public void InitUIElements(View view) {
        postBttn = view.findViewById(R.id.post_help_postBttn);
        cancelBttn = view.findViewById(R.id.post_help_cancelBttn);
        uploadBttn = view.findViewById(R.id.post_help_uploadBttn);

        title = view.findViewById(R.id.post_help_title);
        content = view.findViewById(R.id.post_help_content_text);
        coockedMeat = view.findViewById(R.id.post_help_meatCooked);
        uploadCounter = view.findViewById(R.id.post_help_imageUploadCounter);

        img01 = view.findViewById(R.id.post_help_image01);
        img02 = view.findViewById(R.id.post_help_image02);
        img03 = view.findViewById(R.id.post_help_image03);
        img04 = view.findViewById(R.id.post_help_image04);
        img05 = view.findViewById(R.id.post_help_image05);

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

