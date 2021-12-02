package dk.au.mad21fall.assignment.sousvideentusiaster.Firestore;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

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
    public FirebaseStorage getFirestoreCloudStorageInstance(){
        return imageDb;
    }

    public DocumentReference getFlexPostsDocumentReference(){
        return this.db.collection("posts").document("flex");
    }

    public DocumentReference getHelpPostsDocumentReference(){
        return this.db.collection("posts").document("help");
    }


}
