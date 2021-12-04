package dk.au.mad21fall.assignment.sousvideentusiaster.Repository;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.FirebaseUtils;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.CommentModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.FlexPostModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.PictureModel;
import dk.au.mad21fall.assignment.sousvideentusiaster.Firestore.Models.QuestionPostModel;

public class SousVideRepository {

    private static final String TAG = "SOUS_VIDE_REPOSITORY";
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
                        Log.e(TAG, "Failed to add new flex post: ");
                    }
                }
            });
        }
    }


    // Refence https://firebase.google.com/docs/storage/android/upload-files
    private Task<Uri> uploadImageAsync(String path) {
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

    public CollectionReference subscribeToFlexComments(String ID){
        return firebaseUtils.getFlexPostsDocumentReference()
                .collection("posts")
                .document(ID)
                .collection("comments");
    }

    public DocumentReference subscribeToPostById(String ID){
        return firebaseUtils.getFlexPostsDocumentReference()
                .collection("posts")
                .document(ID);
    }

    public CollectionReference subscribeToFlexPosts(){
        return firebaseUtils.getFlexPostsDocumentReference()
                .collection("posts");
    }


    public Task<QuerySnapshot> fetchNewestFlex(int limit) {

        //asynchronously retrieve multiple documents
        Task<QuerySnapshot> fetchTask =  firebaseUtils.getFlexPostsDocumentReference()
                .collection("posts")
                .orderBy("created", Query.Direction.ASCENDING)
                .limit(limit)
                .get();

        return fetchTask;
    }

    public Task<DocumentSnapshot> fetchFlexPostByID(String ID) {

        //asynchronously retrieve multiple documents
        Task<DocumentSnapshot> fetchTask =  firebaseUtils.getFlexPostsDocumentReference()
                .collection("posts")
                .document(ID).get();

        return fetchTask;
    }

    public Task<QuerySnapshot> fetchFlexCommentsByPostID(String ID) {

        //asynchronously retrieve multiple documents
        Task<QuerySnapshot> fetchTask =  firebaseUtils.getFlexPostsDocumentReference()
                .collection("posts")
                .document(ID)
                .collection("comments")
                .get();

        return fetchTask;
    }


    public Task<DocumentReference> postFlexComment(String postID, CommentModel commentModel) {

        //asynchronously retrieve multiple documents
        Task<DocumentReference> commentUploadTask =  firebaseUtils.getFlexPostsDocumentReference()
                .collection("posts")
                .document(postID)
                .collection("comments")
                .add(commentModel);

        firebaseUtils.getFlexPostsDocumentReference()
                .collection("posts")
                .document(postID)
                .update("numberOfComments", FieldValue.increment(1));

        return commentUploadTask;
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
                comments.size(),
                "ANTON GLEMTE TITLE");

        return flexPost;
    }

    public void postNewQuestionPostAsync(QuestionPostModel newPost, String[] paths, int numOfImages, Activity activity) {
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

                            firebaseUtils.getHelpPostsDocumentReference()
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
                        Log.e(TAG, "Failed to add new question post: ");
                    }
                }
            });
        }
    }

    public Task<QuerySnapshot> fetchNewestHelp(int limit) {

        //asynchronously retrieve multiple documents
        Task<QuerySnapshot> fetchTask =  firebaseUtils.getHelpPostsDocumentReference()
                .collection("posts")
                .orderBy("created", Query.Direction.ASCENDING)
                .limit(limit)
                .get();

        return fetchTask;
    }

    public Task<DocumentSnapshot> fetchHelpPostByID(String ID) {

        //asynchronously retrieve multiple documents
        Task<DocumentSnapshot> fetchTask =  firebaseUtils.getHelpPostsDocumentReference()
                .collection("posts")
                .document(ID).get();

        return fetchTask;
    }

    public CollectionReference subscribeToHelpPosts(){
        return firebaseUtils.getHelpPostsDocumentReference()
                .collection("posts");
    }


    public CollectionReference subscribeToHelpComments(String ID){
        return firebaseUtils.getHelpPostsDocumentReference()
                .collection("posts")
                .document(ID)
                .collection("comments");
    }

    public Task<QuerySnapshot> fetchHelpCommentsByPostID(String ID) {

        //asynchronously retrieve multiple documents
        Task<QuerySnapshot> fetchTask =  firebaseUtils.getHelpPostsDocumentReference()
                .collection("posts")
                .document(ID)
                .collection("comments")
                .get();

        return fetchTask;
    }

    public Task<DocumentReference> postHelpComment(String postID, CommentModel commentModel) {

        //asynchronously retrieve multiple documents
        Task<DocumentReference> commentUploadTask =  firebaseUtils.getHelpPostsDocumentReference()
                .collection("posts")
                .document(postID)
                .collection("comments")
                .add(commentModel);

        firebaseUtils.getHelpPostsDocumentReference()
                .collection("posts")
                .document(postID)
                .update("numberOfComments", FieldValue.increment(1));

        return commentUploadTask;
    }



}
