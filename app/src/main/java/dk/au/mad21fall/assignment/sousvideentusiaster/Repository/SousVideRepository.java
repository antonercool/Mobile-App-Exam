package dk.au.mad21fall.assignment.sousvideentusiaster.Repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.FirebaseUtils;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.CommentModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.PictureModel;

public class SousVideRepository {

    private static FirebaseUtils firebaseUtils;
    private static SousVideRepository instance;

    public static SousVideRepository getSousVideRepositoryInstance(){
        if (instance == null){
            instance = new SousVideRepository();
            firebaseUtils = new FirebaseUtils();
        }

        return instance;
    }

    private SousVideRepository(){}


    public Task<DocumentReference> postNewFlexPostAsync(FlexPostModel newPost){
        final Boolean[] returnVal = new Boolean[1];

        return firebaseUtils.getFlexPostsDocumentReference()
                .collection("posts")
                .add(newPost);
    }

    // Refence https://firebase.google.com/docs/storage/android/upload-files
    public Task<Uri> uploadImageAsync(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        String uniqueID = UUID.randomUUID().toString();
        StorageReference ref = firebaseUtils.getFirestoreCloudStorageInstance().getReference("/images/"+uniqueID);
        UploadTask uploadTask = ref.putBytes(byteArray);


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        });

        return urlTask;
    }


    public FlexPostModel getTestFlexObject(){

        ArrayList<CommentModel> comments = new ArrayList<>();

        comments.add(new CommentModel(new Date(), "Sousvide bøf er nice", "peter", "https://someSite1"));
        comments.add(new CommentModel(new Date(), "Nice one ", "anton", "https://someSite2"));

        ArrayList<PictureModel> pictures = new ArrayList<>();
        pictures.add(new PictureModel("https://someSite1"));
        pictures.add(new PictureModel("https://someSite2"));

        ArrayList<String> labels = new ArrayList();
        labels.add("Ripeye");
        labels.add("Potatos");

        FlexPostModel flexPost = new FlexPostModel(new Date(), "Look at this",
                6, 54, labels,
                "Peter", 4, "https://someSite2",
                pictures, comments,
                comments.size());

        return flexPost;
    }









}
