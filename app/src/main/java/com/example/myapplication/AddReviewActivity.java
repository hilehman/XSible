package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
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

        TextView parking_t = findViewById(R.id.parking_text);
        TextView accessibility_t = findViewById(R.id.accessibility_text);
        TextView toilet_t = findViewById(R.id.toilet_text);
        TextView service_t = findViewById(R.id.service_text);

        //creates a map of the review fields
        Map<String, Object> reviewsMap = new HashMap<>();

        //takes user input ("extra details")
        EditText extra_s = findViewById(R.id.extra_s);

        Intent toResult = new Intent(this, ResultActivity.class);
        SmileRating smileRating = (SmileRating) findViewById(R.id.smile_rating);
        //takes the review and saves in on the database
        Button saveButton;
        saveButton = findViewById(R.id.save_button);//get the id for button


        //parking switch
        com.suke.widget.SwitchButton parking_b = (com.suke.widget.SwitchButton)
                findViewById(R.id.parking_b);
        parking_b.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Toast.makeText(AddReviewActivity.this, "YAY", Toast.LENGTH_SHORT).show();
                //TODO do your job
            }
        });

        //accessibility switch
        com.suke.widget.SwitchButton accessibility_b = (com.suke.widget.SwitchButton)
                findViewById(R.id.accessibility_b);

        accessibility_b.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Toast.makeText(AddReviewActivity.this, "YAY", Toast.LENGTH_SHORT).show();
                //TODO do your job
            }
        });

        com.suke.widget.SwitchButton toilet_b = (com.suke.widget.SwitchButton)
                findViewById(R.id.toilet_b);

        toilet_b.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Toast.makeText(AddReviewActivity.this, "YAY", Toast.LENGTH_SHORT).show();
                //TODO do your job
            }
        });

        com.suke.widget.SwitchButton service_b = (com.suke.widget.SwitchButton)
                findViewById(R.id.service_b);

        service_b.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Toast.makeText(AddReviewActivity.this, "YAY", Toast.LENGTH_SHORT).show();
                //TODO do your job
            }
        });










        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



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
