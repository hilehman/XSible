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

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            /*    Intent mainToMap = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mainToMap);*/

                PlacesFieldSelector fieldSelector = new PlacesFieldSelector();
                Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldSelector.getAllFields())
                        .build(MainActivity.this); // QQQ what exactly does it do??

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

        Toast.makeText(MainActivity.this,"1111111111",Toast.LENGTH_LONG).show();

        super.onActivityResult(requestCode, resultCode, intent);
        Intent mainToMap = new Intent(MainActivity.this, MapsActivity.class);
        Toast.makeText(MainActivity.this,"2222222222",Toast.LENGTH_LONG).show();

        if (requestCode==23) {
            Toast.makeText(MainActivity.this,"TETTTTTT",Toast.LENGTH_LONG).show();

            //  Place place = Autocomplete.getPlaceFromIntent(intent);
            startActivity(mainToMap);

//                 place.getName()//
//                 place.getAddress();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

            Status status = Autocomplete.getStatusFromIntent(intent);
        } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
            Toast.makeText(MainActivity.this,"ASDASD",Toast.LENGTH_LONG).show();

            // The user canceled the operation.
        }
        else
        Toast.makeText(MainActivity.this, "PIPIPI", Toast.LENGTH_LONG).show();


    }
}



