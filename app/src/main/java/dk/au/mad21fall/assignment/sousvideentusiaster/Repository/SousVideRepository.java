package dk.au.mad21fall.assignment.sousvideentusiaster.Repository;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    public static SousVideRepository getSousVideRepositoryInstance() {
        if (instance == null) {
            instance = new SousVideRepository();
            firebaseUtils = new FirebaseUtils();
        }

        return instance;
    }

    private SousVideRepository() {
    }


    public void postNewFlexPostAsync(FlexPostModel newPost, String[] paths, int numOfImages, Activity activity) {
        ArrayList<Uri> imageUrls = new ArrayList<>();
        for (int i = 0; i < numOfImages; i++) {
            uploadImageAsync(paths[i]).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageUrls.add(downloadUri);
                        newPost.pictures.add(new PictureModel(downloadUri.toString()));

                        if (imageUrls.size() == numOfImages) {

                            firebaseUtils.getFlexPostsDocumentReference()
                                    .collection("posts")
                                    .add(newPost)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(activity, "Post uploaded", Toast.LENGTH_SHORT).show();
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


    // Refence https://firebase.google.com/docs/storage/android/upload-files
    public Task<Uri> uploadImageAsync(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        String uniqueID = UUID.randomUUID().toString();
        StorageReference ref = firebaseUtils.getFirestoreCloudStorageInstance().getReference("/images/" + uniqueID);
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


    public FlexPostModel getTestFlexObject() {

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
