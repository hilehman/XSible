package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImagesGalleryActivity extends AppCompatActivity {

    String chosenPlaceId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();  //gets an instance of FireStore database
    private DocumentReference docRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference pathReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_gallery);
        ImageView picture = (ImageView) findViewById(R.id.pic_from_storage);
        FirebaseFirestore db = FirebaseFirestore.getInstance();  //gets an instance of FireStore database
        getSupportActionBar().hide(); //creates full screen
        // takes the chosen place's id from ResultACtivity
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                chosenPlaceId = null;
            } else {
                chosenPlaceId = extras.getString("chosenPlaceId");
                pathReference = storageRef.child(chosenPlaceId);
            }
        } else {
            chosenPlaceId = (String) savedInstanceState.getSerializable("chosenPlaceId");
        }
        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        db.collection("places").document(chosenPlaceId).collection("pictures").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // creates "new review" button
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(ImagesGalleryActivity.this, "No pictures yet", Toast.LENGTH_SHORT).show();
                        } else {
                            // puts every document on a map that goes into a list
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                StorageReference islandRef = storageRef.child((String) document.get("pic"));
                                final long ONE_MEGABYTE = 1024 * 1024;
                                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bitmapPicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        picture.setImageBitmap(bitmapPicture);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });

                            }
                        }
                    }
                });

    }




}
