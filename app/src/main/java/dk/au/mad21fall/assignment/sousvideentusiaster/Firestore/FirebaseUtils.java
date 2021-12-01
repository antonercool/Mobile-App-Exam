package dk.au.mad21fall.assignment.sousvideentusiaster.Firestore;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

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

    public void uploaderImages(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        imageDb.getReference("/images").putBytes(byteArray)
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

    }




}
