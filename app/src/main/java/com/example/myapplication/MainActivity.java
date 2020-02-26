package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(MainActivity.this, "ברוכים הבאים!", Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(0xffa7a9d6);
        Button button;
        button = findViewById(R.id.button);//get id of button 1

        String apiKey = getString(R.string.api_key);





        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        PlacesFieldSelector fieldSelector = new PlacesFieldSelector();

        final Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldSelector.getAllFields())
                .setHint(("הכנס שם עסק לבדיקת נגישות")).build(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(autocompleteIntent,23 );
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode==23) {
            Place place = Autocomplete.getPlaceFromIntent(intent);
            String chosenPlaceId = place.getId();
            Intent toPlace = new Intent(this, ResultActivity.class);
            toPlace.putExtra("chosenPlaceId", chosenPlaceId);
            startActivity(toPlace);
        }

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



