package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.Serializable;

import static com.google.android.libraries.places.api.model.TypeFilter.ESTABLISHMENT;

public class MainActivity extends AppCompatActivity implements Serializable {


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadLocale();
        // create a full screen window
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        // adapts the image to the size of the display */

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.main_background), size.x, size.y, true);

        Button button;
        button = findViewById(R.id.search_button);


        String apiKey = getString(R.string.api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBZKbr8RFXazrIAw6fD7705MMdcL1idUdA");
        }

        // Create a new Places client instance.
        PlacesFieldSelector fieldSelector = new PlacesFieldSelector();
        String searchHint = getString(R.string.search_hint);
        final Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldSelector.getAllFields())
                .setHint((searchHint)).setCountry("il").setTypeFilter(ESTABLISHMENT).build(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(autocompleteIntent, 10 );
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Intent toMain = new Intent(this, ResultActivity.class);
        if (requestCode == 10) {
            if (resultCode == -1) {
                Place place = Autocomplete.getPlaceFromIntent(intent);
                String chosenPlaceId = place.getId();
                Intent toPlace = new Intent(this, ResultActivity.class);
                toPlace.putExtra("chosenPlaceId", chosenPlaceId);
                startActivity(toPlace);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();

            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
            }
        }


    }



}