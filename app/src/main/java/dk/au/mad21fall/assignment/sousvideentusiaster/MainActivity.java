package dk.au.mad21fall.assignment.sousvideentusiaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.FirebaseUtils;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.Comment;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPost;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.Picture;

public class MainActivity extends AppCompatActivity  {

    private static int RESULT_LOAD_IMAGE = 1;

    private Button fetchButton, uploadButton, uploadImageButton;
    private TextView text;
    private ImageView image;

    private static final String TAG = "Main Activity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION= "description";

    private FirebaseFirestore db;
    private FirebaseUtils firestoreUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUiElements();
        firestoreUtils = new FirebaseUtils();
        db = firestoreUtils.getFirestoreInstance();
    }

    private void setUpUiElements(){
        fetchButton = findViewById(R.id.fetch_button);
        uploadButton = findViewById(R.id.upload_button);
        uploadImageButton = findViewById(R.id.upload_image_button);
        text = findViewById(R.id.text_test);
        image = findViewById(R.id.image_test);

        uploadButton.setOnClickListener(uploadListener);
        uploadImageButton.setOnClickListener(uploadImageListener);
    }

    private View.OnClickListener uploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            FlexPost newPost = getFlexObject();
            CollectionReference bla = db.collection("posts");


            firestoreUtils.getFlexPostsDocumentReference()
                    .collection("posts")
                    .add(newPost)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    };


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    private View.OnClickListener uploadImageListener = new View.OnClickListener() {


        @Override
        public void onClick(View view) {
            verifyStoragePermissions();
            Intent i = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            // String picturePath contains the path of selected Image

            firestoreUtils.uploaderImages(picturePath);
        }
    }


    public FlexPost getFlexObject(){

        ArrayList<Comment> comments = new ArrayList<>();

        comments.add(new Comment(new Date(), "Sousvide bøf er nice", "peter", "https://someSite1"));
        comments.add(new Comment(new Date(), "Nice one ", "anton", "https://someSite2"));

        ArrayList<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture("https://someSite1"));
        pictures.add(new Picture("https://someSite2"));

        ArrayList<String> labels = new ArrayList();
        labels.add("Ripeye");
        labels.add("Potatos");

        FlexPost flexPost = new FlexPost(new Date(), "Look at this",
                              6, 54, labels,
                              "Peter", 4, "https://someSite2",
                                    pictures, comments,
                                    comments.size());
        return flexPost;
    }


}