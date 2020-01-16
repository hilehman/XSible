package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable {


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(MainActivity.this, "ברוכים הבאים!", Toast.LENGTH_LONG).show();
        Button button;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);//get id of button 1

        String apiKey = getString(R.string.api_key);





        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        final PlacesClient placesClient = Places.createClient(this);
        PlacesFieldSelector fieldSelector = new PlacesFieldSelector();

        final Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldSelector.getAllFields())
                .setHint(("הכנס שם עסק לבדיקת נגישות")).build(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          /*      Intent mainToMap = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mainToMap);*/

                startActivityForResult(autocompleteIntent,23 );

//________________________________________________________________________________

/*                int AUTOCOMPLETE_REQUEST_CODE = 1;

// Set the fields to specify which types of place data to
// return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

// Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);*/

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {



        super.onActivityResult(requestCode, resultCode, intent);


        if(requestCode==23) {
            Place place = Autocomplete.getPlaceFromIntent(intent);
//            Intent toPlace = new Intent(this, placeDisplayActivity.class);
//            PlaceHolder newPlace = new PlaceHolder(place.getId(), place.getName(), place.getAddress());
//            toPlace.putExtra("id",place.getId());
//            toPlace.putExtra("name",place.getName());
//            toPlace.putExtra("address",place.getAddress());
//            startActivity(toPlace);

        }



      /*  if (requestCode==23) { //sends to the place on google maps
            Place place = Autocomplete.getPlaceFromIntent(intent);
            Toast.makeText(MainActivity.this,place.getName(),Toast.LENGTH_LONG).show();
            LatLng latLng = place.getLatLng();
            Intent toMap = new Intent(this, MapsActivity.class);
            toMap.putExtra("lat",place.getLatLng().latitude);
            toMap.putExtra("lng",place.getLatLng().longitude);
            startActivity(toMap);*/
//
         else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

            Status status = Autocomplete.getStatusFromIntent(intent);
        } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
            Toast.makeText(MainActivity.this,"ASDASD",Toast.LENGTH_LONG).show();

            // The user canceled the operation.
        }
        else
        Toast.makeText(MainActivity.this, "PIPIPI", Toast.LENGTH_LONG).show();


    }
}



