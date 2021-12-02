package dk.au.mad21fall.assignment.sousvideentusiaster.Post;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.CommentModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.PictureModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.MasterNavigator.INavigator;
import dk.au.mad21fall.assignment.sousvideentusiaster.R;
import dk.au.mad21fall.assignment.sousvideentusiaster.Repository.SousVideRepository;
import dk.au.mad21fall.assignment.sousvideentusiaster.Utils.Permissions;

public class PostFlex extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;
    private static int GALLERY_ID = 999;
    private int upload_counter = 0;
    private ImageView[] imageArray = new ImageView[5];
    private String[] imagePaths = new String[5];

    Button postBttn, cancelBttn, uploadBttn;
    TextView title, content, temperature, time, uploadCounter, coockedMeat;
    ImageView img01, img02, img03, img04, img05;

    private SousVideRepository sousVideRepository;

    public static PostFlex newInstance() {
        return new PostFlex();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_flex_fragment, container, false);
        InitUIElements(view);

        // setUp singleton
        // TODO VM
        sousVideRepository = SousVideRepository.getSousVideRepositoryInstance();

        // SetUp Button clicked Listeners
        uploadBttn.setOnClickListener(uploadButtonOnClickedListener);
        postBttn.setOnClickListener(postFlexButtonOnClickedListener);
        cancelBttn.setOnClickListener(cancelButtonOnClickedListener);

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
                comments.size());


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
                ArrayList<Uri> imageUrls = new ArrayList<>();
                for (int i = 0; i < upload_counter; i++) {
                    sousVideRepository.uploadImageAsync(imagePaths[i].toString()).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                imageUrls.add(downloadUri);
                                fromUiModel.pictures.add(new PictureModel(downloadUri.toString()));

                                if (imageUrls.size() == upload_counter) {

                                    sousVideRepository.postNewFlexPostAsync(fromUiModel)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(getActivity(), "Post uploaded", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });
                }
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

    // TODO use new way to doing this.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_ID) {
            upload_counter++;
            uploadCounter.setText(String.valueOf(upload_counter) + "/5");
            imageArray[upload_counter - 1].setVisibility(View.VISIBLE);
            imagePaths[upload_counter - 1] = convertUriToAndroidGalleryPath(data.getData());

            if (upload_counter == 5) {
                uploadBttn.setEnabled(false);
            }


        }
    }


    public void InitUIElements(View view) {

        postBttn = view.findViewById(R.id.post_help_postBttn);
        cancelBttn = view.findViewById(R.id.post_help_cancelBttn);
        uploadBttn = view.findViewById(R.id.post_flex_uploadBttn);

        title = view.findViewById(R.id.post_help_title);
        content = view.findViewById(R.id.post_help_content_text);
        coockedMeat = view.findViewById(R.id.post_help_meatCooked);
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
    }


}

