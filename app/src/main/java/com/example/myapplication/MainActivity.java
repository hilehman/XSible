package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.Serializable;
import java.util.Locale;

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


        // fills the background ImageView with the resized image
        ImageView iv_background = (ImageView) findViewById(R.id.main_background);
        iv_background.setImageBitmap(bmp);

        //setLocale("en");

        Button button;
        button = findViewById(R.id.search_button);//get id of button 1



        String apiKey = getString(R.string.api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBZKbr8RFXazrIAw6fD7705MMdcL1idUdA");
        }

        // Create a new Places client instance.
        PlacesFieldSelector fieldSelector = new PlacesFieldSelector();

        final Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldSelector.getAllFields())
                .setHint(("הכנס שם עסק לבדיקת נגישות")).build(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(autocompleteIntent, 23);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 23) {
            Place place = Autocomplete.getPlaceFromIntent(intent);
            String chosenPlaceId = place.getId();
            Intent toPlace = new Intent(this, ResultActivity.class);
            toPlace.putExtra("chosenPlaceId", chosenPlaceId);
            startActivity(toPlace);
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(intent);
        } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, "CANCELED", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_LONG).show();
    }



}