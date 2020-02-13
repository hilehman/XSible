package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class placeDisplayActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseFirestore db = FirebaseFirestore.getInstance();  //gets in instance of FireStore database

        setContentView(R.layout.activity_place_display);
        PlaceHolder chosenPlace = (PlaceHolder)  getIntent().getSerializableExtra("MyClass"); //gets PlaceHolder from previous activity
        Map<String, Object>chosenPlaceMap = new HashMap<>();
        chosenPlaceMap.put("id", chosenPlace.getPlaceId());
        chosenPlaceMap.put("name", chosenPlace.getName());
        chosenPlaceMap.put("address", chosenPlace.getAddress());

        db.collection("places")
                .add(chosenPlaceMap) //
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("placeDisplayActivity", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("placeDisplayActivity", "Error adding document", e);
                    }
                });
        Toast.makeText(this, "SAVED!", Toast.LENGTH_SHORT).show();
        System.out.println("saved");

    }







}
