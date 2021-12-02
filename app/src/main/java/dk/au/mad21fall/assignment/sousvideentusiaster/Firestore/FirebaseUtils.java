package dk.au.mad21fall.assignment.sousvideentusiaster.Firestore;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.Comment;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPost;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.Picture;

public class FirebaseUtils {

    private FirebaseFirestore db;
    private FirebaseStorage imageDb;

    public FirebaseUtils() {
        this.db = FirebaseFirestore.getInstance();
        this.imageDb = FirebaseStorage.getInstance();
    }

    public FirebaseFirestore getFirestoreInstance(){
        return db;
    }

    public DocumentReference getFlexPostsDocumentReference(){
        return this.db.collection("posts").document("flex");
    }

    public DocumentReference getHelpPostsDocumentReference(){
        return this.db.collection("posts").document("help");
    }

    // Refence https://firebase.google.com/docs/storage/android/upload-files
    public Uri uploadImage(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        String uniqueID = UUID.randomUUID().toString();
        StorageReference ref = imageDb.getReference("/images/"+uniqueID);
        UploadTask uploadTask = ref.putBytes(byteArray);
        final Uri[] downloadUri = {null};


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri[0] = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

        return downloadUri[0];
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
