package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class placeDisplayActivity extends AppCompatActivity implements Serializable {

    String chosenPlaceName = "";

    //  public Place chosenPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseFirestore db = FirebaseFirestore.getInstance();  //gets in instance of FireStore database

        String chosenPlaceId;  // takes the chosen place's id from MainACtivity
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                chosenPlaceId = null;
            } else {
                chosenPlaceId = extras.getString("chosenPlaceId");
            }
        } else {
            chosenPlaceId = (String) savedInstanceState.getSerializable("chosenPlaceId");
        }
        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(chosenPlaceId, placeFields);
        final PlacesClient placesClient = Places.createClient(this); //todo is it doing anything?
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place chosenPlace = response.getPlace();
            chosenPlaceName = chosenPlace.getName();
            Map<String, Object> chosenPlaceMap = new HashMap<>(); //creates map with place's details
            chosenPlaceMap.put("id", chosenPlace.getId());
            chosenPlaceMap.put("name", chosenPlace.getName());
            chosenPlaceMap.put("address", chosenPlace.getAddress());
            chosenPlaceMap.put("link", "https://www.google.com/maps/search/?api=1&query=Google&query_place_id=" + chosenPlace.getId());
            db.collection("places").document(chosenPlace.getId()) // creates a document named <placeID> and add it to db
                    .set(chosenPlaceMap, SetOptions.merge()); //
            final TextView place_id = (TextView) findViewById(R.id.place_id); //get the id for TextView
            place_id.setText(chosenPlaceName); //set the text after clicking button
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                // Handle error with given status code.
                Toast.makeText(placeDisplayActivity.this, "FAILED", Toast.LENGTH_LONG).show();
            }
        });

        setContentView(R.layout.activity_place_display); //set the layout
        Map<String, String> tempMap = new HashMap<>();
        final EditText enterOpinion = (EditText) findViewById(R.id.enter_opinion);//get the id for edit text
        Button saveButton;
        saveButton = findViewById(R.id.save_b);//get the id for button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (enterOpinion.getText().toString() != null)//check whether the entered text is not null
                {
                    tempMap.put("opinion", enterOpinion.getText().toString());
                    db.collection("places").document(chosenPlaceId).set(tempMap, SetOptions.merge());
                    Toast.makeText(placeDisplayActivity.this, "SAVED", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(placeDisplayActivity.this, "FAILED", Toast.LENGTH_LONG).show();
                }
            }
        });

        Toast.makeText(placeDisplayActivity.this, "HIHI", Toast.LENGTH_LONG).show();


    }


}
