package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Toast.makeText(placeDisplayActivity.this, "ברוכים הבאים!", Toast.LENGTH_LONG).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_display);
        PlaceHolder chosenPlace = (PlaceHolder)  getIntent().getSerializableExtra("MyClass");
        Map<String, Object>chosenPlaceMap = new HashMap<>();
        chosenPlaceMap.put("id", chosenPlace.getPlaceId());
        chosenPlaceMap.put("name", chosenPlace.getName());
        chosenPlaceMap.put("address", chosenPlace.getAddress());

        db.collection("places")
                .add(chosenPlaceMap)
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




//        String id = b.getString("ld");
//        String name = b.getString("name");
//        String address = b.getString("address");
//        String grade = PlaceHolder.placesMap.get(id).grade;
//        TextView textViewName = (TextView) findViewById(R.id.placeName);
//        TextView textViewAddress = (TextView) findViewById(R.id.placeAddress);
//        TextView textViewGrade = (TextView) findViewById(R.id.grade);
//
//        textViewName.setText(name);
//        textViewAddress.setText(address);
//        textViewName.setText(grade);
//
//
//
//        EditText editTextGrade = (EditText) findViewById(R.id.grade);
//            String newGrade = editTextGrade.getEditableText().toString();
//            PlaceHolder.placesMap.get(id).grade = newGrade;



    }






}
