package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.util.HashMap;
import java.util.Map;

public class AddReviewActivity extends AppCompatActivity {

    private int rating = 0;
    String TAG = "AddReviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        FirebaseFirestore db = FirebaseFirestore.getInstance();  //gets an instance of FireStore database
        // takes the chosen place's id from ResultACtivity
        String chosenPlaceId;
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

        TextView parking_b = findViewById(R.id.parking_text);
        TextView accessibility_b = findViewById(R.id.accessibility_text);
        TextView toilet_b = findViewById(R.id.toilet_text);
        TextView service_b = findViewById(R.id.service_text);

        //creates a map of the review fields
        Map<String, Object> reviewsMap = new HashMap<>();
        EditText parking_s = findViewById(R.id.parking_s);
        EditText accessibility_s = findViewById(R.id.accessibility_s);
        EditText toilet_s = findViewById(R.id.toilet_s);
        EditText service_s = findViewById(R.id.service_s);
        EditText extra_s = findViewById(R.id.extra_s);

        Intent toResult = new Intent(this, ResultActivity.class);
        SmileRating smileRating = (SmileRating) findViewById(R.id.smile_rating);
        //takes the review and saves in on the database
        Button saveButton;
        saveButton = findViewById(R.id.save_button);//get the id for button




        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (parking_s.getText().toString() != null) {
                    reviewsMap.put("parking", parking_s.getText().toString());
                }

                if (accessibility_s.getText().toString() != null) {
                    reviewsMap.put("accessibility", accessibility_s.getText().toString());
                }
                if (toilet_s.getText().toString() != null) {
                    reviewsMap.put("toilet", toilet_s.getText().toString());
                }
                if (service_s.getText().toString() != null) {
                    reviewsMap.put("service", service_s.getText().toString());
                }
                if (extra_s.getText().toString() != null) {
                    reviewsMap.put("extra", extra_s.getText().toString());
                }
                db.collection("places").document(chosenPlaceId).collection("reviews").document().
                        set(reviewsMap, SetOptions.merge());
                Toast.makeText(AddReviewActivity.this, "תגובתך נשמרה. תודה!", Toast.LENGTH_LONG).show();
                toResult.putExtra("chosenPlaceId", chosenPlaceId);
                startActivity(toResult);


            }
        });

        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        rating = 1;
                        break;
                    case SmileRating.GOOD:
                        rating = 2;
                        break;
                    case SmileRating.GREAT:
                        rating = 3;
                        break;
                    case SmileRating.OKAY:
                        rating = 4;
                        break;
                    case SmileRating.TERRIBLE:
                        rating = 5;
                        break;
                }
            }
        });

    }
}
