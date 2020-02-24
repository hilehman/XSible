package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class placeDisplayActivity extends AppCompatActivity implements Serializable {

  //  public Place chosenPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseFirestore db = FirebaseFirestore.getInstance();  //gets in instance of FireStore database

        String chosenPlaceId;  // takes the chosen place's id from MainACtivity
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
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
            Toast.makeText(placeDisplayActivity.this, chosenPlace.getName() , Toast.LENGTH_LONG).show();
          Map<String, Object>chosenPlaceMap = new HashMap<>();
          chosenPlaceMap.put("id", chosenPlace.getId());
          chosenPlaceMap.put("name", chosenPlace.getName());
          chosenPlaceMap.put("address", chosenPlace.getAddress());

            db.collection("places") //TODO supposed to create collection and add it the data
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

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Toast.makeText(placeDisplayActivity.this, "FAILED" , Toast.LENGTH_LONG).show();
            }
        });





    }







}
